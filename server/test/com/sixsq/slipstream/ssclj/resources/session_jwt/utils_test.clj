(ns
  ^{:copyright "Copyright 2019, SixSq Sàrl"
    :license   "http://www.apache.org/licenses/LICENSE-2.0"}
  com.sixsq.slipstream.ssclj.resources.session-jwt.utils-test
  (:require
    [clojure.test :refer [are deftest is]]
    [com.sixsq.slipstream.ssclj.resources.session-jwt.utils :as t]
    [com.sixsq.slipstream.auth.utils.sign :as sign]))


(deftest check-jwt-parsing

  (is (nil? (t/extract-claims nil)))
  (is (nil? (t/extract-claims "invalid")))
  (is (nil? (t/extract-claims "a.b.invalid")))
  (is (= {:iss "issuer"} (t/extract-claims "a.eyJpc3MiOiJpc3N1ZXIifQ.c")))
  (is (= {:alg "RS256"} (t/extract-claims "a.eyJhbGciOiJSUzI1NiJ9.c")))
  (is (= {:iss "issuer"} (t/extract-claims (sign/sign-claims {:iss "issuer"})))))
