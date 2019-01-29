(ns com.sixsq.slipstream.db.dataclay.loader-direct
  (:refer-clojure :exclude [load])
  (:require
    [com.sixsq.dataclay.handler :as proxy]
    [com.sixsq.slipstream.db.dataclay.binding :as dataclay]
    [clojure.tools.logging :as log]
    [clojure.edn :as edn]))


(defn send-fn
  [argv]
  (log/info "dataClay direct binding argv:" argv)
  (let [{:keys [status body]} (proxy/scrud-action argv)
        edn (some-> body edn/read-string)]
    (log/info "dataClay direct binding scrud status:" status)
    (log/info "dataClay direct binding scrud body:" edn)
    edn))


(defn load
  "Creates a dataClay database binding that directly accesses the database."
  []
  (dataclay/->DataClayBinding send-fn))
