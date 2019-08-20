(ns com.sixsq.slipstream.ssclj.resources.session-jwt.utils
  (:require
    [buddy.core.codecs :as codecs]
    [buddy.sign.jws :as jws]
    [clojure.data.json :as json]
    [com.sixsq.slipstream.ssclj.util.log :as logu]))


(defn extract-claims
  [base64-token]
  (try
    (some-> base64-token
            (#'jws/split-jws-message)                       ;; private function, may break with buddy updates!
            second
            (#'jws/decode-payload)                          ;; private function, may break with buddy updates!
            codecs/bytes->str
            (json/read-str :key-fn keyword))
    (catch Exception _
      nil)))


;; FIXME: If user matching is needed, the method/contents of the user resource must be defined.
(def match-user identity)


(defn throw-no-token []
  (logu/log-and-throw-400 "unable to retrieve JWT authentication token"))


(defn throw-no-issuer []
  (logu/log-and-throw-400 (str "JWT token is missing issuer (iss) attribute")))


(defn throw-invalid-jwt [msg]
  (logu/log-and-throw-400 (str "invalid JWT authentication token: " msg)))


(defn throw-inactive-user [username]
  (logu/log-and-throw-400 (str "account is inactive (" username ")")))
