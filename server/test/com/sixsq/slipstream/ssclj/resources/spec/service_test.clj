(ns com.sixsq.slipstream.ssclj.resources.spec.service-test
    (:require
      [clojure.test :refer [deftest are is]]
      [clojure.spec.alpha :as s]
      [com.sixsq.slipstream.ssclj.resources.service :as t]))

(def valid-acl {:owner {:principal "ADMIN"
                        :type      "ROLE"}
                :rules [{:principal "ADMIN"
                         :type      "ROLE"
                         :right     "MODIFY"}]})

(deftest check-service-schema
         (let [timestamp "1964-08-25T10:00:00.0Z"
               service {:id          (str t/resource-url "/example-service")
                        :resourceURI t/resource-uri
                        :created     timestamp
                        :updated     timestamp
                        :acl         valid-acl
                        :category    {:cpu          "low"
                                      :memory       "medium"
                                      :storage      "low"
                                      :inclinometer true
                                      :temperature  false
                                      :jammer       true
                                      :location     true}
                        :exec         "hello-world"
                        :exec_type    "docker"}]

              (is (s/valid? :cimi/service service))))