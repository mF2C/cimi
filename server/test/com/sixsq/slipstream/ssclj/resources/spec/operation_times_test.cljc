(ns com.sixsq.slipstream.ssclj.resources.spec.operation-times-test
  (:require [clojure.test :refer [deftest are is]]
            [clojure.spec.alpha :as s]
            [com.sixsq.slipstream.ssclj.resources.common.schema :as schema]
            [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
            [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))

(deftest check-operation-times-resource-schema
  (let [resource-name             "OperationTimeResource"
        resource-url              (u/de-camelcase resource-name)
        resource-uri              (str schema/slipstream-schema-uri resource-name)
        timestamp                 "1964-08-25T10:00:00.0Z"
        valid-acl                 {:owner {:principal "ADMIN"
                                           :type      "ROLE"}
                                   :rules [{:principal "ADMIN"
                                            :type      "ROLE"
                                            :right     "MODIFY"}]}
        resource                {:id            (str resource-url "/operation-times-resource")
                                :resourceURI    resource-uri
                                :created        timestamp
                                :updated        timestamp
                                :acl            valid-acl
                                :service_instance_id {:href "service-instance/si-id"}
                                :operation      "dijkstra"
                                :datetime       timestamp
                                :execution_time 99.9
                                }]
    (is (s/valid? :cimi/operation-times resource))
    (is (not (s/valid? :cimi/operation-times (assoc resource :bad-field "bla bla bla"))))))
