(def +version+ "2.20-SNAPSHOT")
(def +slipstream-version+ "3.67")

(defproject eu.mf2c-project.cimi/server "2.20-SNAPSHOT"

  :description "MF2C CIMI resources and server"

  :url "https://github.com/mF2C/cimi"

  :license {:name         "Apache 2.0"
            :url          "http://www.apache.org/licenses/LICENSE-2.0.txt"
            :distribution :repo}

  :plugins [[lein-parent "0.3.2"]
            [lein-environ "1.1.0"]]

  ;:parent-project {:coords  [sixsq/slipstream-parent "5.3.19"]
  ;                 :inherit [:min-lein-version
  ;                           :managed-dependencies]}
  ;                           ;:repositories
  ;                           ;:deploy-repositories]}
  :parent-project {:coords  [sixsq.nuvla/parent "6.2.0"]
                   :inherit [:plugins
                             :min-lein-version
                             :managed-dependencies]}

  :repositories
  [["nuvla-releases" {:url           "https://sixsq-build-artifacts-legacy.s3.amazonaws.com"
                      :snapshots     false
                      :sign-releases false
                      :checksum      :fail
                      :update        :daily}]]

  :source-paths ["src"]

  :resource-paths ["resources"]

  :pom-location "target/"

  :dependencies [[com.sixsq.dataclay/proxy "2.4"]]

  :profiles {:provided
             {:dependencies [[org.clojure/clojure]
                             [com.sixsq.slipstream/SlipStreamCljResources-jar ~+slipstream-version+
                              :exclusions [org.elasticsearch/elasticsearch
                                           org.elasticsearch.client/transport
                                           cc.qbits/spandex]]]}

             :test
             {:dependencies   [[peridot]
                               [org.clojure/test.check]
                               [org.slf4j/slf4j-log4j12]
                               [com.cemerick/url]
                               [org.apache.curator/curator-test]
                               [com.sixsq.slipstream/SlipStreamDbTesting-jar ~+slipstream-version+]
                               [com.sixsq.slipstream/SlipStreamCljResourcesTests-jar ~+slipstream-version+]]
              :resource-paths ["test-resources"]
              :aot            :all}})

