(ns com.sixsq.slipstream.ssclj.resources.spec.sharing-model
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.util.spec :as su]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))

; {
;   "user_id": {:href "user/1230958abdef"},
; 	"id": URI,
; 	"name": string,
; 	"description": "sharing model ...",
; 	"created": dateTime,
; 	"updated": dateTime,
; 	"resourceURI": URI,
; 	"max_apps": integer,
; 	"GPS_allowed": boolean,
; 	"max_CPU_usage": integer,
; 	"max_memory_usage": integer,
; 	"max_storage_usage": integer,
; 	"max_bandwidth_usage": integer,
; 	"battery_limit": integer
; }


(s/def :cimi.sharing-model/user_id :cimi.core/nonblank-string)      ; user
(s/def :cimi.sharing-model/max_apps pos-int?)
(s/def :cimi.sharing-model/gps_allowed boolean?)
(s/def :cimi.sharing-model/max_cpu_usage pos-int?)
(s/def :cimi.sharing-model/max_memory_usage pos-int?)
(s/def :cimi.sharing-model/max_storage_usage pos-int?)
(s/def :cimi.sharing-model/max_bandwidth_usage pos-int?)
(s/def :cimi.sharing-model/battery_limit pos-int?)


(s/def :cimi/sharing-model
  (su/only-keys :req-un [:cimi.common/id
                         :cimi.common/resourceURI
                         :cimi.common/acl
                         ; sharing-model
                         :cimi.sharing-model/user_id
                         :cimi.sharing-model/max_apps
                         :cimi.sharing-model/gps_allowed
                         :cimi.sharing-model/max_cpu_usage
                         :cimi.sharing-model/max_memory_usage
                         :cimi.sharing-model/max_storage_usage
                         :cimi.sharing-model/max_bandwidth_usage
                         :cimi.sharing-model/battery_limit]
                :opt-un [:cimi.common/created               ;; FIXME: should be required
                         :cimi.common/updated               ;; FIXME: should be required
                         :cimi.common/name
                         :cimi.common/description
                         :cimi.common/properties
                         :cimi.common/operations]))
