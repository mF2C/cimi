(ns com.sixsq.slipstream.ssclj.resources.spec.device_dynamic
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.util.spec :as su]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))


(s/def :cimi.device_dynamic/device_id pos-string?)   			
(s/def :cimi.device_dynamic/isLeader :cimi.core/nonblank-boolean)			
(s/def :cimi.device_dynamic/updated_on pos-dateTime?)
(s/def :cimi.device_dynamic/available_RAM_size_in_MBs pos-long?)
(s/def :cimi.device_dynamic/available_RAM_in_percentage pos-float?)
(s/def :cimi.device_dynamic/available_Storage_size_in_MBs pos-long?)
(s/def :cimi.device_dynamic/available_Storage_in_percentage pos-float?)
(s/def :cimi.device_dynamic/power_remaining_status pos-string?)
(s/def :cimi.device_dynamic/remaining_power_info_in_seconds pos-string?)
(s/def :cimi.device_dynamic/ethernet_address pos-string?)
(s/def :cimi.device_dynamic/wifi_address pos-string?)
(s/def :cimi.device_dynamic/throughput_info_ethernet pos-string?)
(s/def :cimi.device_dynamic/throughput_info_wifi pos-string?)
(s/def :cimi.device_dynamic/inclinometer pos-boolean?)
(s/def :cimi.device_dynamic/temperature pos-boolean?)
(s/def :cimi.device_dynamic/jammer pos-boolean?)
(s/def :cimi.device_dynamic/location pos-boolean?)
(s/def :cimi.device_dynamic/ambulance pos-boolean?)
(s/def :cimi.device_dynamic/fire_car pos-boolean?)
(s/def :cimi.device_dynamic/traffic_light pos-boolean?)
(s/def :cimi.device_dynamic/street_light pos-boolean?)
(s/def :cimi.device_dynamic/category (su/only-keys :req-un [:cimi.device_dynamic/device_id
						     :cimi.device_dynamic/isLeader
                 				     :cimi.device_dynamic/created_on
						     :cimi.device_dynamic/available_RAM_size_in_MBs
						     :cimi.device_dynamic/available_RAM_in_percentage
                 				     :cimi.device_dynamic/available_Storage_size_in_MBs
                 				     :cimi.device_dynamic/available_Storage_in_percentage
                 				     :cimi.device_dynamic/power_remaining_status
                 				     :cimi.device_dynamic/remaining_power_info_in_seconds
						     :cimi.device_dynamic/ethernet_address
						     :cimi.device_dynamic/ethernet_address
                 				     :cimi.device_dynamic/throughput_info_ethernet
                 				     :cimi.device_dynamic/throughput_info_wifi
					             :cimi.device_dynamic/inclinometer
                                                     :cimi.device_dynamic/temperature
                                                     :cimi.device_dynamic/jammer
						     :cimi.devive_dynamic/location
						     :cimi.devive_dynamic/ambulance
						     :cimi.devive_dynamic/fire_car
						     :cimi.devive_dynamic/traffic_light
                                                     :cimi.devive_dynamic/street_light]))
(s/def :cimi/device_dynamic
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
