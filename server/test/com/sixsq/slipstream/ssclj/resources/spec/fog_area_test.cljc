(ns com.sixsq.slipstream.ssclj.resources.spec.fog-area-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [are deftest is]]
            [com.sixsq.slipstream.ssclj.resources.common.schema :as schema]
            [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
            [com.sixsq.slipstream.ssclj.resources.device-dynamic :as t]
            [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))


(deftest check-device-resource-schema
  (let [resource-name             "FogArea"
        resource-url              (u/de-camelcase resource-name)
        resource-uri              (str schema/slipstream-schema-uri resource-name)
        timestamp                 "1964-08-25T10:00:00.0Z"
        valid-acl                 {:owner {:principal "ADMIN"
                                           :type      "ROLE"}
                                   :rules [{:principal "ADMIN"
                                            :type      "ROLE"
                                            :right     "MODIFY"}]}
        fog-area-resource     {:id          (str t/resource-url "/fog-area-resource")
                          :resourceURI t/resource-uri
                          :created     timestamp
                          :updated     timestamp
                          :acl         valid-acl
                                  :leaderDevice                               {:href "device/887766345qws"}
                                  :numDevices                                 12
                                  :ramTotal                                   5225.359375
                                  :ramMax                                     66.4
                                  :ramMin                                     211712.4765625
                                  :storageTotal                               95.1
                                  :storageMax                                 50.4
                                  :storageMin                                 50.4
                                  :avgProcessingCapacityPercent               78.0
                                  :cpuMaxPercent                              92.8
                                  :cpuMinPercent                              68.8
                                  :avgPhysicalCores                           4
                                  :physicalCoresMax                           8
                                  :physicalCoresMin                           2
                                  :avgLogicalCores                            6
                                  :logicalCoresMax                            8
                                  :logicalCoresMin                            4
                                  :powerRemainingMax                       "Device has unlimited power source"
                                  :powerRemainingMin                       "97.2"}]
    (is (s/valid? :cimi/fog-area fog-area-resource))
    (is (not (s/valid? :cimi/fog-area (assoc fog-area-resource :bad-field "you need to check...etc...etc.."))))))
