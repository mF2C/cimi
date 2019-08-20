(ns com.sixsq.slipstream.ssclj.resources.session-template-jwt-lifecycle-test
  (:require
    [clojure.data.json :as json]
    [clojure.test :refer [deftest is use-fixtures]]
    [com.sixsq.slipstream.ssclj.app.params :as p]
    [com.sixsq.slipstream.ssclj.middleware.authn-info-header :refer [authn-info-header]]
    [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
    [com.sixsq.slipstream.ssclj.resources.lifecycle-test-utils :as ltu]
    [com.sixsq.slipstream.ssclj.resources.session-template :as st]
    [com.sixsq.slipstream.ssclj.resources.session-template-jwt :as jwt]
    [peridot.core :refer :all]))


(use-fixtures :each ltu/with-test-server-fixture)


(def base-uri (str p/service-context (u/de-camelcase st/resource-name)))


(def session-template-base-uri (str p/service-context (u/de-camelcase st/resource-name)))


(def instance "test-instance")


(def session-template-jwt {:method      jwt/authn-method
                           :instance    instance
                           :name        "JWT Authentication"
                           :description "External Authentication with JWT"
                           :token       "provide-valid-authentication-token"
                           :acl         st/resource-acl})


(deftest lifecycle

  (let [app           (ltu/ring-app)
        session-json  (content-type (session app) "application/json")
        session-anon  (header session-json authn-info-header "unknown ANON")
        session-user  (header session-json authn-info-header "user USER ANON")
        session-admin (header session-json authn-info-header "root ADMIN USER ANON")]

    ;; only admin should be able to create a session template instance
    (doseq [session [session-user session-anon]]
      (-> session
          (request session-template-base-uri
                   :request-method :post
                   :body (json/write-str session-template-jwt))
          (ltu/body->edn)
          (ltu/is-status 403)))

    ;; anonymous query should succeed, only "internal" template should be visible
    (-> session-anon
        (request base-uri)
        (ltu/body->edn)
        (ltu/is-status 200)
        (ltu/is-count 1))

    ;; create the JWT session template
    (let [href         (-> session-admin
                           (request session-template-base-uri
                                    :request-method :post
                                    :body (json/write-str session-template-jwt))
                           (ltu/body->edn)
                           (ltu/is-status 201)
                           (ltu/location))

          template-url (str p/service-context href)]

      ;; verify that the session template exists and can be seen by anon
      (-> session-anon
          (request template-url)
          (ltu/body->edn)
          (ltu/is-status 200)
          (ltu/is-operation-absent "delete")
          (ltu/is-operation-absent "edit"))

      ;; anonymous query should succeed with "internal" and new template visible
      (-> session-anon
          (request base-uri)
          (ltu/body->edn)
          (ltu/is-status 200)
          (ltu/is-count 2))

      ;; check rights for admin
      (-> session-admin
          (request template-url)
          (ltu/body->edn)
          (ltu/is-status 200)
          (ltu/is-operation-present "delete")
          (ltu/is-operation-present "edit"))

      ;; only admin should be able to delete a session template
      (doseq [session [session-user session-anon]]
        (-> session
            (request template-url
                     :request-method :delete)
            (ltu/body->edn)
            (ltu/is-status 403)))

      ;; try editing the session template
      (let [new-name "updated name attribute"]

        ;; change the name of the template
        (-> session-admin
            (request template-url
                     :request-method :put
                     :body (json/write-str {:name new-name}))
            (ltu/body->edn)
            (ltu/is-status 200))

        ;; verify that the name has changed
        (let [{updated-name :name} (-> session-admin
                                       (request template-url)
                                       (ltu/body->edn)
                                       (ltu/is-status 200)
                                       :response
                                       :body)]
          (is (= updated-name new-name))))

      ;; admin should be able to delete session template
      (-> session-admin
          (request template-url
                   :request-method :delete)
          (ltu/body->edn)
          (ltu/is-status 200))

      ;; verify that the session template has disappeared
      (-> session-admin
          (request template-url)
          (ltu/body->edn)
          (ltu/is-status 404)))))

