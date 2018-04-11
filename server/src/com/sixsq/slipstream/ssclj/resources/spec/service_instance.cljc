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
; 	"service_id": string,
;   "agreement_id": string,
; 	"status": string,
; 	"agents": [
;     {"agent": resource-link, "url": "192.168.1.31", "port": int, "container_id": string, "status": string, "num_cpus": int}
;   ]
; }


(s/def :cimi.service-instance/service_id :cimi.core/nonblank-string)   ; service (changed to string)
(s/def :cimi.service-instance/agreement_id :cimi.core/nonblank-string) ; sla (changed to string)
(s/def :cimi.service-instance/status :cimi.core/nonblank-string)
; agent fileds:
(s/def :cimi.service-instance/agent :cimi.common/resource-link)         ; (not mandatory field)
(s/def :cimi.service-instance/url :cimi.core/nonblank-string)           ; docker (new field)
(s/def :cimi.service-instance/port pos-int?)
(s/def :cimi.service-instance/num_cpus pos-int?)
(s/def :cimi.service-instance/container_id string?)
(s/def :cimi.service-instance/allow? boolean?)
(s/def :cimi.service-instance/agentinfo (su/only-keys :req-un [:cimi.service-instance/url
                                                               :cimi.service-instance/port
                                                               :cimi.service-instance/status
                                                               :cimi.service-instance/container_id
                                                               :cimi.service-instance/allow
                                                               ; resources assigned to agent:
                                                               :cimi.service-instance/num_cpus]
                                                      :opt-un [:cimi.service-instance/agent]))
(s/def :cimi.service-instance/agents (s/coll-of :cimi.service-instance/agentinfo :kind vector? :distinct true))


(s/def :cimi/service-instance
  (su/only-keys :req-un [:cimi.common/id
                         :cimi.common/resourceURI
                         :cimi.common/acl
                         :cimi.service-instance/service_id
                         :cimi.service-instance/agreement_id
                         :cimi.service-instance/status
                         :cimi.common/created
                         :cimi.common/updated
                         :cimi.service-instance/agents]
                :opt-un [:cimi.common/name
                         :cimi.common/description
                         :cimi.common/properties
                         :cimi.common/operations]))