(def +version+ "1.10-SNAPSHOT")
(def +slipstream-version+ "3.51")

(defproject eu.mf2c-project.cimi/server "1.10-SNAPSHOT"

  :description "MF2C CIMI resources and server"

  :url "https://github.com/mF2C/cimi"

  :license {:name         "Apache 2.0"
            :url          "http://www.apache.org/licenses/LICENSE-2.0.txt"
            :distribution :repo}

  :plugins [[lein-parent "0.3.2"]
            [lein-environ "1.1.0"]]

  :parent-project {:coords  [sixsq/slipstream-parent "5.3.5"]
                   :inherit [:min-lein-version
                             :managed-dependencies
                             :repositories
                             :deploy-repositories]}

  :source-paths ["src"]

  :pom-location "target/"

  :dependencies
  [[dataclay/cimi "0.0.1-SNAPSHOT"]]

  :profiles {:provided
             {:dependencies [[org.clojure/clojure]
                             [com.sixsq.slipstream/SlipStreamCljResources-jar ~+slipstream-version+]
                             [org.clojure/test.check :scope "provided"]]}

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
