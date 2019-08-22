(ns com.sixsq.slipstream.ssclj.resources.spec.event-test
  (:require
    [clojure.spec.alpha :as s]
    [clojure.test :refer [are deftest is]]
    [com.sixsq.slipstream.ssclj.resources.event :refer :all]
    [com.sixsq.slipstream.ssclj.resources.spec.event :as event]))


(def event-timestamp "2015-01-16T08:05:00.0Z")


(def valid-event
  {:id          "event/262626262626262"
   :resourceURI resource-uri
   :acl         {:owner {:type "USER" :principal "joe"}
                 :rules [{:type "ROLE" :principal "ANON" :right "ALL"}]}

   :timestamp   event-timestamp
   :content     {:resource {:href "module/HNSciCloud-RHEA/S3"}
                 :state    "Started"}
   :type        "state"
   :severity    "critical"})


(deftest check-reference
  (let [updated-event (assoc-in valid-event [:content :resource :href] "another/valid-identifier")]
    (is (s/valid? ::event/event updated-event)))
  (let [updated-event (assoc-in valid-event [:content :resource :href] "/not a valid reference/")]
    (is (not (s/valid? ::event/event updated-event)))))


(deftest check-severity
  (doseq [valid-severity ["critical" "high" "medium" "low"]]
    (is (s/valid? ::event/event (assoc valid-event :severity valid-severity))))
  (is (not (s/valid? ::event/event (assoc valid-event :severity "unknown-severity")))))


(deftest check-type
  (doseq [valid-type ["state" "alarm"]]
    (is (s/valid? ::event/event (assoc valid-event :type valid-type))))
  (is (not (s/valid? ::event/event (assoc valid-event :type "unknown-type")))))


(deftest check-event-schema

  (is (s/valid? ::event/event valid-event))

  ;; mandatory keywords
  (doseq [k #{:id :resourceURI :acl :timestamp :content :type :severity}]
    (is (not (s/valid? ::event/event (dissoc valid-event k)))))

  ;; optional keywords
  (doseq [k #{}]
    (is (s/valid? ::event/event (dissoc valid-event k)))))
