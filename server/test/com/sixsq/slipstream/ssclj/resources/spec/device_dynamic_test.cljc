(ns com.sixsq.slipstream.ssclj.resources.spec.device-dynamic-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [are deftest is]]
            [com.sixsq.slipstream.ssclj.resources.common.schema :as schema]
            [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
            [com.sixsq.slipstream.ssclj.resources.device-dynamic :as t]
            [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))


(deftest check-device-resource-schema
  (let [resource-name             "DeviceDynamic"
        resource-url              (u/de-camelcase resource-name)
        resource-uri              (str schema/slipstream-schema-uri resource-name)
        timestamp                 "1964-08-25T10:00:00.0Z"
        valid-acl                 {:owner {:principal "ADMIN"
                                           :type      "ROLE"}
                                   :rules [{:principal "ADMIN"
                                            :type      "ROLE"
                                            :right     "MODIFY"}]}
        device-dynamic-resource     {:id          (str t/resource-url "/device-dynamic-resource")
                          :resourceURI t/resource-uri
                          :created     timestamp
                          :updated     timestamp
                          :acl         valid-acl
                                  :device                                {:href "device/142165441eewe"}
                                  ; :isLeader                              false
                                  :ramFree                               5225.359375
                                  :ramFreePercent                        66.4
                                  :storageFree                           211712.4765625
                                  :storageFreePercent                    95.1
                                  :cpuFreePercent                        50.4
                                  :powerRemainingStatus                  "97.42316050915865"
                                  :powerRemainingStatusSeconds           "10557"
                                  :ethernetAddress                       "[snic(family=<AddressFamily.AF_PACKET: 17>, address='50:9a:4c:cf:f4:b9', netmask=None, broadcast='ff:ff:ff:ff:ff:ff', ptp=None)]"
                                  :wifiAddress                           "[snic(family=<AddressFamily.AF_INET: 2>, address='10.192.167.20', netmask='255.255.0.0', broadcast='10.192.255.255', ptp=None), snic(family=<AddressFamily.AF_INET6: 10>, address='fe80::fe39:ed60:dff6:db85%wlp3s0', netmask='ffff:ffff:ffff:ffff::', broadcast=None, ptp=None), snic(family=<AddressFamily.AF_PACKET: 17>, address='d4:6a:6a:9a:6b:87', netmask=None, broadcast='ff:ff:ff:ff:ff:ff', ptp=None)]"
                                  :ethernetThroughputInfo                ["0", "0", "0", "0", "0", "0", "0", "0"]
                                  :wifiThroughputInfo                    ["21689997", "950419307", "150482", "663270", "0", "0", "0", "0"]
                                  ;:sensorType                            ["[\"temperature\"]", "[\"humidity\"]"]
                                  ;:sensorModel                           ["DHT22"]
                                  ;:sensorConnection                      ["{\"baudRate\": 5600, \"gpioPin\": 23}", "{\"baudRate\": 5600}"]
                                  :sensors                               [{:sensorType "mytype", :sensorModel "mymodel", :sensorConnection "myconn"}]
                                  ;:myLeaderID                            {:href "device/889345efdet"}}]
                                  :powerPlugged                           true
                                  :actuatorInfo                          "It has Ambulance, Firetruck, Sirene, Traffic light"]
    (is (s/valid? :cimi/device-dynamic device-dynamic-resource))
    (is (not (s/valid? :cimi/device-dynamic (assoc device-dynamic-resource :bad-field "you need to check...etc...etc.."))))))
