(ns
  ^{:copyright "Copyright 2018, SixSq Sàrl"
    :license   "http://www.apache.org/licenses/LICENSE-2.0"}
  com.sixsq.slipstream.ssclj.resources.spec.example-resource-test
  (:require
    [clojure.spec.alpha :as s]
    [clojure.test :refer [are deftest is]]
    [com.sixsq.slipstream.ssclj.resources.example-resource :as t]))


(def valid-acl {:owner {:principal "ADMIN"
                        :type      "ROLE"}
                :rules [{:principal "ADMIN"
                         :type      "ROLE"
                         :right     "MODIFY"}]})


(deftest check-example-resource-schema
  (let [timestamp        "1964-08-25T10:00:00.0Z"
        example-resource {:id          (str t/resource-url "/test-example-resource")
                          :resourceURI t/resource-uri
                          :created     timestamp
                          :updated     timestamp
                          :acl         valid-acl
                          :action      "validate-something"
                          :state       "WAITING"
                          :resource    {:href "email/1230958abdef"}
                          :counter     10
                          :expires     timestamp
                          :data        {:some    "value"
                                        :another "value"}}]

    (is (s/valid? :cimi/example-resource example-resource))
    (is (s/valid? :cimi/example-resource (assoc example-resource :state "SUCCEEDED")))
    (is (s/valid? :cimi/example-resource (assoc example-resource :state "FAILED")))
    (is (not (s/valid? :cimi/example-resource (assoc example-resource :state "UNKNOWN"))))
    (doseq [attr #{:id :resourceURI :created :updated :acl :action}]
      (is (not (s/valid? :cimi/example-resource (dissoc example-resource attr)))))
    (doseq [attr #{:expires :data}]
      (is (s/valid? :cimi/example-resource (dissoc example-resource attr))))))
