(ns com.sixsq.slipstream.db.dataclay.binding
  (:require
    [clojure.string :as str]
    [com.sixsq.slipstream.db.binding :refer [Binding]]
    [com.sixsq.slipstream.util.response :as response]
    [sixsq.slipstream.client.impl.utils.json :as json])
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
      (DataClayWrapper/create type uuid (json/edn->json data))))


  (add [_ _ {:keys [id] :as data} options]
    (let [[type uuid] (str/split id #"/")]
      (DataClayWrapper/create type uuid (json/edn->json data))))


  (retrieve [_ id options]
    (let [[type uuid] (str/split id #"/")]
      (json/json->edn (DataClayWrapper/read type uuid))))


  (delete [_ {:keys [id] :as data} options]
    (let [[type uuid] (str/split id #"/")]
      (try
        (DataClayWrapper/delete type uuid)
        (response/map-response (format "resource %s deleted" id) 200)
        (catch Exception e
          (response/ex-bad-request (format "resource %s NOT deleted" id))))))


  (edit [_ {:keys [id] :as data} options]
    (let [[type uuid] (str/split id #"/")]
      (try
        (DataClayWrapper/update type uuid data)
        (response/map-response (format "resource %s updated" id) 200)
        (catch Exception e
          (response/ex-bad-request (format "resource %s NOT updated" id))))))


  (query [_ collection-id {:keys [filter user-name user-roles] :as options}]
    (let [results (DataClayWrapper/query collection-id filter user-name user-roles)
          json-results (map json/json->edn results)
          n (count json-results)]
      {:count n
       (keyword (str collection-id "s") json-results)}))


  Closeable
  (close [_]
    nil))
