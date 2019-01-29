(ns com.sixsq.slipstream.db.dataclay.loader-direct
  (:refer-clojure :exclude [load])
  (:require
    [com.sixsq.dataclay.handler :as proxy]
    [com.sixsq.slipstream.db.dataclay.binding :as dataclay]))


(defn load
  "Creates a dataClay database binding that directly accesses the database."
  []
  (let [send-fn #(-> proxy/scrud-action second)]
    (dataclay/->DataClayBinding send-fn)))
