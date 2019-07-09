(ns com.sixsq.slipstream.ssclj.resources.spec.agreement
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.resources.spec.sla-assessment :as sla-assessment]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as cimi-common]
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core]
    [com.sixsq.slipstream.ssclj.util.spec :as su]))

; {
;   "id": URI
;   "name": string
;   "state": STARTED|STOPPED|TERMINATED
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
;   }
;   "details": {
;       "id": string    ; optional
;       "type": AGREEMENT|TEMPLATE
;       "name": string
;       "provider": { id : string, name: string}
;       "client": { id: string, name: string}
;       "creation": dateTime,
;       "expiration": dateTime,
;       "variables": [
;          "name": string,
;          "metric": string,
;          "aggregation": {
;             type: AVERAGE,
;             size: int
;          }
;       ],
;       "guarantees": [
;           "name": string
;           "constraint": string
;           "scope": string
;           "schedule" string
;       ]
;   }
; }

(s/def :cimi.agreement/state #{"started" "stopped" "terminated"})

; ASSESSMENT
; namespace in sla-assessment

; DETAILS
(s/def :cimi.agreement/id string?)

; type is used in details.type and details.variables.aggregation.type
; The following is better than just using a string without needing to create
; a new namespace for one of the two usages.
(s/def :cimi.agreement/type #{"agreement" "template" "average"})
;(s/def :cimi.agreement/type string?)

(s/def :cimi.agreement/party (su/only-keys :req-un [:cimi.agreement/id ; this could a common/resourceURI in the near future
                                                    ::cimi-common/name]))

(s/def :cimi.agreement/provider :cimi.agreement/party)
(s/def :cimi.agreement/client :cimi.agreement/party)

(s/def :cimi.agreement/creation ::cimi-core/timestamp)
(s/def :cimi.agreement/expiration ::cimi-core/timestamp)

; DETAILS/GUARANTEE
(s/def :cimi.agreement/constraint string?)
(s/def :cimi.agreement/scope string?)
(s/def :cimi.agreement/schedule string?)

(s/def :cimi.agreement/guarantee (su/only-keys :req-un [::cimi-common/name
                                                        :cimi.agreement/constraint]
                                               :opt-un [:cimi.agreement/scope
                                                        :cimi.agreement/schedule]))

(s/def :cimi.agreement/guarantees (s/coll-of :cimi.agreement/guarantee :kind vector? :distinct true))

; DETAILS/VARIABLES
(s/def :cimi.agreement/metric string?)
(s/def :cimi.agreement/window nat-int?)
(s/def :cimi.agreement/aggregation (su/only-keys :req-un [
                                            :cimi.agreement/type 
                                            :cimi.agreement/window]))

(s/def :cimi.agreement/variable (su/only-keys :req-un [::cimi-common/name]
                                              :opt-un [:cimi.agreement/metric
                                                       :cimi.agreement/aggregation]))

(s/def :cimi.agreement/variables (s/coll-of :cimi.agreement/variable :kind vector? :distinct true))

; --

(s/def :cimi.agreement/details (su/only-keys :req-un [:cimi.agreement/type
                                                      ::cimi-common/name
                                                      :cimi.agreement/provider
                                                      :cimi.agreement/client
                                                      :cimi.agreement/creation
                                                      :cimi.agreement/guarantees]
                                             :opt-un [:cimi.agreement/expiration
                                                      :cimi.agreement/variables
                                                      :cimi.agreement/id]))

; --

(s/def :cimi/agreement
  (su/only-keys :req-un [::cimi-common/id
                         ::cimi-common/resourceURI
                         ::cimi-common/acl
                         ::cimi-common/created
                         ::cimi-common/updated
                         :cimi.agreement/state
                         :cimi.agreement/details]
                :opt-un [::cimi-common/name
                         ::cimi-common/properties
                         :cimi.sla-assessment/assessment
                         ::cimi-common/operations]))
