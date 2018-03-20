(ns com.sixsq.slipstream.ssclj.resources.spec.user-profile
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.util.spec :as su]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))

; {
;   "user_id": "user/1230958abdef",
; 	"id": URI,
; 	"name": string,
; 	"description": "profiling ...",
; 	"created": dateTime,
; 	"updated": dateTime,
; 	"resourceURI": URI,
; 	"id_key": string,
; 	"email": string,
; 	"service_consumer": boolean,
; 	"resource_contributor": boolean
; }


(s/def :cimi.user-profile/user_id :cimi.core/nonblank-string)      ; user
(s/def :cimi.user-profile/id_key :cimi.core/nonblank-string)
(s/def :cimi.user-profile/email :cimi.core/email)
(s/def :cimi.user-profile/service_consumer boolean?)
(s/def :cimi.user-profile/resource_contributor boolean?)


(s/def :cimi/user-profile
  (su/only-keys :req-un [:cimi.common/id
                         :cimi.common/resourceURI
                         :cimi.common/acl
                         ; user-profile
                         :cimi.user-profile/service_consumer
                         :cimi.user-profile/resource_contributor
                         :cimi.common/created
                         :cimi.common/updated]
                :opt-un [:cimi.common/name
                         :cimi.common/description
                         :cimi.common/properties
                         :cimi.common/operations]))
