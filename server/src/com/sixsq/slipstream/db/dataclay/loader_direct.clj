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
  (let [wrapped-response (some-> argv
                                 proxy/scrud-action
                                 edn/read-string
                                 :body)]
    (log/info "dataClay direct binding wrapped response:" wrapped-response)
    wrapped-response))


(defn load
  "Creates a dataClay database binding that directly accesses the database."
  []
  (dataclay/->DataClayBinding send-fn))
