(ns com.sixsq.slipstream.db.dataclay.loader
  (:refer-clojure :exclude [load])
  (:require
    [clojure.core.async :refer [<!!]]
    [clojure.tools.logging :as log]
    [com.sixsq.slipstream.db.dataclay.binding :as dataclay]
    [environ.core :as env]
    [kvlt.chan :as kvlt]))


(defn send-command
  [url command]
  (log/debugf "url: '%s'; command: '%s'" url command)
  (let [{:keys [status body]} (-> {:url    url
                                   :method :get
                                   :body   (prn-str command)
                                   :as     :edn}
                                  kvlt/request!
                                  <!!)]
    (log/debugf "status %s\n\n%s\n\n%s" status command body)
    body))


(defn load
  "Creates a dataClay database binding. Takes the configuration parameters
   from the environmental variables DC_HOST and DC_PORT. These default to
   'localhost' and '6472' if not specified."
  []
  (let [host (env/env :dc-host "localhost")
        port (env/env :dc-port "6472")
        url (format "http://%s:%s/" host port)
        send-fn (partial send-command url)]
    (dataclay/->DataClayBinding send-fn)))
