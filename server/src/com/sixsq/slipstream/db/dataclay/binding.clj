(ns com.sixsq.slipstream.db.dataclay.binding
  (:require
    [com.sixsq.slipstream.db.binding :refer [Binding]])
  (:import
    (java.io Closeable)))


(deftype DataClayBinding
  [send-fn]

  Binding

  (initialize [_ collection-id options]
    nil)


  (add [_ data options]
    (send-fn [:add data options]))


  (add [_ _ data options]
    (send-fn [:add data options]))


  (retrieve [_ id options]
    (send-fn [:retrieve id options]))


  (delete [_ data options]
    (send-fn [:delete data options]))


  (edit [_ data options]
    (send-fn [:edit data options]))


  (query [_ collection-id options]
    (send-fn [:query collection-id options]))


  Closeable
  (close [_]
    nil))
