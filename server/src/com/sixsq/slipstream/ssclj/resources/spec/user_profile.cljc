(ns com.sixsq.slipstream.ssclj.resources.spec.user-profile
  (:require 
    [clojure.spec.alpha :as s] 
    [com.sixsq.slipstream.ssclj.resources.spec.common :as cimi-common]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core]
    [com.sixsq.slipstream.ssclj.util.spec :as su]))

; {
;   "user_id": "user/1230958abdef",
;   "device_id": string,
; 	"service_consumer": boolean,
; 	"resource_contributor": boolean
; 	"max_apps": integer
; }


(s/def :cimi.user-profile/user_id ::cimi-core/nonblank-string)      	; user
(s/def :cimi.user-profile/device_id ::cimi-core/nonblank-string)  	; device ID
(s/def :cimi.user-profile/service_consumer boolean?)
(s/def :cimi.user-profile/resource_contributor boolean?)
(s/def :cimi.user-profile/max_apps pos-int?)


(s/def :cimi/user-profile
  (su/only-keys :req-un [::cimi-common/id
                         ::cimi-common/resourceURI
                         ::cimi-common/acl
                         ; user-profile
						 :cimi.user-profile/user_id
						 :cimi.user-profile/device_id
                         :cimi.user-profile/service_consumer
						 :cimi.user-profile/resource_contributor
                         :cimi.user-profile/max_apps
                         ::cimi-common/created
                         ::cimi-common/updated]
                :opt-un [::cimi-common/name
                         ::cimi-common/description
                         ::cimi-common/properties
                         ::cimi-common/operations]))
