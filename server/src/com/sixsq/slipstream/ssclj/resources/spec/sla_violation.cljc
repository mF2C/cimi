(ns com.sixsq.slipstream.ssclj.resources.spec.sla-violation
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.util.spec :as su]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))

; {
;   "id": URI
;   "agreement_id": URI
;   "guarantee": string
;   "datetime": datetime
; }

(s/def :cimi.sla-violation/agreement_id string?)
(s/def :cimi.sla-violation/guarantee string?)
(s/def :cimi.sla-violation/datetime :cimi.core/timestamp)

(s/def :cimi/sla-violation
  (su/only-keys :req-un [
                        :cimi.common/id
                        :cimi.common/resourceURI
                        :cimi.common/acl
                        :cimi.common/created
                        :cimi.common/updated
                        :cimi.sla-violation/agreement_id
                        :cimi.sla-violation/guarantee
                        :cimi.sla-violation/datetime]))
