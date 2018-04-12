(ns com.sixsq.slipstream.ssclj.resources.spec.sla-violation-test
  (:require [clojure.test :refer [deftest are is]]
            [clojure.spec.alpha :as s]
            [com.sixsq.slipstream.ssclj.resources.common.schema :as schema]
            [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
            [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))

(deftest check-sla-violation-resource-schema
  (let [resource-name             "SlaViolationResource"
        resource-url              (u/de-camelcase resource-name)
        resource-uri              (str schema/slipstream-schema-uri resource-name)
        timestamp                 "1964-08-25T10:00:00.0Z"
        valid-acl                 {:owner {:principal "ADMIN"
                                           :type      "ROLE"}
                                   :rules [{:principal "ADMIN"
                                            :type      "ROLE"
                                            :right     "MODIFY"}]}
        sla-violation-resource     {:id            (str resource-url "/sla-violation-resource")
                                :resourceURI    resource-uri
                                :created        timestamp
                                :updated        timestamp
                                :acl            valid-acl
                                :agreement_id   {:href "agreement/agreement-id"}
                                :guarantee      "gt01"
                                :datetime       timestamp
                                }]
    (is (s/valid? :cimi/sla-violation sla-violation-resource))
    (is (not (s/valid? :cimi/sla-violation (assoc sla-violation-resource :bad-field "bla bla bla"))))))
