(ns com.sixsq.slipstream.ssclj.resources.spec.sharing-model-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [are deftest is]]
            [com.sixsq.slipstream.ssclj.resources.common.schema :as schema]
            [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
            [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))


(deftest check-sharing-model-resource-schema
  (let [resource-name             "SharingModelResource"
        resource-url              (u/de-camelcase resource-name)
        resource-uri              (str schema/slipstream-schema-uri resource-name)
        timestamp                 "1964-08-25T10:00:00.0Z"
        valid-acl                 {:owner {:principal "ADMIN"
                                           :type      "ROLE"}
                                   :rules [{:principal "ADMIN"
                                            :type      "ROLE"
                                            :right     "MODIFY"}]}
        sharing-model-resource     {:id            (str resource-url "/sharing-model-resource")
                                  :resourceURI    resource-uri
                                  :created        timestamp
                                  :updated        timestamp
                                  :acl            valid-acl
                                  ;; sharing model fields
                                  ; :user_id              "user/1230958abdef"
                                  :max_apps             2
                                  :gps_allowed          false
                                  :max_cpu_usage        50
                                  :max_memory_usage     50
                                  :max_storage_usage    50
                                  :max_bandwidth_usage  50
                                  :battery_limit        50}]
    (is (s/valid? :cimi/sharing-model sharing-model-resource))
    (is (not (s/valid? :cimi/sharing-model (assoc sharing-model-resource :bad-field "bla bla bla"))))))
