(ns
  com.sixsq.slipstream.ssclj.resources.qos-model
  (:require
    [clojure.pprint :as pp]
    [com.sixsq.slipstream.auth.acl :as a]
    [com.sixsq.slipstream.ssclj.resources.common.crud :as crud]
    [com.sixsq.slipstream.ssclj.resources.common.schema :as c]
    [com.sixsq.slipstream.ssclj.resources.common.std-crud :as std-crud]
    [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
    [com.sixsq.slipstream.ssclj.resources.spec.qos_model]
    [com.sixsq.slipstream.ssclj.util.log :as log-util]
    [com.sixsq.slipstream.util.response :as r]))

(def ^:const resource-tag :qos-models)
(def ^:const resource-name "QosModel")
(def ^:const resource-url (u/de-camelcase resource-name))
(def ^:const collection-name "QosModelCollection")

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

(def validate-fn (u/create-spec-validation-fn :cimi/qos-model))
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

(def delete-impl (std-crud/delete-fn resource-name))

(defmethod crud/delete resource-name
  [request]
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
;; actions
;;

(def dispatch-on-action :action)

(defmulti execute dispatch-on-action)

(defmethod execute :default
  [{:keys [id] :as resource}]
  (pp/pprint id)
  ; (utils/callback-failed! id)
  (log-util/log-and-throw 400 (str "error executing service: '" (dispatch-on-action resource) "'")))


(defmethod crud/do-action [resource-url "execute"]
  [{{uuid :uuid} :params :as request}]
  ; (try
  (let [id (str resource-url "/" uuid)]
    (when-let [callback (crud/retrieve-by-id id {:user-name "INTERNAL", :user-roles ["ADMIN"]})]
      ; (if (utils/executable? callback)
      (execute callback))))
; (r/map-response "cannot re-execute callback" 409 id)))
; (catch ExceptionInfo ei
; (ex-data ei))))

;;
;; initialization
;;
(defn initialize
  []
  (std-crud/initialize resource-url :cimi/qos-model))
