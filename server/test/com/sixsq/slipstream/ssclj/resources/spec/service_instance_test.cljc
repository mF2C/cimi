(ns com.sixsq.slipstream.ssclj.resources.spec.service-instance-test
  (:require [clojure.test :refer [deftest are is]]
            [clojure.spec.alpha :as s]
            [com.sixsq.slipstream.ssclj.resources.common.schema :as schema]
            [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
            [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))


(deftest check-service-instance-resource-schema
  (let [resource-name             "ServiceInstanceResource"
        resource-url              (u/de-camelcase resource-name)
        resource-uri              (str schema/slipstream-schema-uri resource-name)
        timestamp                 "1964-08-25T10:00:00.0Z"
        valid-acl                 {:owner {:principal "ADMIN"
                                           :type      "ROLE"}
                                   :rules [{:principal "ADMIN"
                                            :type      "ROLE"
                                            :right     "MODIFY"}]}
        service-instance-resource  {:id            (str resource-url "/service-instance-resource")
                                  :resourceURI    resource-uri
                                  :created        timestamp
                                  :updated        timestamp
                                  :acl            valid-acl
                                  ;; service instance fields
                                  :service_id     {:href "service/1230958abdef"}
                                  :agreement_id   {:href "agreement/1230958abdef"}
                                  :user_id        {:href "user/1230958abdef"}
                                  :status         "running"
                                  :agents         [{:agent {:href "agent/1230958abdef1"}, :port 31111, :num_cpus 1
                                                    :status "running", :container_id "asdasd-asdasda"}
                                                   {:agent {:href "agent/1230958abdef2"}, :port 31111, :num_cpus 2
                                                    :status "running", :container_id "asdasd-hasdagsa"}]}]
    (is (s/valid? :cimi/service-instance service-instance-resource))
    (is (not (s/valid? :cimi/service-instance (assoc service-instance-resource :bad-field "bla bla bla"))))))
