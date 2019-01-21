(ns com.sixsq.slipstream.ssclj.resources.spec.service-instance-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [are deftest is]]
            [com.sixsq.slipstream.ssclj.resources.common.schema :as schema]
            [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
            [com.sixsq.slipstream.ssclj.resources.spec.common :as c]))


(deftest check-service-instance-resource-schema
  (let [resource-name             "ServiceInstanceResource"
        resource-url              (u/de-camelcase resource-name)
        resource-uri              (str schema/slipstream-schema-uri resource-name)
        timestamp                 "1964-08-25T10:00:00.0Z"
        valid-acl                 {:owner {:principal "ADMIN"
                                           :type      "ROLE"}
                                   :rules [{:principal "ADMIN"
                                            :type      "ROLE"
                                            :right     "MODIFY"}]}
        service-instance-resource  {:id            		(str resource-url "/service-instance-resource")
                                    :resourceURI    	resource-uri
                                    :created        	timestamp
                                    :updated        	timestamp
                                    :acl            	valid-acl
                                    ;; service instance fields
                                    :user           	"user/testuser"
									:device_id			"device/id12345678"
									:device_ip			"192.168.242.5"
									:parent_device_id	"device/id024681214"
									:parent_device_ip	"192.168.242.15"
									:service_type		"docker"
                                    :service        	"service/71230958abdef9"
                                    :agreement      	"agreement/a1230958abdef0"
                                    :status         	"running"
                                    :agents         	[{:agent {:href "agent/1230958abdef1"}, :ports [31111], :num_cpus 1
															:status "running", :container_id "asdasd-asdasda", :allow true,
															url "192.168.1.31", :master_compss true :app_type "docker"}
														{:agent {:href "agent/1230958abdef2"}, :ports [31111], :num_cpus 2
															:status "running", :container_id "asdasd-hasdagsa", :allow false,
															:url "192.168.1.32", :master_compss false :app_type "docker"}]}]
    (is (s/valid? :cimi/service-instance service-instance-resource))
    (is (not (s/valid? :cimi/service-instance (assoc service-instance-resource :bad-field "bla bla bla"))))))
