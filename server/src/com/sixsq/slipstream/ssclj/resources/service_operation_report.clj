(ns
  com.sixsq.slipstream.ssclj.resources.service-operation-report
  (:require
    [com.sixsq.slipstream.auth.acl :as a]
    [com.sixsq.slipstream.ssclj.resources.common.crud :as crud]
    [com.sixsq.slipstream.ssclj.resources.common.schema :as c]
    [com.sixsq.slipstream.ssclj.resources.common.std-crud :as std-crud]
    [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
    [com.sixsq.slipstream.ssclj.resources.spec.service-operation-report]
    [superstring.core :as str]))

(def ^:const resource-name "ServiceOperationReport")
(def ^:const resource-tag (keyword (str (str/camel-case resource-name) "s")))
(def ^:const resource-url (u/de-camelcase resource-name))
(def ^:const collection-name "ServiceOperationReportCollection")

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

(def validate-fn (u/create-spec-validation-fn :cimi/service-operation-report))
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
;; initialization
;;
(defn initialize
  []
  (std-crud/initialize resource-url :cimi/service-operation-report))
