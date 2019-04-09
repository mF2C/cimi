(ns com.sixsq.slipstream.ssclj.resources.device-dynamic-lifecycle-test
  (:require
    [clojure.data.json :as json]
    [clojure.test :refer :all]
    [com.sixsq.slipstream.ssclj.app.params :as p]
    [com.sixsq.slipstream.ssclj.middleware.authn-info-header :refer [authn-info-header]]
    [com.sixsq.slipstream.ssclj.resources.common.schema :as c]
    [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
    [com.sixsq.slipstream.ssclj.resources.device-dynamic :as device-dynamic]
    [com.sixsq.slipstream.ssclj.resources.example-resource.utils :as utils]
    [com.sixsq.slipstream.ssclj.resources.lifecycle-test-utils :as ltu]
    [peridot.core :refer :all]))

(use-fixtures :each ltu/with-test-server-fixture)

(def base-uri (str p/service-context (u/de-camelcase device-dynamic/resource-url)))

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
    (let [resource-name         "DeviceDynamic"
          resource-url          (u/de-camelcase resource-name)
          create-test-callback  {:id                    (str resource-url "/device-dynamic-resource")
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
                                  :device                                {:href "device/142165441eewe"}
                                ;   :isLeader                              false
                                  :ramFree                               5225.359375
                                  :ramFreePercent                        66.4
                                  :storageFree                           211712.4765625
                                  :storageFreePercent                    95.1
                                  :cpuFreePercent                        50.4
                                  :powerRemainingStatus                  "97.42316050915865"
                                  :powerRemainingStatusSeconds           "10557"
                                  :powerPlugged                           true
                                  :ethernetAddress                       "[snic(family=<AddressFamily.AF_PACKET: 17>, address='50:9a:4c:cf:f4:b9', netmask=None, broadcast='ff:ff:ff:ff:ff:ff', ptp=None)]"
                                  :wifiAddress                           "[snic(family=<AddressFamily.AF_INET: 2>, address='10.192.167.20', netmask='255.255.0.0', broadcast='10.192.255.255', ptp=None), snic(family=<AddressFamily.AF_INET6: 10>, address='fe80::fe39:ed60:dff6:db85%wlp3s0', netmask='ffff:ffff:ffff:ffff::', broadcast=None, ptp=None), snic(family=<AddressFamily.AF_PACKET: 17>, address='d4:6a:6a:9a:6b:87', netmask=None, broadcast='ff:ff:ff:ff:ff:ff', ptp=None)]"
                                  :ethernetThroughputInfo                ["0", "0", "0", "0", "0", "0", "0", "0"]
                                  :wifiThroughputInfo                    ["21689997", "950419307", "150482", "663270", "0", "0", "0", "0"]
                                  ;:sensorType                            ["[\"temperature\"]", "[\"humidity\"]"]
                                  ;:sensorModel                           ["DHT22"]
                                  ;:sensorConnection                      ["{\"baudRate\": 5600, \"gpioPin\": 23}", "{\"baudRate\": 5600}"]
                                  :actuatorInfo                          "It has Ambulance, Firetruck, Sirene, Traffic light"
                                  :sensors                               [{:sensorType "mytype", :sensorModel "mymodel", :sensorConnection "myconn"}]}
                                  ;:status                                "connected"}
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
