(ns com.sixsq.slipstream.ssclj.resources.session-jwt
  (:require
    [clojure.string :as str]
    [clojure.tools.logging :as log]
    [com.sixsq.slipstream.auth.cookies :as cookies]
    [com.sixsq.slipstream.auth.external :as ex]
    [com.sixsq.slipstream.auth.internal :as auth-internal]
    [com.sixsq.slipstream.auth.utils.sign :as sign]
    [com.sixsq.slipstream.auth.utils.timestamp :as ts]
    [com.sixsq.slipstream.ssclj.resources.common.std-crud :as std-crud]
    [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
    [com.sixsq.slipstream.ssclj.resources.session :as p]
    [com.sixsq.slipstream.ssclj.resources.session-jwt.utils :as jwt-utils]
    [com.sixsq.slipstream.ssclj.resources.session-template-api-key :as tpl]
    [com.sixsq.slipstream.ssclj.resources.session.utils :as sutils]
    [com.sixsq.slipstream.ssclj.resources.spec.session :as session]
    [com.sixsq.slipstream.ssclj.resources.spec.session-template-jwt :as session-tpl]))


(def ^:const authn-method "jwt")


;;
;; schemas
;;

(def SessionDescription
  tpl/desc)


;;
;; description
;;
(def ^:const desc SessionDescription)


;;
;; multimethods for validation
;;

(def validate-fn (u/create-spec-validation-fn ::session/session))
(defmethod p/validate-subtype authn-method
  [resource]
  (validate-fn resource))


(def create-validate-fn (u/create-spec-validation-fn ::session-tpl/schema-create))
(defmethod p/create-validate-subtype authn-method
  [resource]
  (create-validate-fn resource))


;;
;; transform template into session resource
;;

(defmethod p/tpl->session authn-method
  [{:keys [token instance href redirectURI] :as resource} {:keys [headers] :as request}]
  (if token
    (try
      (let [{:keys [iss] :as claims} (jwt-utils/extract-claims token)]
        (log/debug "JWT authentication claims for" instance ":" (pr-str claims))
        (if iss
          (if-let [matched-user (ex/match-oidc-username :jwt iss instance)]
            (let [session-info {:href href, :username matched-user, :redirectURI redirectURI}
                  {:keys [id] :as session} (sutils/create-session session-info headers authn-method)
                  claims (cond-> (auth-internal/create-claims matched-user)
                                 id (assoc :session id)
                                 id (update :roles #(str id " " %)))
                  cookie (cookies/claims-cookie claims)
                  expires (ts/rfc822->iso8601 (:expires cookie))
                  claims-roles (:roles claims)
                  session (cond-> (assoc session :expiry expires)
                                  claims-roles (assoc :roles claims-roles))]

              (log/debug "JWT cookie token claims for" (u/document-id href) ":" (pr-str claims))
              (let [cookies {(sutils/cookie-name (:id session)) cookie}]
                (if redirectURI
                  [{:status 303, :headers {"Location" redirectURI}, :cookies cookies} session]
                  [{:cookies cookies} session])))
            (jwt-utils/throw-inactive-user iss nil))
          (jwt-utils/throw-no-issuer nil)))
      (catch Exception e
        (jwt-utils/throw-invalid-access-code (str e) nil)))
    (jwt-utils/throw-no-access-token nil)))


;;
;; initialization: no schema for this parent resource
;;

(defn initialize
  []
  (std-crud/initialize p/resource-url ::session/session))
