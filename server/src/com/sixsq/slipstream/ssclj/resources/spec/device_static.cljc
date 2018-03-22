(ns com.sixsq.slipstream.ssclj.resources.spec.device_static
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.util.spec :as su]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))

;
;{
;  "device_id" : string
;  "created_on": "2018-03-22 12:58:07.560509+00:00", (date)
;  "isleader": "False",
;  "os": "Linux-4.13.0-37-generic-x86_64-with-debian-8.10", (string)
;  "arch": "x86_64", (string)
;  "cpu_manufacturer": "Intel(R) Core(TM) i7-8550U CPU @ 1.80GHz", (string)
;  "physical_cpu_cores": 4, (int)
;  "logical_cpu_cores": 8,  (int)
;  "cpu_clock_speed": "1.8000 GHz" (string),
;  "RAM_size_in_MB": 7874.21484375 (long),
;  "Storage_size_in_MB": 234549.5078125 (long),
;  "power_plugged_information": "False" (boolean),
;  "networking_standards": "['enp2s0', 'wlp3s0', 'docker0', 'lo']" (string),
;  "ethernet_address": "[snic(family=<AddressFamily.AF_PACKET: 17>, address='50:9a:4c:cf:f4:b9', netmask=None, broadcast='ff:ff:ff:ff:ff:ff', ptp=None)]" (string),
;  "wifi_address": "[snic(family=<AddressFamily.AF_INET: 2>, address='10.192.167.20', netmask='255.255.0.0', broadcast='10.192.255.255', ptp=None), snic(family=<AddressFamily.AF_INET6: 10>, address='fe80::fe39:ed60:dff6:db85%wlp3s0', netmask='ffff:ffff:ffff:ffff::', broadcast=None, ptp=None), snic(family=<AddressFamily.AF_PACKET: 17>, address='d4:6a:6a:9a:6b:87', netmask=None, broadcast='ff:ff:ff:ff:ff:ff', ptp=None)] (string)"
;}
;

(s/def :cimi.device_static/device_id pos-string?) ; device_id   			
(s/def :cimi.device_static/created_on pos-date?)
(s/def :cimi.device_static/isLeader {"False"})			
(s/def :cimi.device_static/os pos-string?)
(s/def :cimi.device_static/arch pos-string?)
(s/def :cimi.device_static/cpu_manufacturer pos-string?)
(s/def :cimi.device_static/cpu_physical_core pos-int?)
(s/def :cimi.device_static/cpu_logical_core pos-int?)
(s/def :cimi.device_static/cpu_clock_speed pos-string?)
(s/def :cimi.device_static/memory pos-long?)
(s/def :cimi.device_static/storage pos-long?)
(s/def :cimi.device_static/power_plugged_information? boolean?)
(s/def :cimi.device_static/networking_standards pos-string?)
(s/def :cimi.device_static/ethernet_address pos-string?)
(s/def :cimi.device_static/wifi_address pos-string?)

(s/def :cimi.device_static/ 
	(su/only-keys :req-un [:cimi.device_static/device_id
	      		       :cimi.device_static/created_on
			       :cimi.device_static/isLeader
			       :cimi.device_static/os
			       :cimi.device_static/arch
			       :cimi.device_static/cpu_manufacturer
			       :cimi.device_static/cpu_physical_core
			       :cimi.device_static/cpu_logical_core
			       :cimi.device_static/cpu_clock_speed
                               :cimi.device_static/memory
                               :cimi.device_static/storage
			       :cimi.device_static/power_plugged_information
			       :cimi.device_static/networking_standards
			       :cimi.device_static/ethernet_address
			       :cimi.device_static/ethernet_address]))

                :opt-un [:cimi.common/name
                         :cimi.common/description
                         :cimi.common/properties
                         :cimi.common/operations]))
