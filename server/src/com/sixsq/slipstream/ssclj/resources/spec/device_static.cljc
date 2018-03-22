(ns com.sixsq.slipstream.ssclj.resources.spec.device_static
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.util.spec :as su]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))


(s/def :cimi.device_static/device_id pos-string?) ; device_id   			
(s/def :cimi.device_static/created_on pos-date?)
(s/def :cimi.device_static/isLeader {"False"})			
(s/def :cimi.device_static/os pos-string?)
(s/def :cimi.device_static/cpu_manufacturer pos-string?)
(s/def :cimi.device_static/cpu_physical_core pos-int?)
(s/def :cimi.device_static/cpu_logical_core pos-int?)
(s/def :cimi.device_static/memory pos-long?)
(s/def :cimi.device_static/storage pos-long?)
(s/def :cimi.device_static/power_plugged_information? boolean?)
(s/def :cimi.device_static/networking_standards pos-string?)
(s/def :cimi.device_static/ethernet_address pos-string?)
(s/def :cimi.device_static/wifi_address pos-string?)
(s/def :cimi.device_static/category (su/only-keys :req-un [:cimi.device_static/device_id
						     :cimi.device_static/created_on
						     :cimi.device_static/isLeader
						     :cimi.device_static/os
						     :cimi.device_static/cpu_manufacturer
						     :cimi.device_static/cpu_physical_core
						     :cimi.device_static/cpu_logical_core
                                                     :cimi.device_static/memory
                                                     :cimi.device_static/storage
						     :cimi.device_static/power_plugged_information
						     :cimi.device_static/networking_standards
						     :cimi.device_static/ethernet_address
						     :cimi.device_static/ethernet_address]))
(s/def :cimi/device_static
  (su/only-keys :req-un [:cimi.common/id
                         :cimi.common/resourceURI
                         :cimi.common/acl
			 :cimi.common/created
                         :cimi.common/updated
                         :cimi.device/category]
                :opt-un [:cimi.common/name
                         :cimi.common/description
                         :cimi.common/properties
                         :cimi.common/operations]))
