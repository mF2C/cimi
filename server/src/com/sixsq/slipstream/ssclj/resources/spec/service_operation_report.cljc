(ns com.sixsq.slipstream.ssclj.resources.spec.service-operation-report
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as cimi-common]
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core]
    [com.sixsq.slipstream.ssclj.util.spec :as su]))

; {
;   "id": URI
;   "service_instance_id": URI
;   "operation": string
;   "datetime": datetime
;   "execution_time": float
; }

(s/def :cimi.service-operation-report/requesting_application_id ::cimi-common/resource-link)
(s/def :cimi.service-operation-report/compute_node_id string?)
(s/def :cimi.service-operation-report/operation_name string?)
(s/def :cimi.service-operation-report/operation_id string?)
(s/def :cimi.service-operation-report/start_time ::cimi-core/timestamp)
(s/def :cimi.service-operation-report/expected_end_time ::cimi-core/timestamp)
(s/def :cimi.service-operation-report/execution_length float?)
(s/def :cimi.service-operation-report/result string?)

(s/def :cimi/service-operation-report
  (su/only-keys :req-un [::cimi-common/id
                         ::cimi-common/resourceURI
                         ::cimi-common/acl
                         ::cimi-common/created
                         ::cimi-common/updated
                         :cimi.service-operation-report/requesting_application_id
                         :cimi.service-operation-report/compute_node_id
                         :cimi.service-operation-report/operation_name
                         :cimi.service-operation-report/operation_id
                         :cimi.service-operation-report/start_time
                         :cimi.service-operation-report/expected_end_time
                         :cimi.service-operation-report/execution_length
                         :cimi.service-operation-report/result]))
