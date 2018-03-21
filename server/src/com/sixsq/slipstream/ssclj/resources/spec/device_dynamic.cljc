(ns com.sixsq.slipstream.ssclj.resources.spec.device_dynamic
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.util.spec :as su]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))


(s/def :cimi.device_dynamic/device_id #{URI})   			'''Getting input from Id table'''
(s/def :cimi.device_dynamic/isLeader? #{URI})			'''Getting input from Leader information table'''
(s/def :cimi.device_dynamic/updated_on #{dateTime})
(s/def :cimi.device_dynamic/available_RAM_size_in_MBs #{Long})
(s/def :cimi.device_dynamic/available_RAM_in_percentage #{Float})
(s/def :cimi.device_dynamic/available_Storage_size_in_MBs #{Long})
(s/def :cimi.device_dynamic/available_Storage_in_percentage #{Float})
(s/def :cimi.device_dynamic/power_remaining_status #{String})
(s/def :cimi.device_dynamic/remaining_power_info_in_seconds #{String})
(s/def :cimi.device_dynamic/ethernet_address #{String})
(s/def :cimi.device_dynamic/wifi_address #{String})
(s/def :cimi.device_dynamic/throughput_info_ethernet #{String})
(s/def :cimi.device_dynamic/throughput_info_wifi #{String})
(s/def :cimi.device_dynamic/inclinometer? boolean?)
(s/def :cimi.device_dynamic/temperature? boolean?)
(s/def :cimi.device_dynamic/jammer? boolean?)
(s/def :cimi.device_dynamic/location? boolean?)
(s/def :cimi.device_dynamic/ambulance? boolean?)
(s/def :cimi.device_dynamic/fire_car? boolean?)
(s/def :cimi.device_dynamic/traffic_light? boolean?)
(s/def :cimi.device_dynamic/street_light? boolean?)
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
