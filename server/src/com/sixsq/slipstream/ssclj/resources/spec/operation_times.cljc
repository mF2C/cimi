(ns com.sixsq.slipstream.ssclj.resources.spec.operation-times
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.util.spec :as su]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))

; {
;   "id": URI
;   "service_instance_id": URI
;   "operation": string
;   "datetime": datetime
;   "execution_time": float
; }

(s/def :cimi.operation-times/service_instance_id :cimi.common/resource-link)
(s/def :cimi.operation-times/operation string?)
(s/def :cimi.operation-times/datetime :cimi.core/timestamp)
(s/def :cimi.operation-times/execution_time float?)

(s/def :cimi/operation-times
  (su/only-keys :req-un [
                        :cimi.common/id
                        :cimi.common/resourceURI
                        :cimi.common/acl
                        :cimi.common/created
                        :cimi.common/updated
                        :cimi.operation-times/service_instance_id
                        :cimi.operation-times/operation
                        :cimi.operation-times/datetime
                        :cimi.operation-times/execution_time]))
