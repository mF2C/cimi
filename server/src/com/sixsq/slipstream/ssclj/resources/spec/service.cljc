(ns com.sixsq.slipstream.ssclj.resources.spec.service
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.util.spec :as su]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))


(s/def :cimi.service/cpu #{"high" "medium" "low"})
(s/def :cimi.service/memory #{"high" "medium" "low"})
(s/def :cimi.service/storage #{"high" "medium" "low"})
(s/def :cimi.service/inclinometer? boolean?)
(s/def :cimi.service/temperature? boolean?)
(s/def :cimi.service/jammer? boolean?)
(s/def :cimi.service/location? boolean?)
(s/def :cimi.service/category (su/only-keys :req-un [:cimi.service/cpu
                                                     :cimi.service/memory
                                                     :cimi.service/storage
                                                     :cimi.service/inclinometer
                                                     :cimi.service/temperature
                                                     :cimi.service/jammer
                                                     :cimi.servive/location]))
(s/def :cimi.service/exec :cimi.core/nonblank-string)
(s/def :cimi.service/exec_type :cimi.core/nonblank-string)
(s/def :cimi/service
  (su/only-keys :req-un [:cimi.common/id
                         :cimi.common/resourceURI
                         :cimi.service/category
                         :cimi.common/created
                         :cimi.common/updated
                         :cimi.common/acl
                         :cimi.service/exec
                         :cimi.service/exec_type]
                :opt-un [:cimi.common/name
                         :cimi.common/description
                         :cimi.common/properties
                         :cimi.common/operations]))
