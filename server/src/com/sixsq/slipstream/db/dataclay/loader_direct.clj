(ns com.sixsq.slipstream.db.dataclay.loader-direct
  (:refer-clojure :exclude [load])
  (:require
    [com.sixsq.dataclay.handler :as proxy]
    [com.sixsq.slipstream.db.dataclay.binding :as dataclay]
    [clojure.tools.logging :as log]))


(defn send-fn
  [argv]
  (log/info "dataClay direct binding argv:" argv)
  (let [response (proxy/scrud-action argv)
        response-body (:body response)]
    (log/info "dataClay direct binding scrud response:" response)
    (log/info "dataClay direct binding scrud body:" response-body)
    response-body))


(defn load
  "Creates a dataClay database binding that directly accesses the database."
  []
  (dataclay/->DataClayBinding send-fn))
