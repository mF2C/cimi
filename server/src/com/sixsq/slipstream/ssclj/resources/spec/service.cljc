(ns com.sixsq.slipstream.ssclj.resources.spec.service
  (:require 
    [com.sixsq.slipstream.ssclj.resources.spec.common :as cimi-common] 
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core]
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.util.spec :as su]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))


(s/def :cimi.service/cpu #{"high" "medium" "low"})
(s/def :cimi.service/memory #{"high" "medium" "low"})
(s/def :cimi.service/storage #{"high" "medium" "low"})
(s/def :cimi.service/inclinometer? boolean?)
(s/def :cimi.service/temperature? boolean?)
(s/def :cimi.service/jammer? boolean?)
(s/def :cimi.service/location? boolean?)
(s/def :cimi.service/battery_level? boolean?)
(s/def :cimi.service/door_sensor? boolean?)
(s/def :cimi.service/pump_sensor? boolean?)
(s/def :cimi.service/accelerometer? boolean?)
(s/def :cimi.service/humidity? boolean?)
(s/def :cimi.service/air_pressure? boolean?)
(s/def :cimi.service/ir_motion? boolean?)
(s/def :cimi.service/category (su/only-keys :req-un [:cimi.service/cpu
                                                     :cimi.service/memory
                                                     :cimi.service/storage
                                                     :cimi.service/inclinometer
                                                     :cimi.service/temperature
                                                     :cimi.service/jammer
                                                     :cimi.service/location
                                                     :cimi.service/battery_level
                                                     :cimi.service/door_sensor
                                                     :cimi.service/pump_sensor
                                                     :cimi.service/accelerometer
                                                     :cimi.service/humidity
                                                     :cimi.service/air_pressure
                                                     :cimi.service/ir_motion]))
(s/def :cimi.service/exec ::cimi-core/nonblank-string)
(s/def :cimi.service/exec_type #{"docker" "compss" "docker-compose"})
(s/def :cimi.service/exec_ports vector?)
(s/def :cimi/service
  (su/only-keys :req-un [::cimi-common/id
                         ::cimi-common/resourceURI
                         :cimi.service/category
                         ::cimi-common/created
                         ::cimi-common/updated
                         ::cimi-common/acl
                         :cimi.service/exec
                         :cimi.service/exec_type]
                :opt-un [::cimi-common/name
                         ::cimi-common/description
                         ::cimi-common/properties
                         ::cimi-common/operations
                         :cimi.service/exec_ports]))
