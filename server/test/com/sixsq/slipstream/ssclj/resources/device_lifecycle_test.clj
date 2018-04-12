(ns com.sixsq.slipstream.ssclj.resources.device-lifecycle-test
  (:require
    [clojure.test :refer :all]
    [clojure.data.json :as json]
    [peridot.core :refer :all]
    [com.sixsq.slipstream.ssclj.app.params :as p]
    [com.sixsq.slipstream.ssclj.middleware.authn-info-header :refer [authn-info-header]]
    [com.sixsq.slipstream.ssclj.resources.device :as device]
    [com.sixsq.slipstream.ssclj.resources.lifecycle-test-utils :as ltu]
    [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
    [com.sixsq.slipstream.ssclj.resources.common.schema :as c]
    [com.sixsq.slipstream.ssclj.resources.example-resource.utils :as utils]))

(use-fixtures :each ltu/with-test-server-fixture)

(def base-uri (str p/service-context (u/de-camelcase device/resource-url)))

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
    (let [resource-name         "DeviceStatic"
          resource-url          (u/de-camelcase resource-name)
          create-test-callback  {:id                    (str resource-url "/device-resource")
                                 :resourceURI           base-uri
                                ;  :acl                   {:owner {:principal "ADMIN"
                                ;                                  :type      "ROLE"}
                                ;                          :rules [{:principal "ADMIN"
                                ;                                   :type      "ROLE"
                                ;                                   :right     "ALL"}]}
                                                                 ;{:principal "ANON"
                                                                  ;:type      "ROLE"
                                                                  ;:right     "MODIFY"}]}
                                 ;; sharing model fields
                                ;  :user_id              "user/1230958abdef"
                                  :deviceID            "1234567890abcdef"
                                  :isLeader          false
                                  :os                "Linux-4.4.0-116-generic-x86_64-with-debian-8.10"
                                  :arch              "x86_64"
                                  :cpuManufacturer   "Intel(R) Core(TM) i7-7700 CPU @ 3.60GHz"
                                  :physicalCores     4
                                  :logicalCores       8
                                  :cpuClockSpeed     "3.6000 GHz"
                                  :memory            32134.5078125
                                  :storage           309646.39453125
                                  :powerPlugged      true
                                  :networkingStandards "['lo', 'enp2s0', 'docker0', 'wlp3s0']"
                                  :ethernetAddress   "[snic(family=<AddressFamily.AF_INET: 2>, address='147.83.159.199', netmask='255.255.255.224', broadcast='147.83.159.223', ptp=None), snic(family=<AddressFamily.AF_INET6: 10>, address='fe80::16de:c6b7:3dc3:d11c%enp0s31f6', netmask='ffff:ffff:ffff:ffff::', broadcast=None, ptp=None), snic(family=<AddressFamily.AF_PACKET: 17>, address='4c:cc:6a:f5:a3:ea', netmask=None, broadcast='ff:ff:ff:ff:ff:ff', ptp=None)]"
                                  :wifiAddress       "[snic(family=<AddressFamily.AF_INET: 2>, address='192.168.4.71', netmask='255.255.255.0', broadcast='192.168.4.255', ptp=None), snic(family=<AddressFamily.AF_INET6: 10>, address='fe80::e7ec:a09a:36fe:5ad4%wlx001986d03ca6', netmask='ffff:ffff:ffff:ffff::', broadcast=None, ptp=None), snic(family=<AddressFamily.AF_PACKET: 17>, address='00:19:86:d0:3c:a6', netmask=None, broadcast='ff:ff:ff:ff:ff:ff', ptp=None)]"
                                  :myleaderID        "889345efdet" }
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