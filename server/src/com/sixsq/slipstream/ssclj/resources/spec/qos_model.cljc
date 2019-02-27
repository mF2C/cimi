(ns com.sixsq.slipstream.ssclj.resources.spec.service
  (:require 
    [clojure.spec.alpha :as s] 
    [com.sixsq.slipstream.ssclj.resources.spec.common :as cimi-common]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core]
    [com.sixsq.slipstream.ssclj.util.spec :as su]))

(s/def :cimi.qos-model/service ::cimi-common/resource-link)
(s/def :cimi.qos-model/agreement ::cimi-common/resource-link)
(s/def :cimi.qos-model/agents (s/coll-of ::cimi-common/resource-link))
(s/def :cimi.qos-model/config ::cimi-core/nonblank-string)
(s/def :cimi.qos-model/num_service_instances nat-int?)
(s/def :cimi.qos-model/num_service_failures nat-int?)
(s/def :cimi.qos-model/state (s/coll-of float?))
(s/def :cimi.qos-model/next_state (s/coll-of float?))


(s/def :cimi/qos-model
  (su/only-keys :req-un [::cimi-common/id
                         ::cimi-common/resourceURI
                         ::cimi-common/created
                         ::cimi-common/updated
                         ::cimi-common/acl
                         :cimi.qos-model/service
                         :cimi.qos-model/agreement
                         :cimi.qos-model/agents
                         :cimi.qos-model/config
                         :cimi.qos-model/num_service_instances
                         :cimi.qos-model/num_service_failures
                         :cimi.qos-model/state
                         :cimi.qos-model/next_state
                         ]))
