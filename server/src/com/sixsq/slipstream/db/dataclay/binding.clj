(ns com.sixsq.slipstream.db.dataclay.binding
  (:require
    [com.sixsq.slipstream.db.binding :refer [Binding]]
    [com.sixsq.slipstream.db.utils.acl :as acl-utils]
    [com.sixsq.slipstream.db.utils.common :as cu]
    [com.sixsq.slipstream.util.response :as response]
    [clojure.string :as str])
  (:import
    (java.io Closeable)
    (api DataClayWrapper)))


(deftype DataClayBinding
  []

  Binding

  (initialize [_ collection-id options]
    nil)


  (add [_ {:keys [id] :as data} options]
    (let [[type uuid] (str/split id #"/")]
      (DataClayWrapper/create type uuid data)))


  (add [_ _ {:keys [id] :as data} options]
    (let [[type uuid] (str/split id #"/")]
      (DataClayWrapper/create type uuid data)))


  (retrieve [_ id options]
    (let [[type uuid] (str/split id #"/")]
      (DataClayWrapper/read type uuid)))


  (delete [_ {:keys [id] :as data} options]
    (let [[type uuid] (str/split id #"/")]
      (DataClayWrapper/delete type uuid)))


  (edit [_ {:keys [id] :as data} options]
    (let [[type uuid] (str/split id #"/")]
      (DataClayWrapper/update type uuid data)))


  (query [_ collection-id {:keys [filter user-name user-roles] :as options}]
    (DataClayWrapper/query collection-id filter user-name user-roles))


  Closeable
  (close [_]
    nil))
