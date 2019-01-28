(ns com.sixsq.slipstream.ssclj.resources.spec.service-test
    (:require
      [clojure.spec.alpha :as s]
      [clojure.test :refer [are deftest is]]
      [com.sixsq.slipstream.ssclj.resources.service :as t]))

(def valid-acl {:owner {:principal "ADMIN"
                        :type      "ROLE"}
                :rules [{:principal "ADMIN"
                         :type      "ROLE"
                         :right     "MODIFY"}]})

(deftest check-service-schema
         (let [timestamp "1964-08-25T10:00:00.0Z"
               service {:id           (str t/resource-url "/example-service")
                        :resourceURI  t/resource-uri
                        :created      timestamp
                        :updated      timestamp
                        :acl          valid-acl
                        :name         "name_test"
                        :exec         "exec_name_test"
                        :exec_type    "docker"
                        :num_agents   1
                        :exec_ports   [8080]
                        :agent_type   "normal"
                        :cpu_arch     "x86-64"
                        :os           "linux"
                        :memory_min   0
                        :storage_min  0
                        :disk         0
                        :req_resource ["req_resource_test"]
                        :opt_resource ["opt_resource_test"]
                        :category     0
;                        :template_id  {:href "sla_template/id"}
                        }]

              (is (s/valid? :cimi/service service))))