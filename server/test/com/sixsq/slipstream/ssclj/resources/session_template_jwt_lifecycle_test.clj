(ns com.sixsq.slipstream.ssclj.resources.session-template-jwt-lifecycle-test
  (:require
    [clojure.test :refer :all]
    [com.sixsq.slipstream.ssclj.app.params :as p]
    [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
    [com.sixsq.slipstream.ssclj.resources.lifecycle-test-utils :as ltu]
    [com.sixsq.slipstream.ssclj.resources.session-template :as st]
    [com.sixsq.slipstream.ssclj.resources.session-template-jwt :as jwt]
    [com.sixsq.slipstream.ssclj.util.metadata-test-utils :as mdtu]))


(use-fixtures :each ltu/with-test-server-fixture)


(def base-uri (str p/service-context (u/de-camelcase st/resource-name)))



(deftest check-metadata
  (mdtu/check-metadata-exists (str st/resource-url "-" jwt/resource-url)))


;; FIXME: There should be a lifecycle test!
