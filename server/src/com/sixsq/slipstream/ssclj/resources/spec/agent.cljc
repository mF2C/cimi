(ns com.sixsq.slipstream.ssclj.resources.spec.agent
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as cimi-common]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core]
    [com.sixsq.slipstream.ssclj.util.spec :as su]))

;   {
;     "device_id": string,
;     "device_ip": string,
;     "leader_id": string,
;     "leader_ip": string,
;     "authenticated": boolean,
;     "connected": boolean,
;     "isLeader": boolean,
;     "backup_ip": string,
;     "childrenIPs": string (List)
;   }



(s/def :cimi.agent/device_id ::cimi-core/nonblank-string) ; device_id
(s/def :cimi.agent/device_ip string?)
(s/def :cimi.agent/leader_id string?)
(s/def :cimi.agent/leader_ip string?)
(s/def :cimi.agent/authenticated boolean?)
(s/def :cimi.agent/connected boolean?)
(s/def :cimi.agent/isLeader boolean?)
(s/def :cimi.agent/backup_ip string?)
(s/def :cimi.agent/childrenIPs (s/coll-of string?))


(s/def :cimi/agent
       (su/only-keys-maps c/common-attrs
                          {:req-un [:cimi.agent/device_id
                                     :cimi.agent/device_ip
                                     :cimi.agent/authenticated
                                     :cimi.agent/connected
                                     :cimi.agent/isLeader]
                           :opt-un [:cimi.agent/leader_id
                                     :cimi.agent/leader_ip
                                     :cimi.agent/backup_ip
                                     :cimi.agent/childrenIPs]}))