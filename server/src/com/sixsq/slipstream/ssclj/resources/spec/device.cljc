(ns com.sixsq.slipstream.ssclj.resources.spec.device
  (:require 
    [clojure.spec.alpha :as s] 
    [com.sixsq.slipstream.ssclj.resources.spec.common :as cimi-common]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core]
    [com.sixsq.slipstream.ssclj.util.spec :as su]))

;
;{
;  "device_id" : string,
;  "created_on": x,
;  "isleader": "False",
;  "os":  string,
;  "arch": string,
;  "cpu_manufacturer": string,
;  "physical_cpu_cores": int,
;  "logical_cpu_cores": int,
;  "cpu_clock_speed": string,
;  "RAM_size_in_MB": long,
;  "Storage_size_in_MB": long,
;  "power_plugged_information": boolean,
;  "networking_standards": string,
;  "ethernet_address": string,
;  "wifi_address": string
;}
;

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
(s/def :cimi.device/powerPlugged boolean?)
(s/def :cimi.device/agentType ::cimi-core/nonblank-string)
(s/def :cimi.device/actuatorInfo ::cimi-core/nonblank-string)
(s/def :cimi.device/sensorInfo ::cimi-core/nonblank-string)
(s/def :cimi.device/sensorModel ::cimi-core/nonblank-string)
(s/def :cimi.device/sensorConnection ::cimi-core/nonblank-string)
(s/def :cimi.device/networkingStandards ::cimi-core/nonblank-string)
(s/def :cimi.device/ethernetAddress ::cimi-core/nonblank-string)
(s/def :cimi.device/wifiAddress ::cimi-core/nonblank-string)
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
								:cimi.device/powerPlugged
								:cimi.device/agentType
								:cimi.device/actuatorInfo
                :cimi.device/sensorInfo
                :cimi.device/sensorModel
                :cimi.device/sensorConnection
								:cimi.device/networkingStandards
								:cimi.device/ethernetAddress
								:cimi.device/wifiAddress]
						:opt-un [:cimi.device/hwloc
								:cimi.device/cpuinfo]}))

