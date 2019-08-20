(ns com.sixsq.slipstream.ssclj.resources.session-jwt-lifecycle-test
  (:require
    [aleph.tcp :as tcp]
    [buddy.core.codecs :as codecs]
    [clojure.data.json :as json]
    [clojure.string :as str]
    [clojure.test :refer [deftest is use-fixtures]]
    [com.sixsq.slipstream.auth.utils.sign :as sign]
    [com.sixsq.slipstream.ssclj.app.params :as p]
    [com.sixsq.slipstream.ssclj.middleware.authn-info-header :refer [authn-info-header]]
    [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
    [com.sixsq.slipstream.ssclj.resources.lifecycle-test-utils :as ltu]
    [com.sixsq.slipstream.ssclj.resources.session :as session]
    [com.sixsq.slipstream.ssclj.resources.session-jwt.aclib :as aclib]
    [com.sixsq.slipstream.ssclj.resources.session-jwt.utils :as jwt-utils]
    [com.sixsq.slipstream.ssclj.resources.session-template :as ct]
    [com.sixsq.slipstream.ssclj.resources.session-template :as st]
    [com.sixsq.slipstream.ssclj.resources.session-template-jwt :as jwt]
    [manifold.deferred :as d]
    [manifold.stream :as stream]
    [peridot.core :refer :all]))


(use-fixtures :each ltu/with-test-server-fixture)


(def base-uri (str p/service-context (u/de-camelcase session/resource-name)))


(def session-template-base-uri (str p/service-context (u/de-camelcase ct/resource-name)))


(def instance "test-instance")


(def authn-token (sign/sign-claims {:iss         "device-id"
                                    :test-result "OK"}))


(def session-template-jwt {:method      jwt/authn-method
                           :instance    instance
                           :name        "JWT Authentication"
                           :description "External Authentication with JWT"
                           :token       "provide-valid-authentication-token"
                           :acl         st/resource-acl})


(defn echo-handler
  "Handler that applies the given function f to an incoming message, writing
   the result to the same stream. The signature for f is f(msg), where msg is a
   byte stream. Code taken from the aleph.tcp example."
  [f]
  (fn [s info]
    (d/loop []
            ;; take a message, and define a default value that tells us if the connection is closed
            (-> (stream/take! s ::none)
                (d/chain
                  ;; first, check if there even was a message, and then transform it on another thread
                  (fn [msg]
                    (if (= ::none msg)
                      ::none
                      (d/future (f msg))))
                  ;; once the transformation is complete, write it back to the client
                  (fn [msg']
                    (when-not (= ::none msg')
                      (stream/put! s msg')))
                  ;; if we were successful in our response, recur and repeat
                  (fn [result]
                    (when result
                      (d/recur))))
                ;; if there were any issues on the far end, send a stringified exception back
                ;; and close the connection
                (d/catch
                  (fn [ex]
                    (stream/put! s (str "ERROR: " ex))
                    (stream/close! s)))))))


(defn test-result
  "Takes the msg (a byte stream) converts it to a string, parses it as JSON,
   takes the token value, extracts the claims, and returns the :test-result in
   the object. This can be used to provide a specific result when testing the
   token verification."
  [msg]
  (some-> msg
          codecs/bytes->str
          (json/read-str :key-fn keyword)
          :token
          jwt-utils/extract-claims
          :test-result))


(defn start-server
  [port]
  (let [handler (echo-handler test-result)]
    (tcp/start-server handler {:port port})))


(defn assoc-token
  [template token-map]
  (assoc-in template [:sessionTemplate :token] (when token-map (sign/sign-claims token-map))))


(deftest sanity-check-test-aclib-server
  (let [timeout 1000]
    (with-open [server (start-server aclib/port)
                client @(tcp/client {:host aclib/host, :port aclib/port})]

      (let [token  (sign/sign-claims {:test-result "OK"})
            msg    (str (json/write-str {:typ "jwt", :token token}) "\n")
            result (when @(stream/try-put! client msg timeout)
                     (some-> @(stream/try-take! client timeout)
                             (codecs/bytes->str)))]
        (is (= "OK" result)))

      (let [token  (sign/sign-claims {:test-result "err1"})
            msg    (str (json/write-str {:typ "jwt", :token token}) "\n")
            result (when @(stream/try-put! client msg timeout)
                     (some-> @(stream/try-take! client timeout)
                             (codecs/bytes->str)))]
        (is (= "err1" result))))))


(deftest lifecycle

  (with-open [server (start-server aclib/port)]

    (let [app           (ltu/ring-app)
          session-json  (content-type (session app) "application/json")
          session-anon  (header session-json authn-info-header "unknown ANON")
          session-user  (header session-json authn-info-header "user USER")
          session-admin (header session-json authn-info-header "root ADMIN")]

      ;;
      ;; create the session template and the configuration for the tests
      ;;

      (let [href         (-> session-admin
                             (request session-template-base-uri
                                      :request-method :post
                                      :body (json/write-str session-template-jwt))
                             (ltu/body->edn)
                             (ltu/is-status 201)
                             (ltu/location))

            template-url (str p/service-context href)]

        ;; verify that the session template exists
        (-> session-anon
            (request template-url)
            (ltu/body->edn)
            (ltu/is-status 200))

        ;; anonymous query should succeed but have no entries
        (-> session-anon
            (request base-uri)
            (ltu/body->edn)
            (ltu/is-status 200)
            (ltu/is-count zero?))

        (let [name-attr        "name"
              description-attr "description"
              properties-attr  {:a "one", :b "two"}

              valid-create     {:name            name-attr
                                :description     description-attr
                                :properties      properties-attr
                                :sessionTemplate {:href  href
                                                  :token authn-token}}

              invalid-create   (assoc-in valid-create [:sessionTemplate :invalid] "BAD")]

          ;; invalid create should return a 400
          (-> session-anon
              (request base-uri
                       :request-method :post
                       :body (json/write-str invalid-create))
              (ltu/body->edn)
              (ltu/is-status 400))

          ;; no token provided on create
          (-> session-anon
              (request base-uri
                       :request-method :post
                       :body (json/write-str (assoc-token valid-create nil)))
              (ltu/body->edn)
              (ltu/message-matches #"(?s).*resource does not satisfy defined schema.*")
              (ltu/is-status 400))

          ;; invalid token provided on create
          (-> session-anon
              (request base-uri
                       :request-method :post
                       :body (json/write-str (assoc-in valid-create [:sessionTemplate :token] "invalid")))
              (ltu/body->edn)
              (ltu/message-matches #"(?s).*cannot parse JWT.*")
              (ltu/is-status 400))

          ;; no iss attribute in token
          (-> session-anon
              (request base-uri
                       :request-method :post
                       :body (json/write-str (assoc-token valid-create {:sub "no-issuer"})))
              (ltu/body->edn)
              (ltu/message-matches #"(?s).*JWT token is missing issuer \(iss\) attribute.*")
              (ltu/is-status 400))

          ;; validation fails
          (-> session-anon
              (request base-uri
                       :request-method :post
                       :body (json/write-str (assoc-token valid-create {:iss         "issuer"
                                                                        :test-result "err1"})))
              (ltu/body->edn)
              (ltu/message-matches #"(?s).*token validation error 'err1'.*")
              (ltu/is-status 400))

          (let [resp    (-> session-anon
                            (request base-uri
                                     :request-method :post
                                     :body (json/write-str valid-create))
                            (ltu/body->edn)
                            (ltu/is-status 201)
                            (ltu/is-set-cookie))

                id      (get-in resp [:response :body :resource-id])
                token   (get-in resp [:response :cookies "com.sixsq.slipstream.cookie" :value :token])
                claims  (if token (sign/unsign-claims token) {})
                uri     (-> resp ltu/location)
                abs-uri (str p/service-context uri)]

            ;; check claims in cookie
            (is (= "device-id" (:username claims)))
            (is (= #{"USER" "ANON" id} (some-> claims :roles (str/split #"\s+") set)))
            (is (= uri (:session claims)))
            (is (not (nil? (:exp claims))))

            ;; user should not be able to see session without session role
            (-> session-user
                (request abs-uri)
                (ltu/body->edn)
                (ltu/is-status 403))

            ;; anonymous query should succeed but still have no entries
            (-> session-anon
                (request base-uri)
                (ltu/body->edn)
                (ltu/is-status 200)
                (ltu/is-count zero?))

            ;; user query should succeed but have no entries because of missing session role
            (-> session-user
                (request base-uri)
                (ltu/body->edn)
                (ltu/is-status 200)
                (ltu/is-count zero?))

            ;; admin query should succeed, but see no sessions without the correct session role
            (-> session-admin
                (request base-uri)
                (ltu/body->edn)
                (ltu/is-status 200)
                (ltu/is-count zero?))

            ;; user should be able to see session with session role
            (-> (session app)
                (header authn-info-header (str "user USER " id))
                (request abs-uri)
                (ltu/body->edn)
                (ltu/is-status 200)
                (ltu/is-id id)
                (ltu/is-operation-present "delete")
                (ltu/is-operation-absent "edit"))

            ;; check contents of session
            (let [{:keys [name description properties] :as body} (-> session-user
                                                                     (header authn-info-header (str "user USER ANON " id))
                                                                     (request abs-uri)
                                                                     (ltu/body->edn)
                                                                     :response
                                                                     :body)]
              (is (= name name-attr))
              (is (= description description-attr))
              (is (= properties properties-attr)))

            ;; user query with session role should succeed and have one entry
            (-> (session app)
                (header authn-info-header (str "user USER " id))
                (request base-uri)
                (ltu/body->edn)
                (ltu/is-status 200)
                (ltu/is-count 1))

            ;; user with session role can delete resource
            (-> (session app)
                (header authn-info-header (str "user USER " id))
                (request abs-uri
                         :request-method :delete)
                (ltu/is-unset-cookie)
                (ltu/body->edn)
                (ltu/is-status 200))))))))

