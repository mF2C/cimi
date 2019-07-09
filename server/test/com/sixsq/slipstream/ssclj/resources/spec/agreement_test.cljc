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
                                            {
                                                :name   "gt2"
                                                :constraint "m2 > 0"
                                                :scope  "Operation1"
                                                :schedule   "hourly"
                                            }
                                    ]
                                }
                                }
        initial-assessment      {:first_execution timestamp
                                :last_execution timestamp}
        last        {:m0        {:key "m0"
                                :value 50.0
                                :datetime timestamp}
                    :m1         {:key "m1"
                                :value 51
                                :datetime timestamp}}
        guarantees {:gt0        {:first_execution   timestamp
                                :last_execution     timestamp
                                :last_values        last}
        }
        assessment  {           :first_execution    timestamp
                                :last_execution     timestamp
                                :guarantees         guarantees
        }]

    (is (s/valid? :cimi/agreement agreement-resource))
    (is (s/valid? :cimi/agreement (assoc agreement-resource :assessment initial-assessment)))
    (is (s/valid? :cimi/agreement (assoc agreement-resource :assessment assessment)))
    (is (not (s/valid? :cimi/agreement (assoc agreement-resource :bad-field "bla bla bla"))))))
