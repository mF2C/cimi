(ns com.sixsq.slipstream.db.dataclay.loader
  (:refer-clojure :exclude [load])
  (:require
    [com.sixsq.slipstream.db.dataclay.binding :as dataclay]))


(defn load
  "Creates an dataClay database binding. This implementation takes no
   parameters from the environment."
  []
  (dataclay/->DataClayBinding))
