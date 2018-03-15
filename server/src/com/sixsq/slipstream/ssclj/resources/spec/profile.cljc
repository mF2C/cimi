(ns com.sixsq.slipstream.ssclj.resources.spec.profile
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


(s/def :cimi.profile/user_id :cimi.core/nonblank-string)      ; user
(s/def :cimi.profile/id_key :cimi.core/nonblank-string)
(s/def :cimi.profile/email :cimi.core/email)
(s/def :cimi.profile/service_consumer boolean?)
(s/def :cimi.profile/resource_contributor boolean?)


(s/def :cimi/profile
  (su/only-keys :req-un [:cimi.common/id
                         :cimi.common/resourceURI
                         :cimi.common/acl
                         ; profile
                         :cimi.profile/user_id
                         :cimi.profile/id_key
                         :cimi.profile/email
                         :cimi.profile/service_consumer
                         :cimi.profile/resource_contributor]
                :opt-un [:cimi.common/created               ;; FIXME: should be required
                         :cimi.common/updated               ;; FIXME: should be required
                         :cimi.common/name
                         :cimi.common/description
                         :cimi.common/properties
                         :cimi.common/operations]))
