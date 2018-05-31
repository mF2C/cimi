(ns com.sixsq.slipstream.db.dataclay.binding
  (:require
    [clojure.core.async :refer [<!!]]
    [clojure.tools.logging :as log]
    [com.sixsq.slipstream.db.binding :refer [Binding]]
    [kvlt.chan :as kvlt])
  (:import
    (java.io Closeable)))


(defn send-command
  [url command]
  (let [{:keys [status body]} (<!! (kvlt/request! {:url    url
                                                   :method get
                                                   :body   ^:kvlt.body/edn command
                                                   :as     :edn}))]
    (log/debugf "status %s\n\n%s\n\n%s" status command body)
    body))


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
