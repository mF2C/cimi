(ns com.sixsq.slipstream.ssclj.resources.identification-lifecycle-test
  (:require
    [clojure.data.json :as json]
    [clojure.test :refer :all]
    [com.sixsq.slipstream.ssclj.app.params :as p]
    [com.sixsq.slipstream.ssclj.middleware.authn-info-header :refer [authn-info-header]]
    [com.sixsq.slipstream.ssclj.resources.common.schema :as c]
    [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
    [com.sixsq.slipstream.ssclj.resources.example-resource.utils :as utils]
    [com.sixsq.slipstream.ssclj.resources.lifecycle-test-utils :as ltu]
    [com.sixsq.slipstream.ssclj.resources.identification :as identification]
    [peridot.core :refer :all]))

(use-fixtures :each ltu/with-test-server-fixture)

(def base-uri (str p/service-context (u/de-camelcase identification/resource-url)))

(deftest lifecycle
  (let [session (-> (ltu/ring-app)
                    session
                    (content-type "application/json"))
        session-admin (header session authn-info-header "root ADMIN USER ANON")
        session-user (header session authn-info-header "jane USER ANON")
        session-anon (header session authn-info-header "unknown ANON")]


    ;; admin collection query should succeed but be empty
    (-> session-admin
        (request base-uri)
        (ltu/body->edn)
        (ltu/is-status 200)
        (ltu/is-count zero?)
        (ltu/is-operation-present "add")
        (ltu/is-operation-absent "delete")
        (ltu/is-operation-absent "edit")
        (ltu/is-operation-absent "execute"))

    ;; user collection query should not succeed
    (-> session-user
        (request base-uri)
        (ltu/body->edn)
        ;(ltu/is-status 403)
    )

    ;; anonymous collection query should not succeed
    (-> session-anon
        (request base-uri)
        (ltu/body->edn)
        (ltu/is-status 403)
    )

    ;; create a callback as an admin
    (let [resource-name         "IdentificationResource"
          resource-url          (u/de-camelcase resource-name)
          create-test-callback  {:id                    (str resource-url "/identification-resource")
                                 :resourceURI           base-uri
                                ;  :acl                   {:owner {:principal "ADMIN"
                                ;                                  :type      "ROLE"}
                                ;                          :rules [{:principal "ADMIN"
                                ;                                   :type      "ROLE"
                                ;                                   :right     "ALL"}]}
                                                                 ;{:principal "ANON"
                                                                  ;:type      "ROLE"
                                                                  ;:right     "MODIFY"}]}
							     :user          "abcdefghij"
                                 :idkey         "998F36E8DCD68C6B97770DAF3AED5BD2AA2AC4DA4EF71E8EBE94F32FCE7D0F8C30BC9BE502CA79FA9CA66E8E8E00EC0A21BF8EC3252736DF01F54FF40AE6FD6E"
                                 :deviceid      "2EBD853CDA3D0643AFA3619293DFA829338473A5149254A9CD2A28532F8E0E7E1F465F91D5E5F296791D9F2FBBB9122995360B258837612676EB7CC9F510240E"}
          resp-test             (-> session-admin
                                  (request base-uri
                                           :request-method :post
                                           :body (json/write-str create-test-callback))
                                  (ltu/body->edn)
                                  (ltu/is-status 201))
          id-test               (get-in resp-test [:response :body :resource-id])
          location-test         (str p/service-context (-> resp-test ltu/location))
          test-uri              (str p/service-context id-test)]

      (is (= location-test test-uri))

      ;; admin should be able to see the callback
      (-> session-admin
          (request test-uri)
          (ltu/body->edn)
          (ltu/is-status 200)
          (ltu/is-operation-present "delete")
          ;(ltu/is-operation-absent "edit")
          ;(ltu/is-operation-present (:execute c/action-uri))
      )

      ;; user cannot directly see the callback
      (-> session-user
          (request test-uri)
          (ltu/body->edn)
          (ltu/is-status 403))

      ;; check contents and editing
      (let [reread-test-callback (-> session-admin
                                     (request test-uri)
                                     (ltu/body->edn)
                                     (ltu/is-status 200)
                                     :response
                                     :body)
            original-updated-timestamp (:updated reread-test-callback)]

        ;(is (= (ltu/strip-unwanted-attrs reread-test-callback)
        ;       (ltu/strip-unwanted-attrs (assoc create-test-callback :state "WAITING"))))

        ;; mark callback as failed
        (utils/callback-failed! id-test)
        (let [callback (-> session-admin
                           (request test-uri)
                           (ltu/body->edn)
                           (ltu/is-status 200)
                           (ltu/is-operation-absent (:execute c/action-uri))
                           :response
                           :body)]
          (is (= "FAILED" (:state callback)))
          (is (not= original-updated-timestamp (:updated callback))))

        ;; mark callback as succeeded
        (utils/callback-succeeded! id-test)
        (let [callback (-> session-admin
                           (request test-uri)
                           (ltu/body->edn)
                           (ltu/is-status 200)
                           (ltu/is-operation-absent (:execute c/action-uri))
                           :response
                           :body)]
          (is (= "SUCCEEDED" (:state callback)))
          (is (not= original-updated-timestamp (:updated callback)))))

      ;; search
      (-> session-admin
          (request base-uri
                   :request-method :put
                   :body (json/write-str {}))
          (ltu/body->edn)
          (ltu/is-count 1)
          (ltu/is-status 200))

      ;; delete
      (-> session-anon
          (request test-uri
                   :request-method :delete)
          (ltu/body->edn)
          (ltu/is-status 403))

      (-> session-user
          (request test-uri
                   :request-method :delete)
          (ltu/body->edn)
          (ltu/is-status 403))

      (-> session-admin
          (request test-uri
                   :request-method :delete)
          (ltu/body->edn)
          (ltu/is-status 200))

      ;; callback must be deleted
      (-> session-admin
          (request test-uri
                   :request-method :delete)
          (ltu/body->edn)
          (ltu/is-status 404)))))