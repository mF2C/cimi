(ns com.sixsq.slipstream.ssclj.resources.spec.sla-template
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.resources.spec.agreement :as agreement]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as cimi-common]
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core]
    [com.sixsq.slipstream.ssclj.util.spec :as su]))

; {
;   "id": URI
;   "name": string
;   "state": STARTED|STOPPED|TERMINATED
;   "details": {
;       "id": string    ; optional
;       "type": TEMPLATE
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


(s/def :cimi/sla-template
  (su/only-keys :req-un [::cimi-common/id
                         ::cimi-common/resourceURI
                         ::cimi-common/acl
                         ::cimi-common/created
                         ::cimi-common/updated
                         :cimi.agreement/state
                         :cimi.agreement/details]
                :opt-un [::cimi-common/name
                         ::cimi-common/properties
                         ::cimi-common/operations]))
