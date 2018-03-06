(ns
  ^{:copyright "Copyright 2018, SixSq Sàrl"
    :license   "http://www.apache.org/licenses/LICENSE-2.0"}
  com.sixsq.slipstream.ssclj.resources.spec.example-resource
  (:require
    [clojure.spec.alpha :as s]
    [com.sixsq.slipstream.ssclj.util.spec :as su]
    [com.sixsq.slipstream.ssclj.resources.spec.session-template]
    [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))

(s/def :cimi.example-resource/action :cimi.core/nonblank-string)
(s/def :cimi.example-resource/state #{"WAITING" "FAILED" "SUCCEEDED"})
(s/def :cimi.example-resource/resource :cimi.common/resource-link)
(s/def :cimi.example-resource/data (su/constrained-map keyword? any?))
(s/def :cimi.example-resource/expires :cimi.core/timestamp)
(s/def :cimi.example-resource/counter nat-int?)

(s/def :cimi/example-resource
  (su/only-keys-maps c/common-attrs
                     {:req-un [:cimi.example-resource/action
                               :cimi.example-resource/state
                               :cimi.example-resource/resource
                               :cimi.example-resource/counter]
                      :opt-un [:cimi.example-resource/data
                               :cimi.example-resource/expires]}))
