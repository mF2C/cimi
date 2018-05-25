(ns com.sixsq.slipstream.ssclj.resources.spec.agreement
  (:require 
    [com.sixsq.slipstream.ssclj.resources.spec.common :as cimi-common] 
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core]
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.util.spec :as su]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))

; {
;   "id": URI
;   "name": string
;   "state": STARTED|STOPPED|TERMINATED
;   "assessment": {
;       "first_execution": dateTime
;       "last_execution": dateTime
;   }
;   "details": {
;       "id": string    ; optional
;       "type": AGREEMENT|TEMPLATE
;       "name": string
;       "provider": { id : string, name: string}
;       "client": { id: string, name: string}
;       "creation": dateTime,
;       "expiration": dateTime,
;       "guarantees": [
;           "name": string
;           "constraint": string
;       ]
;   }
; }

(s/def :cimi.agreement/state #{"started" "stopped" "terminated"})

; ASSESSMENT
(s/def :cimi.agreement/first_execution ::cimi-core/timestamp)
(s/def :cimi.agreement/last_execution ::cimi-core/timestamp)

(s/def :cimi.agreement/assessment (su/only-keys :req-un [:cimi.agreement/first_execution
                                                        :cimi.agreement/last_execution]))

; DETAILS
(s/def :cimi.agreement/id string?)
(s/def :cimi.agreement/type #{"agreement" "template"})

(s/def :cimi.agreement/party (su/only-keys :req-un [:cimi.agreement/id  ; this could a common/resourceURI in the near future
                                                    ::cimi-common/name]))

(s/def :cimi.agreement/provider :cimi.agreement/party)
(s/def :cimi.agreement/client :cimi.agreement/party)

(s/def :cimi.agreement/creation ::cimi-core/timestamp)
(s/def :cimi.agreement/expiration ::cimi-core/timestamp)

; DETAILS/GUARANTEE
(s/def :cimi.agreement/constraint string?)

(s/def :cimi.agreement/guarantee (su/only-keys :req-un [::cimi-common/name
                                                        :cimi.agreement/constraint]))

(s/def :cimi.agreement/guarantees (s/coll-of :cimi.agreement/guarantee :kind vector? :distinct true))

; --

(s/def :cimi.agreement/details (su/only-keys :req-un [:cimi.agreement/type
                                                    ::cimi-common/name
                                                    :cimi.agreement/provider
                                                    :cimi.agreement/client
                                                    :cimi.agreement/creation
                                                    :cimi.agreement/expiration
                                                    :cimi.agreement/guarantees]
                                             :opt-un [:cimi.agreement/id]))

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
                         :cimi.agreement/assessment
                         ::cimi-common/operations]))
