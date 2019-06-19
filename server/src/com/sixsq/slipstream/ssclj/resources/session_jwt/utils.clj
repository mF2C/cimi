(ns com.sixsq.slipstream.ssclj.resources.session-jwt.utils
  (:require
    [aleph.tcp :as tcp]
    [buddy.core.codecs :as codecs]
    [buddy.sign.jws :as jws]
    [clojure.data.json :as json]
    [clojure.edn :as edn]
    [com.sixsq.slipstream.ssclj.util.log :as logu]
    [environ.core :as env]
    [manifold.stream :as stream]))


(def aclib-host (env/env :mf2c-aclib-host "aclib"))


(def aclib-port (edn/read-string (env/env :mf2c-aclib-port "46080")))


(def timeout 1000)


;; WARNING: Private functions are used. May break with buddy version updates!
(defn extract-claims
  [base64-token]
  (try
    (some-> base64-token
            (#'jws/split-jws-message)
            second
            (#'jws/decode-payload)
            codecs/bytes->str
            (json/read-str :key-fn keyword))
    (catch Exception _
      nil)))


(defn validate-jwt
  [token]
  (with-open [client @(tcp/client {:host aclib-host, :port aclib-port})]
    (let [msg (str (json/write-str {:typ "jwt", :token token}) "\n")]
      (when @(stream/try-put! client msg timeout)
        (some-> @(stream/try-take! client timeout)
                codecs/bytes->str)))))


;; FIXME: If user matching is needed, the method/contents of the user resource must be defined.
(def match-user identity)

;; general exceptions

(defn throw-no-token []
  (logu/log-and-throw-400 "unable to retrieve JWT authentication token"))

(defn throw-no-issuer []
  (logu/log-and-throw-400 (str "JWT token is missing issuer (iss) attribute")))

(defn throw-invalid-access-code [msg]
  (logu/log-and-throw-400 (str "error when processing JWT authentication token: " msg)))

(defn throw-invalid-jwt [msg]
  (logu/log-and-throw-400 (str "invalid JWT authentication token: " msg)))

(defn throw-inactive-user [username]
  (logu/log-and-throw-400 (str "account is inactive (" username ")")))
