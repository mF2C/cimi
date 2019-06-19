(ns com.sixsq.slipstream.ssclj.resources.spec.session-template-jwt-test
  (:require
    [clojure.test :refer [deftest is]]
    [com.sixsq.slipstream.ssclj.resources.session-template :as st]
    [com.sixsq.slipstream.ssclj.resources.spec.session-template-jwt :as session-tpl]
    [com.sixsq.slipstream.ssclj.resources.spec.spec-test-utils :as stu]))


(def valid-acl {:owner {:principal "ADMIN"
                        :type      "ROLE"}
                :rules [{:type      "ROLE",
                         :principal "ADMIN",
                         :right     "ALL"}]})


(deftest check-session-template-mitreid-token-schema
  (let [timestamp "1964-08-25T10:00:00.0Z"
        cfg {:id          (str st/resource-url "/jwt")
             :resourceURI st/resource-uri
             :created     timestamp
             :updated     timestamp
             :acl         valid-acl

             :method      "jwt"
             :instance    "jwt"
             :group       "Federated Identity"
             :redirectURI "https://nuv.la/webui/profile"

             :token       "some-compressed-jwt-value"}]

    (stu/is-valid ::session-tpl/schema cfg)

    (doseq [attr #{:id :resourceURI :created :updated :acl :method :instance :token}]
      (stu/is-invalid ::session-tpl/schema (dissoc cfg attr)))

    (doseq [attr #{:group :redirectURI}]
      (stu/is-valid ::session-tpl/schema (dissoc cfg attr)))))
