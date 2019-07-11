; Namespace sla-assessment corresponds to agreement.assessment

(ns com.sixsq.slipstream.ssclj.resources.spec.sla-assessment
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as cimi-common]
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core]
    [com.sixsq.slipstream.ssclj.util.spec :as su]))

;   "assessment": {
;       "first_execution": dateTime
;       "last_execution": dateTime
;       "guarantees": {
;           <gt-name>: {
;             "first_execution": dateTime
;             "last_execution": dateTime
;             "last_values": {
;                "<var-name>": {
;                    "key" string
;                    "value" string
;                    "datetime" timestamp
;                }
;             }
;           }
;       }


(s/def :cimi.sla-assessment/first_execution ::cimi-core/timestamp)
(s/def :cimi.sla-assessment/last_execution ::cimi-core/timestamp)
(s/def :cimi.sla-assessment/key string?)
(s/def :cimi.sla-assessment/value any?)
(s/def :cimi.sla-assessment/datetime ::cimi-core/timestamp)


(s/def :cimi.sla-assessment/metricvalue (su/only-keys
        :req-un [:cimi.sla-assessment/key
                :cimi.sla-assessment/value
                :cimi.sla-assessment/datetime]))

(s/def :cimi.sla-assessment/last_values (su/constrained-map keyword? :cimi.sla-assessment/metricvalue))

(s/def :cimi.sla-assessment/guarantee (su/only-keys
        :req-un [:cimi.sla-assessment/first_execution
                :cimi.sla-assessment/last_execution]
        :opt-un [:cimi.sla-assessment/last_values]))

(s/def :cimi.sla-assessment/guarantees (su/constrained-map keyword? :cimi.sla-assessment/guarantee))

(s/def :cimi.sla-assessment/assessment (su/only-keys
        :req-un [:cimi.sla-assessment/first_execution
                :cimi.sla-assessment/last_execution]
        :opt-un [:cimi.sla-assessment/guarantees]))

