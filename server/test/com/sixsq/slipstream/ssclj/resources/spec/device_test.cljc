(ns com.sixsq.slipstream.ssclj.resources.spec.device-test
  (:require [clojure.test :refer [deftest are is]]
            [clojure.spec.alpha :as s]
            [com.sixsq.slipstream.ssclj.resources.common.schema :as schema]
            [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
            [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))


(deftest check-device-resource-schema
  (let [resource-name             "DeviceStatic"
        resource-url              (u/de-camelcase resource-name)
        resource-uri              (str schema/slipstream-schema-uri resource-name)
        timestamp                 "1964-08-25T10:00:00.0Z"
        valid-acl                 {:owner {:principal "ADMIN"
                                           :type      "ROLE"}
                                   :rules [{:principal "ADMIN"
                                            :type      "ROLE"
                                            :right     "MODIFY"}]}
        device-resource     {:deviceID            "1234567890abcdef"
                                  :created_on        timestamp
                                  :isLeader          false
                                  :os                "Linux-4.4.0-116-generic-x86_64-with-debian-8.10"
                                  :arch              "x86_64"
                                  :cpuManufacturer   "Intel(R) Core(TM) i7-7700 CPU @ 3.60GHz"
                                  :physicalCores     4
                                  :logicalCore       8 
                                  :cpuClockSpeed     "3.6000 GHz"
                                  :memory            32134.5078125
                                  :storage           309646.39453125
                                  :powerPlugged      true
                                  :networkingStandards "['lo', 'enp2s0', 'docker0', 'wlp3s0']" 
                                  :ethernetAddress   "[snic(family=<AddressFamily.AF_INET: 2>, address='147.83.159.199', netmask='255.255.255.224', broadcast='147.83.159.223', ptp=None), snic(family=<AddressFamily.AF_INET6: 10>, address='fe80::16de:c6b7:3dc3:d11c%enp0s31f6', netmask='ffff:ffff:ffff:ffff::', broadcast=None, ptp=None), snic(family=<AddressFamily.AF_PACKET: 17>, address='4c:cc:6a:f5:a3:ea', netmask=None, broadcast='ff:ff:ff:ff:ff:ff', ptp=None)]"
                                  :wifiAddress       "[snic(family=<AddressFamily.AF_INET: 2>, address='192.168.4.71', netmask='255.255.255.0', broadcast='192.168.4.255', ptp=None), snic(family=<AddressFamily.AF_INET6: 10>, address='fe80::e7ec:a09a:36fe:5ad4%wlx001986d03ca6', netmask='ffff:ffff:ffff:ffff::', broadcast=None, ptp=None), snic(family=<AddressFamily.AF_PACKET: 17>, address='00:19:86:d0:3c:a6', netmask=None, broadcast='ff:ff:ff:ff:ff:ff', ptp=None)]"}]
    (is (s/valid? :cimi/device device-resource))
    (is (not (s/valid? :cimi/device (assoc device-resource :bad-field "you need to check...etc...etc.."))))))
