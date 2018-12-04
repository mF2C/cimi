(ns com.sixsq.slipstream.ssclj.resources.spec.device-dynamic
  (:require 
    [clojure.spec.alpha :as s] 
    [com.sixsq.slipstream.ssclj.resources.spec.common :as cimi-common]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core]
    [com.sixsq.slipstream.ssclj.util.spec :as su]))

;{
;  "device_id":x
;  "updated_on": x
;  "available_RAM_size_in_MB": long,
;  "available_RAM_in_percentage": float,
;  "available_Storage_size_in_MB": long,
;  "available_Storage_in_percentage": float,
;  "available_CPU_percentage": float,
;  "power_remaining_status": string,
;  "remaining_power_info_in_seconds": string,
;  "ethernet_address": string,
;  "wifi_address": string,
;  "throughput_info_ethernet": string,
;  "throughput_info_wifi": string
;  "inclinometer": x,                  The information about the sensors and actuators will be provided   
;  "temperature": x,
;  "jammer": x,
;  "location": x,
;  "ambulance": x,
;  "fire_car": x,
;  "traffic_light": x,
;  "street_light": x
; }


 	
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
(s/def :cimi.device-dynamic/ethernetAddress ::cimi-core/nonblank-string)
(s/def :cimi.device-dynamic/wifiAddress ::cimi-core/nonblank-string)
(s/def :cimi.device-dynamic/ethernetThroughputInfo (s/coll-of integer?))
(s/def :cimi.device-dynamic/wifiThroughputInfo (s/coll-of integer?))
(s/def :cimi.device-dynamic/myLeaderID ::cimi-common/resource-link)


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
								:cimi.device-dynamic/ethernetAddress
								:cimi.device-dynamic/wifiAddress
								:cimi.device-dynamic/ethernetThroughputInfo
								:cimi.device-dynamic/wifiThroughputInfo
								:cimi.device-dynamic/myLeaderID]
						:opt-un []}))
