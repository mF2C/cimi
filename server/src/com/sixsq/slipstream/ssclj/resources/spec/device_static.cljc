(ns com.sixsq.slipstream.ssclj.resources.spec.device_static
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.util.spec :as su]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))


(s/def :cimi.device_static/device_id #{})   			'''Getting input from Id table'''
(s/def :cimi.device_static/created_on #{})
(s/def :cimi.device_static/isLeader {"False"})			'''Initially the Leader information should remain as False'''
(s/def :cimi.device_static/os #{})
(s/def :cimi.device_static/cpu #{})
(s/def :cimi.device_static/memory #{})
(s/def :cimi.device_static/storage #{})
(s/def :cimi.device_static/power_plugged_information? boolean?)
(s/def :cimi.device_static/networking_standards #{})
(s/def :cimi.device_static/ethernet_address #{})
(s/def :cimi.device_static/wifi_address #{})
(s/def :cimi.device_static/category (su/only-keys :req-un [:cimi.device_static/device_id
						     :cimi.device_static/created_on
						     :cimi.device_static/isLeader
						     :cimi.device_static/os
						     :cimi.device_static/cpu
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
                         :cimi.device/category]
                :opt-un [:cimi.common/created
                         :cimi.common/updated
                         :cimi.common/name
                         :cimi.common/description
                         :cimi.common/properties
                         :cimi.common/operations]))
