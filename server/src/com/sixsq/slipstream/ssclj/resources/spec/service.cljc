(ns com.sixsq.slipstream.ssclj.resources.spec.service
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as cimi-common]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core]
    [com.sixsq.slipstream.ssclj.util.spec :as su]))

(s/def :cimi.service/exec ::cimi-core/nonblank-string)
(s/def :cimi.service/exec_type #{"docker" "compss" "docker-compose" "docker-swarm" "kubernetes"})
(s/def :cimi.service/exec_ports (s/coll-of ::cimi-core/port))
(s/def :cimi.service/agent_type #{"cloud" "normal" "micro"})
(s/def :cimi.service/sla_templates (s/coll-of ::cimi-common/resource-link))
(s/def :cimi.service/num_agents nat-int?)
(s/def :cimi.service/cpu_arch #{"arm" "x86-64"})
(s/def :cimi.service/os #{"linux" "mac" "windows" "iOS" "android"})
(s/def :cimi.service/cpu nat-int?)
(s/def :cimi.service/memory nat-int?)
(s/def :cimi.service/disk nat-int?)
(s/def :cimi.service/network nat-int?)
(s/def :cimi.service/storage_min nat-int?)
(s/def :cimi.service/req_resource (s/coll-of string?))
(s/def :cimi.service/opt_resource (s/coll-of string?))
(s/def :cimi.service/category nat-int?)

(s/def :cimi/service
  (su/only-keys :req-un [::cimi-common/id
                         ::cimi-common/resourceURI
                         ::cimi-common/created
                         ::cimi-common/updated
                         ::cimi-common/acl
                         ::cimi-common/name
                         :cimi.service/exec
                         :cimi.service/exec_type
                         :cimi.service/agent_type]
                :opt-un [::cimi-common/description
                         ::cimi-common/properties
                         ::cimi-common/operations
                         :cimi.service/sla_templates
                         :cimi.service/exec_ports
                         :cimi.service/num_agents
                         :cimi.service/cpu_arch
                         :cimi.service/os
                         :cimi.service/cpu
                         :cimi.service/memory
                         :cimi.service/disk
                         :cimi.service/network
                         :cimi.service/storage_min
                         :cimi.service/req_resource
                         :cimi.service/opt_resource
                         :cimi.service/category
                         ]))
