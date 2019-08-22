(ns com.sixsq.slipstream.ssclj.resources.spec.sla-assessment-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [are deftest is]]
            [com.sixsq.slipstream.ssclj.resources.common.schema :as schema]
            [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
            [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))

(deftest check-assessment-resource-schema
  (let [
        first_execution    "2019-07-08T10:00:00.01Z"
        last_execution     "2019-07-08T14:57:00.01Z"
        last               {:m0 {:key      "m0"
                                 :value    50.0
                                 :datetime first_execution}
                            :m1 {:key      "m1"
                                 :value    51
                                 :datetime last_execution}}
        guarantees         {:gt0 {:first_execution first_execution
                                  :last_execution  last_execution
                                  :last_values     last}
                            }
        initial-assessment {:first_execution first_execution
                            :last_execution  last_execution
                            }]

    ; Not working - testing directly in agreement-test
    ;    (is (s/valid? :cimi/sla-assessment/assessment initial-assessment))
    ;    (is (s/valid? :cimi/sla-assessment/assessment (assoc initial-assessment :guarantees guarantees)))
    ;    (is (not (s/valid? :cimi/sla-assessment/assessment (assoc initial-assessment :bad-field "bla bla bla"))))
    ))
