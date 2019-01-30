(ns com.sixsq.slipstream.ssclj.resources.service-instance-lifecycle-test
  (:require
    [clojure.data.json :as json]
    [clojure.test :refer :all]
    [com.sixsq.slipstream.ssclj.app.params :as p]
    [com.sixsq.slipstream.ssclj.middleware.authn-info-header :refer [authn-info-header]]
    [com.sixsq.slipstream.ssclj.resources.common.schema :as c]
    [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
    [com.sixsq.slipstream.ssclj.resources.example-resource.utils :as utils]
    [com.sixsq.slipstream.ssclj.resources.lifecycle-test-utils :as ltu]
    [com.sixsq.slipstream.ssclj.resources.service-instance :as service-instance]
    [peridot.core :refer :all]))

(use-fixtures :each ltu/with-test-server-fixture)

(def base-uri (str p/service-context (u/de-camelcase service-instance/resource-url)))

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
    (let [resource-name         "ServiceInstanceResource"
          resource-url          (u/de-camelcase resource-name)
          create-test-callback  {:id              (str resource-url "/service-instance-resource")
                                 :resourceURI     base-uri
                                 ;; service instance fields
                                 :user         	  "user/testuser"
								 :device_id			"device/id12345678"
								 :device_ip			"192.168.242.5"
								 :parent_device_id	"device/id024681214"
								 :parent_device_ip	"192.168.242.15"
								 :service_type		"docker"
                                 :service      	  "service/71230958abdef9"
                                 :agreement    	  "agreement/a1230958abdef0"
                                 :status          "running"
                                 :agents          [{:agent {:href "agent/1230958abdef1"}, :ports [31111], :num_cpus 1
                                                    :status "running", :container_id "asdasd-asdasda", :allow true,
                                                    :url "192.168.1.31", :master_compss true :app_type "docker"}
                                                   {:agent {:href "agent/1230958abdef2"}, :ports [31111], :num_cpus 2
                                                    :status "running", :container_id "asdasd-hasdagsa", :allow false,
                                                    :url "192.168.1.32", :master_compss false :app_type "docker"}]}
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
