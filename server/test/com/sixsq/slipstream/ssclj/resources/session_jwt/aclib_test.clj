(ns
  ^{:copyright "Copyright 2019, SixSq Sàrl"
    :license   "http://www.apache.org/licenses/LICENSE-2.0"}
  com.sixsq.slipstream.ssclj.resources.session-jwt.aclib-test
  (:require
    [aleph.tcp :as tcp]
    [clojure.test :refer :all]
    [com.sixsq.slipstream.ssclj.resources.session-jwt.aclib :as t])
  (:import
    (clojure.lang ExceptionInfo)
    (java.io Closeable)))


(def no-op-client
  (proxy
    [Closeable] []
    (close [] nil)))


(defn check-thrown-exception
  [token test-name pattern]
  (try
    (t/validate-jwt token)
    (is false test-name)
    (catch Exception e
      (is (instance? ExceptionInfo e))
      (let [{:keys [status body]} (ex-data e)
            {body-status :status body-message :message} body]
        (is (= 500 status))
        (is (= 500 body-status))
        (is (re-matches pattern body-message))))))


(deftest check-validate-jwt

  (let [msg   "connection error"
        token "placeholder-token"]

    (with-redefs [tcp/client (fn [opts] (throw (ex-info msg {:message msg})))]
      (is (thrown-with-msg? ExceptionInfo (re-pattern msg) (t/validate-jwt token))))

    (with-redefs [tcp/client (fn [opts] (throw (Exception. msg)))]
      (check-thrown-exception token "client connection" (re-pattern (str "^error when connecting.*" msg ".*$"))))

    (with-redefs [tcp/client (constantly (atom no-op-client))
                  t/put!     (fn [_ _ _ _] :timeout)]
      (check-thrown-exception token "put timeout" #"^put to.*timed out.*$"))

    (with-redefs [tcp/client (constantly (atom no-op-client))
                  t/put!     (fn [_ _ _ _] false)]
      (check-thrown-exception token "put general failure" #"^failed to send message.*$"))

    (with-redefs [tcp/client (constantly (atom no-op-client))
                  t/put!     (fn [_ _ _ _] false)]
      (check-thrown-exception token "put general failure" #"^failed to send message.*$"))

    (with-redefs [tcp/client (constantly (atom no-op-client))
                  t/put!     (fn [_ _ _ _] true)
                  t/take!    (fn [_ _ _ _] :timeout)]
      (check-thrown-exception token "take timeout" #"^take.*timed out.*$"))

    (with-redefs [tcp/client (constantly (atom no-op-client))
                  t/put!     (fn [_ _ _ _] true)
                  t/take!    (fn [_ _ _ _] :error)]
      (check-thrown-exception token "take error" #"^take.*failed.*$"))

    (with-redefs [tcp/client (constantly (atom no-op-client))
                  t/put!     (fn [_ _ _ _] true)
                  t/take!    (fn [_ _ _ _] (byte-array (map byte "OK\n")))]
      (is (= "OK" (t/validate-jwt token))))))
