(ns
  com.sixsq.slipstream.ssclj.resources.device
  (:require
    [com.sixsq.slipstream.auth.acl :as a]
    [com.sixsq.slipstream.ssclj.resources.common.crud :as crud]
    [com.sixsq.slipstream.ssclj.resources.common.schema :as c]        
    [com.sixsq.slipstream.ssclj.resources.common.std-crud :as std-crud]
    [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
    [com.sixsq.slipstream.ssclj.resources.spec.device]
    [com.sixsq.slipstream.util.response :as r]
    [clj-time.core :as time]
    [com.sixsq.slipstream.ssclj.resources.event :as event]  
    [superstring.core :as str]))

(def ^:const resource-name "Device")
(def ^:const resource-tag (keyword (str (str/camel-case resource-name) "s")))
(def ^:const resource-url (u/de-camelcase resource-name))
(def ^:const collection-name "DeviceCollection")

(def ^:const resource-uri (str c/cimi-schema-uri resource-name))
(def ^:const collection-uri (str c/cimi-schema-uri collection-name))

(def collection-acl {:owner {:principal "ADMIN"
                             :type      "ROLE"}
                     :rules [{:principal "USER"
                              :type      "ROLE"
                              :right     "MODIFY"}]})

(defmethod crud/add-acl resource-uri
  [resource request]
  (a/add-acl resource request))
;;
;; "Implementations" of multimethod declared in crud namespace
;;

(def validate-fn (u/create-spec-validation-fn :cimi/device))
(defmethod crud/validate
  resource-uri
  [resource]
  (validate-fn resource))

(def add-impl (std-crud/add-fn resource-name collection-acl resource-uri))

(defmethod crud/add resource-name
  [request]
  (add-impl request))

(def retrieve-impl (std-crud/retrieve-fn resource-name))

(defmethod crud/retrieve resource-name
  [request]
  (retrieve-impl request))


(defn create-event [deviceID type acl]
  (let [event-request {:params   {:resource-name event/resource-url}
                          :body     {:type         type
                                      :content {:resource {:href deviceID} :state "DELETED"}
                                      :severity "low"
                                      :timestamp (u/unparse-timestamp-datetime (time/now))
                                      :acl acl}
                          :identity {:current         "INTERNAL"
                                      :authentications {"INTERNAL" {:identity "INTERNAL"
                                                                    :roles    ["ADMIN"]}}}}
        {{:keys [resource-id]} :body status :status} (crud/add event-request)]
    ; (if (!= 201 status)
    ;   (let [msg "cannot create event"]
    ;     (throw (ex-info msg (r/map-response msg 500 deviceID)))))
        ))


(def delete-impl (std-crud/delete-fn resource-name))

(defmethod crud/delete resource-name
  [request]
  (create-event (clojure.string/replace (:uri request) #"/api/" "") "action" {:owner {:type "USER" :principal "joe"}
    :rules [{:type "ROLE" :principal "ANON" :right "ALL"}]})
  (delete-impl request))

;;
;; available operations
;;
; (defmethod crud/set-operations resource-uri
;   [resource request]
;   (try
;     (a/can-modify? resource request)
;     (let [href (:id resource)
;           ^String resourceURI (:resourceURI resource)
;           ops (if (.endsWith resourceURI "Collection")
;                 [{:rel (:add c/action-uri) :href href}]
;                 [{:rel (:delete c/action-uri) :href href}])]
;       (assoc resource :operations ops))
;     (catch Exception e
;       (dissoc resource :operations))))

(def edit-impl (std-crud/edit-fn resource-name))
(defmethod crud/edit resource-name
  [request]
  (edit-impl request))

;;
;; collection
;;
; (def query-impl (std-crud/query-fn resource-name collection-acl collection-uri resource-tag))
; (defmethod crud/query resource-name
;   [request]
;   (query-impl (update-in request [:cimi-params] #(assoc % :orderby [["timestamp" :desc]]))))

(def query-impl (std-crud/query-fn resource-name collection-acl collection-uri resource-tag))

(defmethod crud/query resource-name
  [request]
  (query-impl request))

;;
;; initialization
;;
(defn initialize
  []
  (std-crud/initialize resource-url :cimi/device))