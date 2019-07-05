(ns com.sixsq.slipstream.ssclj.resources.spec.service-container-metric-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [are deftest is]]
            [com.sixsq.slipstream.ssclj.resources.common.schema :as schema]
            [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
            [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))

(deftest check-service-container-metric-resource-schema
  (let [resource-name             "service-container-metric"
        resource-url              (u/de-camelcase resource-name)
        resource-uri              (str schema/slipstream-schema-uri resource-name)
        timestamp                 "2019-08-25T10:00:00.0Z"
        valid-acl                 {:owner {:principal "ADMIN"
                                           :type      "ROLE"}
                                   :rules [{:principal "ADMIN"
                                            :type      "ROLE"
                                            :right     "MODIFY"}]}
        resource                {:id            (str resource-url "/service-container-metric-resource")
                                :resourceURI    resource-uri
                                :created        timestamp
                                :updated        timestamp
                                :acl            valid-acl
                                :device_id {:href "device/d-id"}
                                :container_id "this-is-a-container-id"
								:start_time  "2019-08-25T10:00:00.0Z"
                                }]
    (is (s/valid? :cimi/service-container-metric resource))
    (is (s/valid? :cimi/service-container-metric (assoc resource :stop_time "2019-08-26T10:00:00.0Z")))
    (is (not (s/valid? :cimi/service-container-metric (assoc resource :bad-field "bla bla bla"))))))
