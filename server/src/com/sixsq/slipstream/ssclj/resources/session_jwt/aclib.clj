(ns com.sixsq.slipstream.ssclj.resources.session-jwt.aclib
  (:refer-clojure :exclude [send])
  (:require
    [aleph.tcp :as tcp]
    [buddy.core.codecs :as codecs]
    [clojure.data.json :as json]
    [clojure.edn :as edn]
    [clojure.string :as str]
    [com.sixsq.slipstream.ssclj.util.log :as logu]
    [environ.core :as env]
    [manifold.stream :as stream]))


(def host (env/env :mf2c-aclib-host "aclib"))


(def port (edn/read-string (env/env :mf2c-aclib-port "46080")))


;; This exceptionally long timeout is needed because the first request to
;; the ACLIB server can take 10-15s because of server initialization.
(def timeout 20000)


;; wrapped to allow for testing
(defn put!
  [client msg timeout timeout-val]
  @(stream/try-put! client msg timeout timeout-val))


;; wrapped to allow for testing
(defn take!
  [client error-val timeout timeout-val]
  @(stream/try-take! client error-val timeout timeout-val))


(defn tcp-client
  "Creates a TCP client used to connect to the ACLIB server."
  [host port]
  (try
    @(tcp/client {:host host, :port port})
    (catch Exception e
      (logu/log-and-throw 500 (format "error when connecting to %s:%s: %s" host port (str e))))))


(defn send
  "Sends the wrapped JWT token to the ACLIB server. Returns nil on success,
   throws an exception otherwise."
  [client wrapped-jwt]
  (let [resp (put! client wrapped-jwt timeout :timeout)]
    (case resp
      :timeout (logu/log-and-throw 500 (format "put to %s:%s timed out after %s ms" host port timeout))
      false (logu/log-and-throw 500 (format "failed to send message to %s:%s" host port))
      nil)))


(defn receive
  "Waits for a response from the ACLIB server and provides the response as a
   trimmed string. Throws exceptions on failures."
  [client]
  (let [take-response (take! client :error timeout :timeout)]
    (case take-response
      :timeout (logu/log-and-throw 500 (format "take from %s:%s timed out after %s ms" host port timeout))
      :error (logu/log-and-throw 500 (format "take from %s:%s failed" host port))
      (str/trim (codecs/bytes->str take-response)))))


(defn validate-jwt
  [token]
  (with-open [client (tcp-client host port)]
    (let [wrapped-jwt (str (json/write-str {:typ "jwt", :token token}) "\n")]
      (send client wrapped-jwt)
      (receive client))))
