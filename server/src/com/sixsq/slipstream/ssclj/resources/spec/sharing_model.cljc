(ns com.sixsq.slipstream.ssclj.resources.spec.sharing-model
  (:require 
    [clojure.spec.alpha :as s] 
    [com.sixsq.slipstream.ssclj.resources.spec.common :as cimi-common]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core]
    [com.sixsq.slipstream.ssclj.util.spec :as su]))

; {
;   "user_id": {:href "user/1230958abdef"},
;   "device_id": string,
; 	"GPS_allowed": boolean,
; 	"max_CPU_usage": integer,
; 	"max_memory_usage": integer,
; 	"max_storage_usage": integer,
; 	"max_bandwidth_usage": integer,
; 	"battery_limit": integer
; }


(s/def :cimi.sharing-model/user_id ::cimi-core/nonblank-string)      	; user
(s/def :cimi.sharing-model/device_id ::cimi-core/nonblank-string)  	; device ID
(s/def :cimi.sharing-model/gps_allowed boolean?)
(s/def :cimi.sharing-model/max_cpu_usage pos-int?)
(s/def :cimi.sharing-model/max_memory_usage pos-int?)
(s/def :cimi.sharing-model/max_storage_usage pos-int?)
(s/def :cimi.sharing-model/max_bandwidth_usage pos-int?)
(s/def :cimi.sharing-model/battery_limit pos-int?)


(s/def :cimi/sharing-model
  (su/only-keys :req-un [::cimi-common/id
                         ::cimi-common/resourceURI
                         ::cimi-common/acl
                         ::cimi-common/created
                         ::cimi-common/updated
                         ; sharing-model
						 :cimi.sharing-instance/user_id
						 :cimi.sharing-instance/device_id
                         :cimi.sharing-model/gps_allowed
                         :cimi.sharing-model/max_cpu_usage
                         :cimi.sharing-model/max_memory_usage
                         :cimi.sharing-model/max_storage_usage
                         :cimi.sharing-model/max_bandwidth_usage
                         :cimi.sharing-model/battery_limit]
                :opt-un [::cimi-common/name
                         ::cimi-common/description
                         ::cimi-common/properties
                         ::cimi-common/operations]))
