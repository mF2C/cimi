(ns com.sixsq.slipstream.ssclj.resources.spec.service-operation-report
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

(s/def :cimi.service-operation-report/serviceInstance :cimi.common/resource-link)
(s/def :cimi.service-operation-report/operation string?)
(s/def :cimi.service-operation-report/datetime :cimi.core/timestamp)
(s/def :cimi.service-operation-report/execution_time float?)

(s/def :cimi/service-operation-report
  (su/only-keys :req-un [
                        :cimi.common/id
                        :cimi.common/resourceURI
                        :cimi.common/acl
                        :cimi.common/created
                        :cimi.common/updated
                        :cimi.service-operation-report/serviceInstance
                        :cimi.service-operation-report/operation
                        :cimi.service-operation-report/datetime
                        :cimi.service-operation-report/execution_time]))
