(ns com.sixsq.slipstream.ssclj.resources.spec.service-operation-report-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [are deftest is]]
            [com.sixsq.slipstream.ssclj.resources.common.schema :as schema]
            [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
            [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))

(deftest check-service-operation-report-resource-schema
  (let [resource-name "OperationTimeResource"
        resource-url  (u/de-camelcase resource-name)
        resource-uri  (str schema/slipstream-schema-uri resource-name)
        timestamp     "1964-08-25T10:00:00.0Z"
        valid-acl     {:owner {:principal "ADMIN"
                               :type      "ROLE"}
                       :rules [{:principal "ADMIN"
                                :type      "ROLE"
                                :right     "MODIFY"}]}
        resource      {:id                        (str resource-url "/service-operation-report-resource")
                       :resourceURI               resource-uri
                       :created                   timestamp
                       :updated                   timestamp
                       :acl                       valid-acl
                       :requesting_application_id {:href "service-instance/si-id"}
                       :compute_node_id           "compute_node_id"
                       :operation_name            "dijkstra"
                       :operation_id              "operation_id"
                       :start_time                "1964-08-25T10:00:00.0Z"
                       :expected_end_time         "1964-08-25T10:00:00.0Z"
                       :execution_length          99.9
                       :result                    "result "
                       }]
    (is (s/valid? :cimi/service-operation-report resource))
    (is (not (s/valid? :cimi/service-operation-report (assoc resource :bad-field "bla bla bla"))))))
