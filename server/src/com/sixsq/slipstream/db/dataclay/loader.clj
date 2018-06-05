(ns com.sixsq.slipstream.db.dataclay.loader
  (:refer-clojure :exclude [load])
  (:require
    [com.sixsq.slipstream.db.dataclay.binding :as dataclay]
    [environ.core :as env]))


(defn load
  "Creates a dataClay database binding. Takes the configuration parameters
   from the environmental variables DC_HOST and DC_PORT. These default to
   'localhost' and '6472' if not specified."
  []
  (let [host (env/env :dc-host "localhost")
        port (env/env :dc-port "6472")
        url (format "http://%s:%s/" host port)
        send-fn (partial dataclay/send-command url)]
    (dataclay/->DataClayBinding send-fn)))
