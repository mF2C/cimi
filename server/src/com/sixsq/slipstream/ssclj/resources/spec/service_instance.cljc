(ns com.sixsq.slipstream.ssclj.resources.spec.service-instance
  (:require 
    [com.sixsq.slipstream.ssclj.resources.spec.common :as cimi-common] 
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core]
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.util.spec :as su]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))

; {
; 	"id": URI,
;   "user": string,
; 	"service": string,
;   "agreement": string,
; 	"status": string,
; 	"agents": [
;     {"agent": resource-link, "url": "192.168.1.31", "ports": [], "agent_param": string,
;      "container_id": string, "status": string, "num_cpus": int, "allow": boolean, "master_compss": boolean}
;   ]
; }


(s/def :cimi.service-instance/user ::cimi-core/nonblank-string)       ; the user that launches the service
(s/def :cimi.service-instance/service ::cimi-core/nonblank-string)    ; service
(s/def :cimi.service-instance/agreement ::cimi-core/nonblank-string)  ; sla
(s/def :cimi.service-instance/status ::cimi-core/nonblank-string)
; agent fileds:
(s/def :cimi.service-instance/agent ::cimi-common/resource-link)
(s/def :cimi.service-instance/url ::cimi-core/nonblank-string)
(s/def :cimi.service-instance/ports (s/coll-of ::cimi-core/port)) ; pos-int?
(s/def :cimi.service-instance/num_cpus pos-int?)
(s/def :cimi.service-instance/container_id string?)
(s/def :cimi.service-instance/allow boolean?)
(s/def :cimi.service-instance/master_compss boolean?)
(s/def :cimi.service-instance/agent_param string?)
(s/def :cimi.service-instance/agentinfo (su/only-keys :req-un [:cimi.service-instance/url
                                                               :cimi.service-instance/ports
                                                               :cimi.service-instance/status
                                                               :cimi.service-instance/container_id
                                                               :cimi.service-instance/allow
                                                               ; resources assigned to agent:
                                                               :cimi.service-instance/num_cpus]
                                                      :opt-un [:cimi.service-instance/agent
                                                               :cimi.service-instance/master_compss
                                                               :cimi.service-instance/agent_param]))
(s/def :cimi.service-instance/agents (s/coll-of :cimi.service-instance/agentinfo :kind vector? :distinct true))


(s/def :cimi/service-instance
  (su/only-keys :req-un [::cimi-common/id
                         ::cimi-common/resourceURI
                         ::cimi-common/acl
                         :cimi.service-instance/user
                         :cimi.service-instance/service
                         :cimi.service-instance/agreement
                         :cimi.service-instance/status
                         ::cimi-common/created
                         ::cimi-common/updated
                         :cimi.service-instance/agents]
                :opt-un [::cimi-common/name
                         ::cimi-common/description
                         ::cimi-common/properties
                         ::cimi-common/operations]))
