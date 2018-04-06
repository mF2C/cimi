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

(s/def :cimi.sla_violation/agreement_id string?)
(s/def :cimi.sla_violation/guarantee string?)
(s/def :cimi.sla_violation/datetime :cimi.core/timestamp)

(s/def :cimi/sla_violation
  (su/only-keys :req-un [
                        :cimi.common/id
                        :cimi.common/resourceURI
                        :cimi.common/acl
                        :cimi.common/created
                        :cimi.common/updated
                        :cimi.sla_violation/agreement_id
                        :cimi.sla_violation/guarantee
                        :cimi.sla_violation/datetime]))
