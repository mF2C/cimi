(ns
  ^{:copyright "Copyright 2018, SixSq Sàrl"
    :license   "http://www.apache.org/licenses/LICENSE-2.0"}
  com.sixsq.slipstream.ssclj.resources.example-resource-example
  (:require
    [clojure.tools.logging :as log]
    [com.sixsq.slipstream.ssclj.resources.example-resource :as callback]
    [com.sixsq.slipstream.ssclj.resources.example-resource.utils :as utils]
    [com.sixsq.slipstream.ssclj.util.log :as log-util]
    [com.sixsq.slipstream.util.response :as r]))

(def ^:const action-name "example")

(defmethod callback/execute action-name
  [{{:keys [ok?]} :data id :id :as resource}]
  (if ok?
    (do
      (utils/callback-succeeded! id)
      (log/info (format "executing action %s of %s succeeded" action-name id))
      (r/map-response "success" 200 id))
    (do
      (utils/callback-failed! id)
      (log-util/log-and-throw 400 (format "executing action %s of %s FAILED" action-name id)))))
