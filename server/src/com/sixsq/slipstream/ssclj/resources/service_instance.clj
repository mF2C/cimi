(ns
  com.sixsq.slipstream.ssclj.resources.service-instance
  (:require
    [com.sixsq.slipstream.auth.acl :as a]
    [com.sixsq.slipstream.ssclj.resources.common.crud :as crud]
    [com.sixsq.slipstream.ssclj.resources.common.schema :as c]
    [com.sixsq.slipstream.ssclj.resources.common.std-crud :as std-crud]
    [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
    [clojure.data.json :as json]
    [clj-http.client :as http]
    [com.sixsq.slipstream.ssclj.resources.spec.service-instance]
    [clojure.string :as stringer]
    [superstring.core :as str]))

(def ^:const resource-name "ServiceInstance")
(def ^:const resource-tag (keyword (str (str/camel-case resource-name) "s")))
(def ^:const resource-url (u/de-camelcase resource-name))
(def ^:const collection-name "ServiceInstanceCollection")

(defn ns->type
  [ns]
  (-> ns str (stringer/split #"\.") last))

(def ^:const resource-type (ns->type *ns*))

(def ^:const resource-uri (str c/cimi-schema-uri resource-name))
(def ^:const collection-uri (str c/cimi-schema-uri collection-name))

(def collection-acl {:owner {:principal "ADMIN"
                             :type      "ROLE"}
                     :rules [{:principal "ADMIN"
                              :type      "ROLE"
                              :right     "ALL"}
                             {:principal "USER"
                              :type      "ROLE"
                              :right     "MODIFY"}]})

(defmethod crud/add-acl resource-uri
  [resource request]
  (a/add-acl resource request))




(defn call-sm
  "Returns a tuple with status and message"
  [url body]
  ; (if (and url body)
  ;   (try
  (:service-instance (json/read-str (:body (http/post url
                                             {:headers      {"Accept" "application/json"}
                                              :content-type :json
                                              :accept       :json
                                              :body         (json/write-str body)})) :key-fn keyword)
    )
  ; (catch Exception e
  ;   (ex-data e))  )
  ; [412 "Incomplete"]))
  )


;;
;; "Implementations" of multimethod declared in crud namespace
;;

(def validate-fn (u/create-spec-validation-fn :cimi/service-instance))
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

(def edit-impl (std-crud/edit-fn resource-name))
(defmethod crud/edit resource-name
  [request]
  (edit-impl request))

;;
;; collection
;;

(def query-impl (std-crud/query-fn resource-name collection-acl collection-uri resource-tag))
(defmethod crud/query resource-name
  [request]
  (query-impl request))

;;
;; initialization
;;
(defn initialize
  []
  (std-crud/initialize resource-url :cimi/service-instance))


;;
;;
;; Custom operation
;;

;;
;; Redeem operation
;;


(defn operation-map
  "Provides the operation map for the given href and operation."
  [href op-kw-or-name]
  {:rel  (name op-kw-or-name)
   :href href})


(defn action-map
  "Provides the operation map for an action, which always has a relative path
   to the resource's id."
  [id op-kw-or-name]
  (let [href (str id "/" (name op-kw-or-name))]
    (operation-map href op-kw-or-name)))

(defmethod crud/do-action [resource-type "deploy"]
  [{{uuid :uuid} :params :as request}]
  (add-impl (assoc request :body (call-sm "http://lm-um:46000/api/v2/lm/service-instance-int" (:body request)))))


(defmethod crud/set-operations resource-type
  [{:keys [id state] :as resource} request]
  (let [deploy-op (action-map id :deploy)]
    (cond-> (crud/set-standard-operations resource request)
            (and true?) (update :operations conj deploy-op)
           )))