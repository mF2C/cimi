(ns com.sixsq.slipstream.ssclj.resources.spec.sla-template-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [are deftest is]]
            [com.sixsq.slipstream.ssclj.resources.common.schema :as schema]
            [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
            [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))

(deftest check-agreement-resource-schema
  (let [resource-name             "TemplateResource"
        resource-url              (u/de-camelcase resource-name)
        resource-uri              (str schema/slipstream-schema-uri resource-name)
        timestamp                 "2019-08-25T10:00:00.0Z"
        state                     "started"
        type                      "template"
        name                      "template-name"
        details-name              "{{.agreement-name}}"
        provider                  { :id         "provider-id"
                                    :name       "provider-name"}
        client                    { :id         "client-id"
                                    :name       "client-name"}
        valid-acl                 {:owner {:principal "ADMIN"
                                           :type      "ROLE"}
                                   :rules [{:principal "ADMIN"
                                            :type      "ROLE"
                                            :right     "MODIFY"}]}
        template-resource     {:id            (str resource-url "/template-resource")
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
                                    :guarantees [
                                            { 
                                                :name   "gt1"
                                                :constraint "m > 0"
                                            }
                                    ]
                                }
                                }]
    (is (s/valid? :cimi/sla-template template-resource))
    (is (not (s/valid? :cimi/sla-template (assoc template-resource :bad-field "bla bla bla"))))))
