(ns com.sixsq.slipstream.ssclj.resources.spec.identification
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as cimi-common]
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core]
    [com.sixsq.slipstream.ssclj.util.spec :as su]))

; {
;   "user": string,
; 	"idkey": string,
; 	"deviceid": string
; }


(s/def :cimi.identification/user ::cimi-core/nonblank-string)
(s/def :cimi.identification/idkey string?)
(s/def :cimi.identification/deviceid string?)


(s/def :cimi/identification
  (su/only-keys :req-un [::cimi-common/id
                         ::cimi-common/resourceURI
                         ::cimi-common/acl
                         ::cimi-common/created
                         ::cimi-common/updated
                         ; user-profile
                         :cimi.identification/user
                         :cimi.identification/idkey
                         :cimi.identification/deviceid]))