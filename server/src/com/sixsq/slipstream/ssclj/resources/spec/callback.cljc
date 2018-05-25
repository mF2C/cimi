(ns com.sixsq.slipstream.ssclj.resources.spec.callback
  (:require 
    [com.sixsq.slipstream.ssclj.resources.spec.common :as cimi-common] 
    [com.sixsq.slipstream.ssclj.resources.spec.core :as cimi-core] 
    [clojure.spec.alpha :as s]
    [clojure.spec.gen.alpha :as gen]
    [clojure.string :as str]
    [com.sixsq.slipstream.ssclj.util.spec :as su]
    [com.sixsq.slipstream.ssclj.resources.spec.session-template]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))

(s/def :cimi.callback/action ::cimi-core/nonblank-string)
(s/def :cimi.callback/state #{"WAITING" "FAILED" "SUCCEEDED"})
(s/def :cimi.callback/targetResource ::cimi-common/resource-link)
(s/def :cimi.callback/data (su/constrained-map keyword? any?))
(s/def :cimi.callback/expires ::cimi-core/timestamp)

(s/def :cimi/callback
  (su/only-keys-maps c/common-attrs
                     {:req-un [:cimi.callback/action
                               :cimi.callback/state
                               :cimi.callback/targetResource]
                      :opt-un [:cimi.callback/data
                               :cimi.callback/expires]}))
