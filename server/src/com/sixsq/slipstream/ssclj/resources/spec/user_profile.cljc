(ns com.sixsq.slipstream.ssclj.resources.spec.user-profile
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as cimi-common]
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core]
    [com.sixsq.slipstream.ssclj.util.spec :as su]))

; {
;   "device_id": string,
; 	"service_consumer": boolean,
; 	"resource_contributor": boolean
; }


(s/def :cimi.user-profile/device_id ::cimi-core/nonblank-string) ; device ID
(s/def :cimi.user-profile/service_consumer boolean?)
(s/def :cimi.user-profile/resource_contributor boolean?)


(s/def :cimi/user-profile
  (su/only-keys :req-un [::cimi-common/id
                         ::cimi-common/resourceURI
                         ::cimi-common/acl
                         ::cimi-common/created
                         ::cimi-common/updated
                         ; user-profile
                         :cimi.user-profile/device_id
                         :cimi.user-profile/service_consumer
                         :cimi.user-profile/resource_contributor]
                :opt-un [::cimi-common/name
                         ::cimi-common/description
                         ::cimi-common/properties
                         ::cimi-common/operations]))
