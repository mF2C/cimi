(ns com.sixsq.slipstream.ssclj.resources.spec.agreement-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [are deftest is]]
            [com.sixsq.slipstream.ssclj.resources.common.schema :as schema]
            [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
            [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))

(deftest check-agreement-resource-schema
  (let [resource-name             "AgreementResource"
        resource-url              (u/de-camelcase resource-name)
        resource-uri              (str schema/slipstream-schema-uri resource-name)
        timestamp                 "1964-08-25T10:00:00.0Z"
        state                     "started"
        type                      "agreement"
        name                      "agreement-name"
        provider                  { :id         "provider-id"
                                    :name       "provider-name"}
        client                    { :id         "client-id"
                                    :name       "client-name"}
        valid-acl                 {:owner {:principal "ADMIN"
                                           :type      "ROLE"}
                                   :rules [{:principal "ADMIN"
                                            :type      "ROLE"
                                            :right     "MODIFY"}]}
        aggregation {
            :type   "average"
            :window 600
        }
        variable    {
            :name   "m"
            :metric "_m"
            :aggregation aggregation
        }
        variables   [
            variable
        ]
        agreement-resource     {:id            (str resource-url "/agreement-resource")
                                :resourceURI    resource-uri
                                :created        timestamp
                                :updated        timestamp
                                :acl            valid-acl
                                :state          state
                                :details        {
                                    :type       type
                                    :name       name
                                    :provider   provider
                                    :client     client
                                    :creation   timestamp
                                    :expiration timestamp
                                    :variables [
                                        {
                                            :name "m"
                                            :metric "_m"
                                            :aggregation {
                                                :type "average"
                                                :window 600
                                            }
                                        }
                                    ]
                                    :guarantees [
                                            { 
                                                :name   "gt1"
                                                :constraint "m > 0"
                                            }
                                    ]
                                }
                                }]

    (is (s/valid? :cimi/agreement agreement-resource))
    (is (not (s/valid? :cimi/agreement (assoc agreement-resource :bad-field "bla bla bla"))))))
