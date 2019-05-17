(ns com.sixsq.slipstream.ssclj.resources.spec.service-container-metric
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as cimi-common]
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core]
    [com.sixsq.slipstream.ssclj.util.spec :as su]))

; {
;   "id": URI
;   "device_id": URI
;   "container_id": URI
;   "start_time": datetime
;   "stop_time": datetime
; }

(s/def :cimi.service-container-metric/device_id ::cimi-common/resource-link)
(s/def :cimi.service-container-metric/container_id string?)
(s/def :cimi.service-container-metric/start_time ::cimi-core/timestamp)
(s/def :cimi.service-container-metric/stop_time ::cimi-core/timestamp)

(s/def :cimi/service-container-metric
  (su/only-keys :req-un [::cimi-common/id
                         ::cimi-common/resourceURI
                         ::cimi-common/acl
                         ::cimi-common/created
                         ::cimi-common/updated
                         :cimi.service-container-metric/device_id
                         :cimi.service-container-metric/container_id
                         :cimi.service-container-metric/start_time
                         :cimi.service-container-metric/stop_time]))
