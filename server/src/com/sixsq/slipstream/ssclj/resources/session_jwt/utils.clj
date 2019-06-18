(ns com.sixsq.slipstream.ssclj.resources.session-jwt.utils
  (:require
    [com.sixsq.slipstream.ssclj.resources.callback :as callback]
    [com.sixsq.slipstream.ssclj.resources.common.crud :as crud]
    [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
    [com.sixsq.slipstream.ssclj.util.log :as logu]
    [com.sixsq.slipstream.util.response :as r]
    [buddy.sign.jws :as jws]))

;; WARNING: Private functions are used. May break with buddy version updates!
(defn extract-claims
  [base64-token]
  (-> base64-token
      (#'jws/split-jws-message)
      second
      (#'jws/decode-payload)))

;; general exceptions

(defn throw-no-access-token [redirectURI]
  (logu/log-error-and-throw-with-redirect 400 "unable to retrieve JWT access token" redirectURI))

(defn throw-no-issuer [redirectURI]
  (logu/log-error-and-throw-with-redirect 400 (str "JWT token is missing subject (iss) attribute") redirectURI))

(defn throw-invalid-access-code [msg redirectURI]
  (logu/log-error-and-throw-with-redirect 400 (str "error when processing JWT access token: " msg) redirectURI))

(defn throw-inactive-user [username redirectURI]
  (logu/log-error-and-throw-with-redirect 400 (str "account is inactive (" username ")") redirectURI))
