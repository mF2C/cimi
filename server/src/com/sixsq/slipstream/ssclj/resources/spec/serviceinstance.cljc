(ns com.sixsq.slipstream.ssclj.resources.spec.serviceinstance
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.util.spec :as su]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))

; {
; 	"id": URI,
; 	"name": string,
; 	"description": "profiling ...",
; 	"created": dateTime,
; 	"updated": dateTime,
; 	"resourceURI": URI,
; 	"service_id": resource-link,
;   "agreement_id": resource-link,
;   "user_id": resource-link,
; 	"status": string,
; 	"agents": [
;     {"agent_id": resource-link, "port": int, "container_id": string, "status": string, "num_cpus": int}
;   ]
; }


(s/def :cimi.serviceinstance/service_id :cimi.common/resource-link)   ; service
(s/def :cimi.serviceinstance/agreement_id :cimi.common/resource-link) ; sla
(s/def :cimi.serviceinstance/user_id :cimi.common/resource-link)      ; user
(s/def :cimi.serviceinstance/status :cimi.core/nonblank-string)
; agent fileds:
(s/def :cimi.serviceinstance/agent :cimi.common/resource-link)
(s/def :cimi.serviceinstance/port pos-int?)
(s/def :cimi.serviceinstance/num_cpus pos-int?)
(s/def :cimi.serviceinstance/container_id string?)
(s/def :cimi.serviceinstance/agentinfo (su/only-keys :req-un [:cimi.serviceinstance/agent
                                                              :cimi.serviceinstance/port
                                                              :cimi.serviceinstance/status
                                                              :cimi.serviceinstance/container_id
                                                              ; resources assigned to agent:
                                                              :cimi.serviceinstance/num_cpus]))
(s/def :cimi.serviceinstance/agents (s/coll-of :cimi.serviceinstance/agentinfo :kind vector? :distinct true))


(s/def :cimi/serviceinstance
  (su/only-keys :req-un [:cimi.common/id
                         :cimi.common/resourceURI
                         :cimi.common/acl
                         :cimi.serviceinstance/service_id
                         :cimi.serviceinstance/agreement_id
                         :cimi.serviceinstance/user_id
                         :cimi.serviceinstance/status
                         :cimi.serviceinstance/agents]
                :opt-un [:cimi.common/created               ;; FIXME: should be required
                         :cimi.common/updated               ;; FIXME: should be required
                         :cimi.common/name
                         :cimi.common/description
                         :cimi.common/properties
                         :cimi.common/operations]))
