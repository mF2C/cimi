(ns com.sixsq.slipstream.ssclj.resources.spec.qos-model-test
    (:require
      [clojure.spec.alpha :as s]
      [clojure.test :refer [are deftest is]]
      [com.sixsq.slipstream.ssclj.resources.qos-model :as t]))

(def valid-acl {:owner {:principal "ADMIN"
                        :type      "ROLE"}
                :rules [{:principal "ADMIN"
                         :type      "ROLE"
                         :right     "MODIFY"}]})

(deftest check-qos-model-schema
         (let [timestamp "1964-08-25T10:00:00.0Z"
               qos-model {:id                   (str t/resource-url "/example-qos-model")
                        :resourceURI            t/resource-uri
                        :created                timestamp
                        :updated                timestamp
                        :acl                    valid-acl
                        :service                {:href "service/service-id"}
                        :agreement              {:href "agreement/agreement-id"}
                        :agents                 [{:href "agent/agent-id-1"}, {:href "agent/agent-id-2"}]
                        :config                 "test"
                        :num_service_instances  2
                        :num_service_failures   1
                        :state                  [1.0, 0.0]
                        :next_state             [0.0, 1.0]
                        }]

              (is (s/valid? :cimi/qos-model qos-model))))