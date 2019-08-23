(ns
  ^{:copyright "Copyright 2018, SixSq Sàrl"
    :license   "http://www.apache.org/licenses/LICENSE-2.0"}
  com.sixsq.slipstream.ssclj.resources.example-resource.utils-test
  (:require
    [clj-time.core :refer [ago from-now weeks]]
    [clojure.test :refer [are deftest is]]
    [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
    [com.sixsq.slipstream.ssclj.resources.example-resource.utils :as t]))


(deftest check-executable?
  (let [future (-> 2 weeks from-now u/unparse-timestamp-datetime)
        past   (-> 2 weeks ago u/unparse-timestamp-datetime)]
    (are [expected arg] (= expected (t/executable? arg))
                        true {:state "WAITING", :expires future}
                        false {:state "WAITING", :expires past}
                        true {:state "WAITING"}
                        false {:state "FAILED", :expires future}
                        false {:state "FAILED", :expires past}
                        false {:state "FAILED"}
                        false {:state "SUCCEEDED", :expires future}
                        false {:state "SUCCEEDED", :expires past}
                        false {:state "SUCCEEDED"}
                        false {})))
