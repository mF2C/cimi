(ns com.sixsq.slipstream.ssclj.resources.spec.service-instance
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as cimi-common]
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core]
    [com.sixsq.slipstream.ssclj.util.spec :as su]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; {
;   "id": URI,
;   "user": string,
;   "device_id": string,
;   "device_ip": string,
;   "parent_device_id": string,
;   "parent_device_ip": string,
;   "service": string,
;   "agreement": string,
; 	"status": string,
;   "service_type": string,
;   "exec_device_id": string,
;   "exec_device_ip": string,
;   "agents": [
;     {
;       "app_type": string,
;       "url": "192.168.1.31",
;       "device_id": string,
;       "ports": [],
;       "agent_param": string,
;       "container_id": string,
;       "status": string,
;       "allow": boolean,
;       "master_compss": boolean,
;       "compss_app_id": string
;     }
;   ]
; }
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(s/def :cimi.service-instance/user ::cimi-core/nonblank-string) ; the user that launches the service
(s/def :cimi.service-instance/device_id ::cimi-core/nonblank-string) ; the device ID that gets the "submit" request
(s/def :cimi.service-instance/device_ip ::cimi-core/nonblank-string) ; the device IP address that gets the "submit" request
(s/def :cimi.service-instance/parent_device_id ::cimi-core/nonblank-string) ; the leader device ID
(s/def :cimi.service-instance/parent_device_ip ::cimi-core/nonblank-string) ; the leader device IP address
(s/def :cimi.service-instance/service ::cimi-core/nonblank-string) ; service
(s/def :cimi.service-instance/agreement ::cimi-core/nonblank-string) ; sla
(s/def :cimi.service-instance/status ::cimi-core/nonblank-string) ; status of the service (instance): running, error...
(s/def :cimi.service-instance/service_type ::cimi-core/nonblank-string) ; type of service: docker, docker-compose, docker-swarm, kubernetes
; agent fileds:
(s/def :cimi.service-instance/url ::cimi-core/nonblank-string)
(s/def :cimi.service-instance/ports (s/coll-of ::cimi-core/port))
(s/def :cimi.service-instance/container_id string?)
(s/def :cimi.service-instance/compss_app_id string?)
(s/def :cimi.service-instance/allow boolean?)
(s/def :cimi.service-instance/master_compss boolean?)
(s/def :cimi.service-instance/agent_param string?)
(s/def :cimi.service-instance/app_type ::cimi-core/nonblank-string)
(s/def :cimi.service-instance/agentinfo (su/only-keys :req-un [:cimi.service-instance/url
                                                               :cimi.service-instance/device_id
                                                               :cimi.service-instance/ports
                                                               :cimi.service-instance/status
                                                               :cimi.service-instance/container_id
                                                               :cimi.service-instance/allow
                                                               :cimi.service-instance/app_type]
                                                      :opt-un [:cimi.service-instance/master_compss
                                                               :cimi.service-instance/compss_app_id
                                                               :cimi.service-instance/agent_param]))
(s/def :cimi.service-instance/agents (s/coll-of :cimi.service-instance/agentinfo :kind vector? :distinct true))


(s/def :cimi/service-instance
  (su/only-keys :req-un [::cimi-common/id
                         ::cimi-common/resourceURI
                         ::cimi-common/acl
                         :cimi.service-instance/user
                         :cimi.service-instance/device_id
                         :cimi.service-instance/device_ip
                         :cimi.service-instance/parent_device_id
                         :cimi.service-instance/parent_device_ip
                         :cimi.service-instance/service
                         :cimi.service-instance/agreement
                         :cimi.service-instance/status
                         :cimi.service-instance/service_type
                         ::cimi-common/created
                         ::cimi-common/updated
                         :cimi.service-instance/agents]
                :opt-un [::cimi-common/name
                         ::cimi-common/description
                         ::cimi-common/properties
                         ::cimi-common/operations]))
