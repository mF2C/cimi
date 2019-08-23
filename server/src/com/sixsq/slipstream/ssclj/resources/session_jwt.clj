(ns com.sixsq.slipstream.ssclj.resources.session-jwt
  (:require
    [clojure.tools.logging :as log]
    [com.sixsq.slipstream.auth.cookies :as cookies]
    [com.sixsq.slipstream.auth.internal :as auth-internal]
    [com.sixsq.slipstream.auth.utils.timestamp :as ts]
    [com.sixsq.slipstream.ssclj.resources.common.std-crud :as std-crud]
    [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
    [com.sixsq.slipstream.ssclj.resources.session :as p]
    [com.sixsq.slipstream.ssclj.resources.session-jwt.aclib :as aclib]
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

(def validate-fn (u/create-spec-validation-fn :cimi/session))
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
  [{:keys [token href] :as resource} {:keys [headers] :as request}]
  (if token
    (if-let [{:keys [iss]} (jwt-utils/extract-claims token)]
      (if iss
        (let [validation-response (aclib/validate-jwt token)]
          (if (= "OK" validation-response)
            (if-let [matched-user (jwt-utils/match-user iss)]
              (let [session-info {:href href, :username matched-user}
                    {:keys [id] :as session} (sutils/create-session session-info headers authn-method)
                    claims       (cond-> (auth-internal/create-claims matched-user)
                                         id (assoc :session id)
                                         id (update :roles #(str id " " %)))
                    cookie       (cookies/claims-cookie claims)
                    expires      (ts/rfc822->iso8601 (:expires cookie))
                    claims-roles (:roles claims)
                    session      (cond-> (assoc session :expiry expires)
                                         claims-roles (assoc :roles claims-roles))]

                (log/debug "JWT cookie token claims for" (u/document-id href) ":" (pr-str claims))
                (let [cookies {(sutils/cookie-name (:id session)) cookie}]
                  [{:cookies cookies} session]))
              (jwt-utils/throw-inactive-user iss))
            (jwt-utils/throw-invalid-jwt (format "token validation error '%s'" validation-response))))
        (jwt-utils/throw-no-issuer))
      (jwt-utils/throw-invalid-jwt "cannot parse JWT"))
    (jwt-utils/throw-no-token)))


;;
;; initialization: no schema for this parent resource
;;

(defn initialize
  []
  (std-crud/initialize p/resource-url :cimi/session))
