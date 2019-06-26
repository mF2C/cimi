(ns com.sixsq.slipstream.ssclj.resources.spec.identification-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [are deftest is]]
            [com.sixsq.slipstream.ssclj.resources.common.schema :as schema]
            [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
            [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))


(deftest check-identification-resource-schema
  (let [resource-name             "IdentificationResource"
        resource-url              (u/de-camelcase resource-name)
        resource-uri              (str schema/slipstream-schema-uri resource-name)
        timestamp                 "1964-08-25T10:00:00.0Z"
        valid-acl                 {:owner {:principal "ADMIN"
                                           :type      "ROLE"}
                                   :rules [{:principal "ADMIN"
                                            :type      "ROLE"
                                            :right     "MODIFY"}]}
        identification-resource     {:id            (str resource-url "/identification-resource")
                              :resourceURI    resource-uri
                              :created        timestamp
                              :updated        timestamp
                              :acl            valid-acl
                              ;; profiling fields
                              :idkey      "998F36E8DCD68C6B97770DAF3AED5BD2AA2AC4DA4EF71E8EBE94F32FCE7D0F8C30BC9BE502CA79FA9CA66E8E8E00EC0A21BF8EC3252736DF01F54FF40AE6FD6E"
                              :deviceid   "2EBD853CDA3D0643AFA3619293DFA829338473A5149254A9CD2A28532F8E0E7E1F465F91D5E5F296791D9F2FBBB9122995360B258837612676EB7CC9F510240E"}]
    (is (s/valid? :cimi/identification identification-resource))
    (is (not (s/valid? :cimi/identification (assoc identification-resource :bad-field "bla bla bla"))))))