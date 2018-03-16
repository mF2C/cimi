(ns com.sixsq.slipstream.ssclj.resources.spec.profile-test
  (:require [clojure.test :refer [deftest are is]]
            [clojure.spec.alpha :as s]
            [com.sixsq.slipstream.ssclj.resources.common.schema :as schema]
            [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
            [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))


(deftest check-profile-resource-schema
  (let [resource-name             "ProfileResource"
        resource-url              (u/de-camelcase resource-name)
        resource-uri              (str schema/slipstream-schema-uri resource-name)
        timestamp                 "1964-08-25T10:00:00.0Z"
        valid-acl                 {:owner {:principal "ADMIN"
                                           :type      "ROLE"}
                                   :rules [{:principal "ADMIN"
                                            :type      "ROLE"
                                            :right     "MODIFY"}]}
        profile-resource     {:id            (str resource-url "/profile-resource")
                              :resourceURI    resource-uri
                              :created        timestamp
                              :updated        timestamp
                              :acl            valid-acl
                              ;; profiling fields
                              :user_id                "user/1230958abdef"
                              :id_key                 "asdasdasdasdasd"
                              :email                  "email@gmail.com"
                              :service_consumer       false
                              :resource_contributor   false}]
    (is (s/valid? :cimi/profile profile-resource))
    (is (not (s/valid? :cimi/profile (assoc profile-resource :bad-field "bla bla bla"))))))
