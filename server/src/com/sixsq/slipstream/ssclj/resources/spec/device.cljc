(ns com.sixsq.slipstream.ssclj.resources.spec.device
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core]
    [com.sixsq.slipstream.ssclj.util.spec :as su]))



(s/def :cimi.device/deviceID ::cimi-core/nonblank-string)
(s/def :cimi.device/isLeader boolean?)
(s/def :cimi.device/os ::cimi-core/nonblank-string)
(s/def :cimi.device/arch ::cimi-core/nonblank-string)
(s/def :cimi.device/cpuManufacturer ::cimi-core/nonblank-string)
(s/def :cimi.device/physicalCores nat-int?)
(s/def :cimi.device/logicalCores nat-int?)
(s/def :cimi.device/cpuClockSpeed ::cimi-core/nonblank-string)
(s/def :cimi.device/memory float?)
(s/def :cimi.device/storage float?)
;(s/def :cimi.device/powerPlugged boolean?)
(s/def :cimi.device/agentType ::cimi-core/nonblank-string)
(s/def :cimi.device/status ::cimi-core/nonblank-string)
(s/def :cimi.device/networkingStandards ::cimi-core/nonblank-string)
;(s/def :cimi.device/ethernetAddress ::cimi-core/nonblank-string)
;(s/def :cimi.device/wifiAddress ::cimi-core/nonblank-string)
(s/def :cimi.device/hwloc string?)
(s/def :cimi.device/cpuinfo string?)


(s/def :cimi/device
  (su/only-keys-maps c/common-attrs
                     {:req-un [:cimi.device/deviceID
                               :cimi.device/isLeader
                               :cimi.device/os
                               :cimi.device/arch
                               :cimi.device/cpuManufacturer
                               :cimi.device/physicalCores
                               :cimi.device/logicalCores
                               :cimi.device/cpuClockSpeed
                               :cimi.device/memory
                               :cimi.device/storage
                               ;:cimi.device/powerPlugged
                               :cimi.device/agentType
                               ;:cimi.device/status
                               :cimi.device/networkingStandards
                               :cimi.device/hwloc
                               :cimi.device/cpuinfo]}))
;:opt-un [:cimi.device/hwloc
;         :cimi.device/cpuinfo]}))


