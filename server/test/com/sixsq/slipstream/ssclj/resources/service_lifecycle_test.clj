(ns
  ^{:copyright "Copyright 2018, SixSq Sàrl"
    :license   "http://www.apache.org/licenses/LICENSE-2.0"}
  com.sixsq.slipstream.ssclj.resources.service-lifecycle-test
  (:require
    [clojure.data.json :as json]
    [clojure.test :refer :all]
    [clj-http.client :as http]
    [com.sixsq.slipstream.ssclj.app.params :as p]
    [com.sixsq.slipstream.ssclj.middleware.authn-info-header :refer [authn-info-header]]
    [com.sixsq.slipstream.ssclj.resources.common.schema :as c]
    [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
    [com.sixsq.slipstream.ssclj.resources.example-resource.utils :as utils]
    [com.sixsq.slipstream.ssclj.resources.lifecycle-test-utils :as ltu]
    [com.sixsq.slipstream.ssclj.resources.service :as service]
    [com.sixsq.slipstream.ssclj.resources.service :as callback]
    [peridot.core :refer :all]))

(use-fixtures :each ltu/with-test-server-fixture)

(def base-uri (str p/service-context (u/de-camelcase service/resource-url)))

(def valid-acl {:owner {:principal "ADMIN"
                        :type      "ROLE"}
                :rules [{:principal "ADMIN"
                         :type      "ROLE"
                         :right     "MODIFY"}]})

(def resource-body {:id           (str callback/resource-url "/example-service")
                    :resourceURI  callback/resource-uri
                    :acl          valid-acl
                    :name         "name_test"
                    :exec         "exec_name_test"
                    :exec_type    "docker"
                    :exec_ports   [8080]
                    :agent_type   "normal"
                    :category     0
                    :cpu_arch     "x86-64"
                    :os           "linux"
                    :memory_min   0
                    :storage_min  0
                    :disk         0
                    :req_resource ["req_resource_test"]
                    :opt_resource ["opt_resource_test"]
                    })

(deftest lifecycle
    (with-redefs [http/post (fn [_ _]
        {
        :body "{\"service\": {
                    \"resourceURI\":  \"callback/resource-uri\",
                    \"name\":         \"name_test\",
                    \"exec\":         \"exec_name_test\",
                    \"exec_type\":    \"docker\",
                    \"exec_ports\":   [8080],
                    \"agent_type\":   \"normal\",
                    \"category\":     0,
                    \"cpu_arch\":     \"x86-64\",
                    \"os\":           \"linux\",
                    \"memory_min\":   0,
                    \"storage_min\":  0,
                    \"disk\":         0,
                    \"req_resource\": [\"req_resource_test\"],
                    \"opt_resource\": [\"opt_resource_test\"]}}"
        })]

        (let [session (-> (ltu/ring-app)
                          session
                          (content-type "application/json"))
              session-admin (header session authn-info-header "root ADMIN USER ANON")
              session-user (header session authn-info-header "jane USER ANON")
              session-anon (header session authn-info-header "unknown ANON")]

             (-> session-admin
                 (request base-uri)
                 (ltu/body->edn)
                 (ltu/is-status 200)
                 (ltu/is-count zero?)
                 (ltu/is-operation-present "add")
                 (ltu/is-operation-absent "delete")
                 (ltu/is-operation-absent "edit"))

             ; user collection query should succeed
             (-> session-user
                 (request base-uri)
                 (ltu/body->edn)
                 (ltu/is-status 200))

             ;; anonymous collection query should not succeed
             (-> session-anon
                 (request base-uri)
                 (ltu/body->edn)
                 (ltu/is-status 403))

             ;; create a callback as an admin
             (let [create-test-callback {:id           (str callback/resource-url "/example-service")
                                         :resourceURI  callback/resource-uri
                                         :acl          valid-acl
                                         :name         "name_test"
                                         :exec         "exec_name_test"
                                         :exec_type    "docker"
                                         :exec_ports   [8080]
                                         :agent_type   "normal"
                                         :num_agents   1
                                         :cpu_arch     "x86-64"
                                         :os           "linux"
                                         :memory_min   0
                                         :storage_min  0
                                         :disk         0
                                         :req_resource ["req_resource_test"]
                                         :opt_resource ["opt_resource_test"]
                                         :category     0
                                        ;  :template_id  {:href "sla_template/id"}
                                         }

                   resp-test (-> session-admin
                                 (request base-uri
                                          :request-method :post
                                          :body (json/write-str create-test-callback))
                                 (ltu/body->edn)
                                 (ltu/is-status 201))

                   id-test (get-in resp-test [:response :body :resource-id])

                   location-test (str p/service-context (-> resp-test ltu/location))

                   test-uri (str p/service-context id-test)]

                  (is (= location-test test-uri))

                  ;; admin should be able to see the callback
                  (-> session-admin
                      (request test-uri)
                      (ltu/body->edn)
                      (ltu/is-status 200)
                      (ltu/is-operation-present "delete")
                      ; (ltu/is-operation-absent "edit")
                      ; (ltu/is-operation-present (:execute c/action-uri)))
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
                            ;(is (= "FAILED" (:state callback)))
                            ;(is (not= original-updated-timestamp (:updated callback)))
                            )

                       ;; mark callback as succeeded
                       (utils/callback-succeeded! id-test)
                       (let [callback (-> session-admin
                                          (request test-uri)
                                          (ltu/body->edn)
                                          (ltu/is-status 200)
                                          (ltu/is-operation-absent (:execute c/action-uri))
                                          :response
                                          :body)]
                            ;(is (= "SUCCEEDED" (:state callback)))
                            ;(is (not= original-updated-timestamp (:updated callback)))
                            )
                       )

                  ;; put
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
                      (ltu/is-status 404))))))


(deftest bad-methods
        (let [resource-uri (str p/service-context (u/new-resource-id callback/resource-name))]
             (ltu/verify-405-status [[base-uri :options]
                                     [base-uri :delete]
                                     [resource-uri :options]
                                     [resource-uri :put]
                                     [resource-uri :post]])))
