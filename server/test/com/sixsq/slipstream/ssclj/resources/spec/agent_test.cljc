(ns com.sixsq.slipstream.ssclj.resources.spec.agent-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [are deftest is]]
            [com.sixsq.slipstream.ssclj.resources.common.schema :as schema]
            [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
            [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))

(deftest check-agent-resource-schema
  (let [
         resource-name            "Agent"
         resource-url              (u/de-camelcase resource-name)
         resource-uri              (str schema/slipstream-schema-uri resource-name)

         timestamp                 "1964-08-25T10:00:00.0Z"
         valid-acl                 {:owner {:principal "ADMIN"
                                           :type      "ROLE"}
                                   :rules [{:principal "ADMIN"
                                            :type      "ROLE"
                                            :right     "MODIFY"}]}

         agent-resource {
                          :id                 (str t/resource-url "/agent-resource")
                          :resourceURI        t/resource-uri
                          :created            timestamp
                          :updated            timestamp
                          :acl                valid-acl

                          :device_id          "device/id12345678"
                          :device_ip          "127.0.0.1"
                          :leader_id          "somelongstringhere"
                          :leader_ip          "127.0.0.1"
                          :authenticated      true
                          :connected          true
                          :isLeader           false
                          :backup_ip          "0.0.0.0"
                          :childrenIPs        "[]"
                          }
         ]
    (is (s/valid? :cimi/agent agent-resource))
    (is (not (s/valid? :cimi/agent (assoc agent-resource :bad-field "you need to check...etc...etc.."))))
    )
  )