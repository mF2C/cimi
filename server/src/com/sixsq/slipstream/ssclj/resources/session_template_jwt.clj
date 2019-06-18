(ns com.sixsq.slipstream.ssclj.resources.session-template-jwt
  "
  Allows authentication based on a signed JSON Web Token (JWT), passed as a
  base64 encoded string.
"
  (:require
    [com.sixsq.slipstream.ssclj.resources.common.std-crud :as std-crud]
    [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
    [com.sixsq.slipstream.ssclj.resources.resource-metadata :as md]
    [com.sixsq.slipstream.ssclj.resources.session-template :as p]
    [com.sixsq.slipstream.ssclj.resources.spec.session-template-jwt :as session-tpl]
    [com.sixsq.slipstream.ssclj.util.metadata :as gen-md]))


(def ^:const authn-method "jwt")


(def ^:const resource-name "JWT")


(def ^:const resource-url authn-method)


;;
;; description
;;
(def ^:const desc
  (merge p/SessionTemplateDescription
         {:token {:displayName "JWT"
                  :category    "general"
                  :description "JSON Web Token"
                  :type        "string"
                  :mandatory   true
                  :readOnly    false
                  :order       20}}))


;;
;; initialization: register this Session template
;;

(defn initialize
  []
  (p/register authn-method desc)
  (std-crud/initialize p/resource-url ::session-tpl/schema)
  (md/register (gen-md/generate-metadata ::ns ::p/ns ::session-tpl/schema)))


;;
;; multimethods for validation
;;

(def validate-fn (u/create-spec-validation-fn ::session-tpl/schema))
(defmethod p/validate-subtype authn-method
  [resource]
  (validate-fn resource))
