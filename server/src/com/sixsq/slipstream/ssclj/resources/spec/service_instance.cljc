(ns com.sixsq.slipstream.ssclj.resources.spec.service-instance
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


(s/def :cimi.service-instance/service_id :cimi.common/resource-link)   ; service
(s/def :cimi.service-instance/agreement_id :cimi.common/resource-link) ; sla
(s/def :cimi.service-instance/user_id :cimi.common/resource-link)      ; user
(s/def :cimi.service-instance/status :cimi.core/nonblank-string)
; agent fileds:
(s/def :cimi.service-instance/agent :cimi.common/resource-link)
(s/def :cimi.service-instance/port pos-int?)
(s/def :cimi.service-instance/num_cpus pos-int?)
(s/def :cimi.service-instance/container_id string?)
(s/def :cimi.service-instance/agentinfo (su/only-keys :req-un [:cimi.service-instance/agent
                                                              :cimi.service-instance/port
                                                              :cimi.service-instance/status
                                                              :cimi.service-instance/container_id
                                                              ; resources assigned to agent:
                                                              :cimi.service-instance/num_cpus]))
(s/def :cimi.service-instance/agents (s/coll-of :cimi.service-instance/agentinfo :kind vector? :distinct true))


(s/def :cimi/service-instance
  (su/only-keys :req-un [:cimi.common/id
                         :cimi.common/resourceURI
                         :cimi.common/acl
                         :cimi.service-instance/service_id
                         :cimi.service-instance/agreement_id
                         :cimi.service-instance/user_id
                         :cimi.service-instance/status
                         :cimi.service-instance/agents]
                :opt-un [:cimi.common/created               ;; FIXME: should be required
                         :cimi.common/updated               ;; FIXME: should be required
                         :cimi.common/name
                         :cimi.common/description
                         :cimi.common/properties
                         :cimi.common/operations]))
