(ns com.sixsq.slipstream.db.dataclay.loader-direct
  (:refer-clojure :exclude [load])
  (:require
    [clojure.edn :as edn]
    [clojure.tools.logging :as log]
    [com.sixsq.dataclay.handler :as proxy]
    [com.sixsq.slipstream.db.dataclay.binding :as dataclay]))


(defn send-fn
  [argv]
  (log/info "dataClay direct binding argv:" argv)
  (let [wrapped-response (some-> argv
                                 proxy/scrud-action
                                 :body)]
    (log/info "dataClay direct binding wrapped response:" wrapped-response)
    (log/info "dataClay direct binding wrapped response:" (string? wrapped-response))
    (if (string? wrapped-response)
      (edn/read-string wrapped-response)
      wrapped-response)))


(defn load
  "Creates a dataClay database binding that directly accesses the database."
  []
  (dataclay/->DataClayBinding send-fn))
