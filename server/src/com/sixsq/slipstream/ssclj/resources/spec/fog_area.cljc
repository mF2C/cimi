(ns com.sixsq.slipstream.ssclj.resources.spec.fog_area
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.util.spec :as su]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))

; Before publishing the fog area's information, all the agent's information of the corresponding fog area will be retrieve to the Leader agent 
; Then making some simple aggregation and publish the total capacity of a fog area 



(s/def :cimi.fog_area/id :cimi.core/nonblank-string)               ; Fog-area's ID 
(s/def :cimi.fog_area/device_id :cimi.core/nonblank-string)   	   ; Leader's ID		
(s/def :cimi.fog_area/created_on pos-date?)
(s/def :cimi.fog_area/updated_on pos-dateTime?)
(s/def :cimi.fog_area/number_of_devices pos-int?)           
(s/def :cimi.fog_area/total_RAM_in_MBs pos-long?)
(s/def :cimi.fog_area/top_device_RAM_in_MBs pos-long?)
(s/def :cimi.fog_area/lowest_device_RAM_in_MBs pos-long?)
(s/def :cimi.fog_area/total_storage_storage_in_MBs pos-long?)
(s/def :cimi.fog_area/top_device_storage_in_MBs pos-long?)
(s/def :cimi.fog_area/lowest_device_storage_in_MBs pos-long?)
(s/def :cimi.fog_area/avg_processing_capacity_in_percentage pos-float?)
(s/def :cimi.fog_area/top_device_CPU_in_percentage pos-float?)
(s/def :cimi.fog_area/lowest_device_CPU_in_percentage pos-float?)
(s/def :cimi.fog_area/avg_no_of_physical_CPU_cores pos-int?)
(s/def :cimi.fog_area/top_device_CPU_physical_CPU_cores pos-int?)
(s/def :cimi.fog_area/lowest_device_CPU_physical_CPU_cores pos-int?)
(s/def :cimi.fog_area/avg_no_of_logical_CPU_cores pos-int?)
(s/def :cimi.fog_area/top_device_CPU_logical_CPU_cores pos-int?)
(s/def :cimi.fog_area/lowest_device_CPU_logical_CPU_cores pos-int?)
(s/def :cimi.fog_area/avg_time_of_power_remaining_in_seconds pos-int?)
(s/def :cimi.fog_area/top_device_with_highest_power_remaining_in_seconds pos-string?)
(s/def :cimi.fog_area/lowest_device_with_lowest_power_remaining_in_seconds pos-string?)
(s/def :cimi.fog_area/inclinometer boolean?)
(s/def :cimi.fog_area/temperature boolean?)
(s/def :cimi.fog_area/jammer boolean?)
(s/def :cimi.fog_area/location boolean?)
(s/def :cimi.fog_area/ambulance boolean?)
(s/def :cimi.fog_area/fire_car boolean?)
(s/def :cimi.fog_area/traffic_light boolean?)
(s/def :cimi.fog_area/street_light boolean?)
(s/def :cimi.fog_area/category (su/only-keys :req-un [:cimi.fog_area/id
						     :cimi.fog_area/device_id
                 :cimi.fog_area/created_on
						     :cimi.fog_area/updated_on
						     :cimi.fog_area/number_of_devices
                 :cimi.fog_area/total_RAM_in_MBs
                 :cimi.fog_area/top_device_RAM_in_MBs
                 :cimi.fog_area/lowest_device_RAM_in_MBs
                 :cimi.fog_area/total_storage_storage_in_MBs
	         :cimi.fog_area/top_device_storage_in_MBs
		 :cimi.fog_area/lowest_device_storage_in_MBs
                 :cimi.fog_area/avg_processing_capacity_in_percentage
                 :cimi.fog_area/top_device_CPU_in_percentage
                 :cimi.fog_area/lowest_device_CPU_in_percentage
                 :cimi.fog_area/avg_no_of_physical_CPU_cores
                 :cimi.fog_area/top_device_CPU_physical_CPU_cores
                 :cimi.fog_area/lowest_device_CPU_physical_CPU_cores
                 :cimi.fog_area/avg_no_of_logical_CPU_cores
                 :cimi.fog_area/top_device_CPU_logical_CPU_cores
                 :cimi.fog_area/lowest_device_CPU_logical_CPU_cores
                 :cimi.fog_area/avg_time_of_power_remaining_in_seconds
                 :cimi.fog_area/top_device_with_highest_power_remaining_in_seconds
                 :cimi.fog_area/lowest_device_with_lowest_power_remaining_in_seconds
                 :cimi.fog_area/inclinometer
                 :cimi.fog_area/temperature
                 :cimi.fog_area/jammer
                 :cimi.fog_area/location
                 :cimi.fog_area/ambulance
                 :cimi.fog_area/fire_car
                 :cimi.fog_area/traffic_light
                 :cimi.fog_area/street_light]))
(s/def :cimi/fog_area
  (su/only-keys :req-un [:cimi.common/id
                         :cimi.common/resourceURI
                         :cimi.common/acl
                         :cimi.device/category
			 :cimi.common/created
                         :cimi.common/updated]
                
                :opt-un [:cimi.common/name
                         :cimi.common/description
                         :cimi.common/properties
                         :cimi.common/operations]))
