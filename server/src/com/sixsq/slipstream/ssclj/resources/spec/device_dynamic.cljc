(ns com.sixsq.slipstream.ssclj.resources.spec.device-dynamic
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as cimi-common]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core]
    [com.sixsq.slipstream.ssclj.util.spec :as su]))




(s/def :cimi.device-dynamic/device ::cimi-common/resource-link)
; (s/def :cimi.device-dynamic/isLeader boolean?)
;(s/def :cimi.device-dynamic/ramUnits ::cimi-core/nonblank-string)
(s/def :cimi.device-dynamic/ramFree float?)
(s/def :cimi.device-dynamic/ramFreePercent float?)
;(s/def :cimi.device-dynamic/storageUnits ::cimi-core/nonblank-string)
(s/def :cimi.device-dynamic/storageFree float?)
(s/def :cimi.device-dynamic/storageFreePercent float?)
(s/def :cimi.device-dynamic/cpuFreePercent float?)
(s/def :cimi.device-dynamic/powerRemainingStatus ::cimi-core/nonblank-string)
(s/def :cimi.device-dynamic/powerRemainingStatusSeconds ::cimi-core/nonblank-string)
(s/def :cimi.device-dynamic/powerPlugged boolean?)
(s/def :cimi.device-dynamic/ethernetAddress ::cimi-core/nonblank-string)
(s/def :cimi.device-dynamic/wifiAddress ::cimi-core/nonblank-string)
(s/def :cimi.device-dynamic/ethernetThroughputInfo (s/coll-of string?))
(s/def :cimi.device-dynamic/wifiThroughputInfo (s/coll-of string?))
(s/def :cimi.device-dynamic/actuatorInfo ::cimi-core/nonblank-string)
(s/def :cimi.device-dynamic/status ::cimi-core/nonblank-string)
(s/def :cimi.device-dynamic/sensorType string?)
(s/def :cimi.device-dynamic/sensorModel string?)
(s/def :cimi.device-dynamic/sensorConnection string?)
;(s/def :cimi.device-dynamic/myLeaderID ::cimi-common/resource-link)

(s/def :cimi.device-dynamic/sensor (su/only-keys :req-un [:cimi.device-dynamic/sensorType
																													:cimi.device-dynamic/sensorConnection
																													:cimi.device-dynamic/sensorModel]))

(s/def :cimi.device-dynamic/sensors (s/coll-of :cimi.device-dynamic/sensor :min-count 0 :kind vector?))

(s/def :cimi/device-dynamic
	(su/only-keys-maps c/common-attrs
										 {:req-un [:cimi.device-dynamic/device
															 ; :cimi.device-dynamic/isLeader
															 ;:cimi.device-dynamic/ramUnits
																:cimi.device-dynamic/ramFree
																:cimi.device-dynamic/ramFreePercent
															 ;:cimi.device-dynamic/storageUnits
																:cimi.device-dynamic/storageFree
																:cimi.device-dynamic/storageFreePercent
																:cimi.device-dynamic/cpuFreePercent
																:cimi.device-dynamic/powerRemainingStatus
																:cimi.device-dynamic/powerRemainingStatusSeconds
																:cimi.device-dynamic/powerPlugged
																:cimi.device-dynamic/ethernetAddress
																:cimi.device-dynamic/wifiAddress
																:cimi.device-dynamic/ethernetThroughputInfo
																:cimi.device-dynamic/wifiThroughputInfo
																:cimi.device-dynamic/actuatorInfo
																:cimi.device-dynamic/status]
											:opt-un [:cimi.device-dynamic/sensors]}))

