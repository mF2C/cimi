(ns com.sixsq.slipstream.ssclj.resources.spec.fog-area
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.util.spec :as su]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))

; Before publishing the fog area's information, all the agent's information of the corresponding fog area will be retrieve to the Leader agent local device
; Then making some simple aggregation and publish the total capacity of a fog area as following- 
;{
;  "fog-area_id": x
;  "device_id" : string, 
;  "created_on": x,
;  "updated_on": x,
;  "number_of_devices": int,
;  "total_RAM_in_MBs": long,
;  "top_device_RAM_in_MBs": long,
;  "lowest_device_RAM_in_MBs": long,
;  "total_storage_storage_in_MBs": long,
;  "top_device_storage_in_MBs": long,
;  "lowest_device_storage_in_MBs": long,
;  "avg_processing_capacity_in_percentage": float,
;  "top_device_CPU_in_percentage": float,
;  "lowest_device_CPU_in_percentage": float,
;  "avg_no_of_physical_CPU_cores": int,
;  "top_device_CPU_physical_CPU_cores": int,
;  "lowest_device_CPU_physical_CPU_cores": int,
;  "avg_no_of_logical_CPU_cores": int,
;  "top_device_CPU_logical_CPU_cores": int,
;  "lowest_device_CPU_logical_CPU_cores": int,
;  "top_device_with_highest_power_remaining_in_seconds": string,
;  "lowest_device_with_lowest_power_remaining_in_seconds": string,
;  "inclinometer": boolean,
;  "temperature": boolean,
;  "jammer": boolean,
;  "location": boolean,
;  "ambulance": boolean,
;  "fire_car": boolean,
;  "traffic_light": boolean,
;  "street_light": boolean
;}
;


(s/def :cimi.fog-area/leaderDevice :cimi.common/resource-link)   	   
(s/def :cimi.fog-area/numDevices pos-int?)     
(s/def :cimi.fog-area/ramUnits :cimi.core/nonblank-string)	    
(s/def :cimi.fog-area/ramTotal pos-int?)
(s/def :cimi.fog-area/ramMax pos-int?)
(s/def :cimi.fog-area/ramMin pos-int?)
(s/def :cimi.fog-area/storageUnits :cimi.core/nonblank-string)		  
(s/def :cimi.fog-area/storageTotal pos-int?)
(s/def :cimi.fog-area/storageMax pos-int?)
(s/def :cimi.fog-area/storageMin pos-int?)
(s/def :cimi.fog-area/avgProcessingCapacityPercent float?)
(s/def :cimi.fog-area/cpuMaxPercent float?)
(s/def :cimi.fog-area/cpuMinPercent float?)
(s/def :cimi.fog-area/avgPhysicalCores pos-int?)
(s/def :cimi.fog-area/physicalCoresMax pos-int?)
(s/def :cimi.fog-area/physicalCoresMin pos-int?)
(s/def :cimi.fog-area/avgLogicalCores pos-int?)
(s/def :cimi.fog-area/logicalCoresMax pos-int?)
(s/def :cimi.fog-area/logicalCoresMin pos-int?)
(s/def :cimi.fog-area/powerRemainingMax :cimi.core/nonblank-string)
(s/def :cimi.fog-area/powerRemainingMin :cimi.core/nonblank-string)

(s/def :cimi/fog-area
	(su/only-keys-maps c/common-attrs
					   {:req-un [:cimi.device-dynamic/leaderDevice
								:cimi.fog-area/numDevices
								:cimi.fog-area/ramUnits
								:cimi.fog-area/ramTotal
								:cimi.fog-area/ramMax
								:cimi.fog-area/ramMin
								:cimi.fog-area/storageUnits
								:cimi.fog-area/storageTotal
								:cimi.fog-area/storageMax
								:cimi.fog-area/storageMin
								:cimi.fog-area/avgProcessingCapacityPercent
								:cimi.fog-area/cpuMaxPercent
								:cimi.fog-area/cpuMinPercent
								:cimi.fog-area/avgPhysicalCores
                :cimi.fog-area/physicalCoresMax
                :cimi.fog-area/physicalCoresMin
                :cimi.fog-area/avgLogicalCores
                :cimi.fog-area/logicalCoresMax
                :cimi.fog-area/logicalCoresMin					     
                :cimi.fog-area/powerRemainingMax
                :cimi.fog-area/powerRemainingMin]
						:opt-un []}))
