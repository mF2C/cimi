(ns com.sixsq.slipstream.ssclj.resources.device-lifecycle-test
  (:require
    [clojure.data.json :as json]
    [clojure.test :refer :all]
    [com.sixsq.slipstream.ssclj.app.params :as p]
    [com.sixsq.slipstream.ssclj.middleware.authn-info-header :refer [authn-info-header]]
    [com.sixsq.slipstream.ssclj.resources.common.schema :as c]
    [com.sixsq.slipstream.ssclj.resources.common.utils :as u]
    [com.sixsq.slipstream.ssclj.resources.device :as device]
    [com.sixsq.slipstream.ssclj.resources.example-resource.utils :as utils]
    [com.sixsq.slipstream.ssclj.resources.lifecycle-test-utils :as ltu]
    [peridot.core :refer :all]))

(use-fixtures :each ltu/with-test-server-fixture)

(def base-uri (str p/service-context (u/de-camelcase device/resource-url)))

(deftest lifecycle
  (let [session (-> (ltu/ring-app)
                    session
                    (content-type "application/json"))
        session-admin (header session authn-info-header "root ADMIN USER ANON")
        session-user (header session authn-info-header "jane USER ANON")
        session-anon (header session authn-info-header "unknown ANON")]


    ;; admin collection query should succeed but be empty
    (-> session-admin
        (request base-uri)
        (ltu/body->edn)
        (ltu/is-status 200)
        (ltu/is-count zero?)
        (ltu/is-operation-present "add")
        (ltu/is-operation-absent "delete")
        (ltu/is-operation-absent "edit")
        (ltu/is-operation-absent "execute"))

    ;; user collection query should not succeed
    (-> session-user
        (request base-uri)
        (ltu/body->edn)
        ;(ltu/is-status 403)
    )

    ;; anonymous collection query should not succeed
    (-> session-anon
        (request base-uri)
        (ltu/body->edn)
        (ltu/is-status 403)
    )

    ;; create a callback as an admin
    (let [resource-name         "DeviceStatic"
          resource-url          (u/de-camelcase resource-name)
          create-test-callback  {:id                    (str resource-url "/device-resource")
                                 :resourceURI           base-uri
                                ;  :acl                   {:owner {:principal "ADMIN"
                                ;                                  :type      "ROLE"}
                                ;                          :rules [{:principal "ADMIN"
                                ;                                   :type      "ROLE"
                                ;                                   :right     "ALL"}]}
                                                                 ;{:principal "ANON"
                                                                  ;:type      "ROLE"
                                                                  ;:right     "MODIFY"}]}
                                 ;; sharing model fields
                                ;  :user_id              "user/1230958abdef"
                                  :deviceID            "1234567890abcdef"
                                  :isLeader          false
                                  :os                "Linux-4.4.0-116-generic-x86_64-with-debian-8.10"
                                  :arch              "x86_64"
                                  :cpuManufacturer   "Intel(R) Core(TM) i7-7700 CPU @ 3.60GHz"
                                  :physicalCores     4
                                  :logicalCores       8
                                  :cpuClockSpeed     "3.6000 GHz"
                                  :memory            32134.5078125
                                  :storage           309646.39453125
                                  :powerPlugged      true
                                  :networkingStandards "['lo', 'enp2s0', 'docker0', 'wlp3s0']"
                                  :ethernetAddress   "[snic(family=<AddressFamily.AF_INET: 2>, address='147.83.159.199', netmask='255.255.255.224', broadcast='147.83.159.223', ptp=None), snic(family=<AddressFamily.AF_INET6: 10>, address='fe80::16de:c6b7:3dc3:d11c%enp0s31f6', netmask='ffff:ffff:ffff:ffff::', broadcast=None, ptp=None), snic(family=<AddressFamily.AF_PACKET: 17>, address='4c:cc:6a:f5:a3:ea', netmask=None, broadcast='ff:ff:ff:ff:ff:ff', ptp=None)]"
                                  :wifiAddress       "[snic(family=<AddressFamily.AF_INET: 2>, address='192.168.4.71', netmask='255.255.255.0', broadcast='192.168.4.255', ptp=None), snic(family=<AddressFamily.AF_INET6: 10>, address='fe80::e7ec:a09a:36fe:5ad4%wlx001986d03ca6', netmask='ffff:ffff:ffff:ffff::', broadcast=None, ptp=None), snic(family=<AddressFamily.AF_PACKET: 17>, address='00:19:86:d0:3c:a6', netmask=None, broadcast='ff:ff:ff:ff:ff:ff', ptp=None)]"
                                  :hwloc             "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE topology SYSTEM \"hwloc.dtd\">\n<topology>\n  <object type=\"Machine\" os_index=\"0\" cpuset=\"0x000000ff\" complete_cpuset=\"0x000000ff\" online_cpuset=\"0x000000ff\" allowed_cpuset=\"0x000000ff\" local_memory=\"8256716800\">\n    <page_type size=\"4096\" count=\"2015800\"/>\n    <page_type size=\"2097152\" count=\"0\"/>\n    <page_type size=\"1073741824\" count=\"0\"/>\n    <info name=\"DMIProductName\" value=\"Inspiron 5570\"/>\n    <info name=\"DMIProductVersion\" value=\"\"/>\n    <info name=\"DMIBoardVendor\" value=\"Dell Inc.\"/>\n    <info name=\"DMIBoardName\" value=\"0YDF7T\"/>\n    <info name=\"DMIBoardVersion\" value=\"X06\"/>\n    <info name=\"DMIBoardAssetTag\" value=\"\"/>\n    <info name=\"DMIChassisVendor\" value=\"Dell Inc.\"/>\n    <info name=\"DMIChassisType\" value=\"10\"/>\n    <info name=\"DMIChassisVersion\" value=\"\"/>\n    <info name=\"DMIChassisAssetTag\" value=\"\"/>\n    <info name=\"DMIBIOSVendor\" value=\"Dell Inc.\"/>\n    <info name=\"DMIBIOSVersion\" value=\"1.0.9\"/>\n    <info name=\"DMIBIOSDate\" value=\"12/12/2017\"/>\n    <info name=\"DMISysVendor\" value=\"Dell Inc.\"/>\n    <info name=\"Backend\" value=\"Linux\"/>\n    <info name=\"LinuxCgroup\" value=\"/\"/>\n    <info name=\"OSName\" value=\"Linux\"/>\n    <info name=\"OSRelease\" value=\"4.13.0-38-generic\"/>\n    <info name=\"OSVersion\" value=\"#43~16.04.1-Ubuntu SMP Wed Mar 14 17:48:43 UTC 2018\"/>\n    <info name=\"HostName\" value=\"souvik-Inspiron-5570\"/>\n    <info name=\"Architecture\" value=\"x86_64\"/>\n    <info name=\"hwlocVersion\" value=\"1.11.2\"/>\n    <info name=\"ProcessName\" value=\"hwloc-ls\"/>\n    <object type=\"Package\" os_index=\"0\" cpuset=\"0x000000ff\" complete_cpuset=\"0x000000ff\" online_cpuset=\"0x000000ff\" allowed_cpuset=\"0x000000ff\">\n      <info name=\"CPUVendor\" value=\"GenuineIntel\"/>\n      <info name=\"CPUFamilyNumber\" value=\"6\"/>\n      <info name=\"CPUModelNumber\" value=\"142\"/>\n      <info name=\"CPUModel\" value=\"Intel(R) Core(TM) i7-8550U CPU @ 1.80GHz\"/>\n      <info name=\"CPUStepping\" value=\"10\"/>\n      <object type=\"Cache\" cpuset=\"0x000000ff\" complete_cpuset=\"0x000000ff\" online_cpuset=\"0x000000ff\" allowed_cpuset=\"0x000000ff\" cache_size=\"8388608\" depth=\"3\" cache_linesize=\"64\" cache_associativity=\"16\" cache_type=\"0\">\n        <info name=\"Inclusive\" value=\"1\"/>\n        <object type=\"Cache\" cpuset=\"0x00000011\" complete_cpuset=\"0x00000011\" online_cpuset=\"0x00000011\" allowed_cpuset=\"0x00000011\" cache_size=\"262144\" depth=\"2\" cache_linesize=\"64\" cache_associativity=\"4\" cache_type=\"0\">\n          <info name=\"Inclusive\" value=\"0\"/>\n          <object type=\"Cache\" cpuset=\"0x00000011\" complete_cpuset=\"0x00000011\" online_cpuset=\"0x00000011\" allowed_cpuset=\"0x00000011\" cache_size=\"32768\" depth=\"1\" cache_linesize=\"64\" cache_associativity=\"8\" cache_type=\"1\">\n            <info name=\"Inclusive\" value=\"0\"/>\n            <object type=\"Cache\" cpuset=\"0x00000011\" complete_cpuset=\"0x00000011\" online_cpuset=\"0x00000011\" allowed_cpuset=\"0x00000011\" cache_size=\"32768\" depth=\"1\" cache_linesize=\"64\" cache_associativity=\"8\" cache_type=\"2\">\n              <info name=\"Inclusive\" value=\"0\"/>\n              <object type=\"Core\" os_index=\"0\" cpuset=\"0x00000011\" complete_cpuset=\"0x00000011\" online_cpuset=\"0x00000011\" allowed_cpuset=\"0x00000011\">\n                <object type=\"PU\" os_index=\"0\" cpuset=\"0x00000001\" complete_cpuset=\"0x00000001\" online_cpuset=\"0x00000001\" allowed_cpuset=\"0x00000001\"/>\n                <object type=\"PU\" os_index=\"4\" cpuset=\"0x00000010\" complete_cpuset=\"0x00000010\" online_cpuset=\"0x00000010\" allowed_cpuset=\"0x00000010\"/>\n              </object>\n            </object>\n          </object>\n        </object>\n        <object type=\"Cache\" cpuset=\"0x00000022\" complete_cpuset=\"0x00000022\" online_cpuset=\"0x00000022\" allowed_cpuset=\"0x00000022\" cache_size=\"262144\" depth=\"2\" cache_linesize=\"64\" cache_associativity=\"4\" cache_type=\"0\">\n          <info name=\"Inclusive\" value=\"0\"/>\n          <object type=\"Cache\" cpuset=\"0x00000022\" complete_cpuset=\"0x00000022\" online_cpuset=\"0x00000022\" allowed_cpuset=\"0x00000022\" cache_size=\"32768\" depth=\"1\" cache_linesize=\"64\" cache_associativity=\"8\" cache_type=\"1\">\n            <info name=\"Inclusive\" value=\"0\"/>\n            <object type=\"Cache\" cpuset=\"0x00000022\" complete_cpuset=\"0x00000022\" online_cpuset=\"0x00000022\" allowed_cpuset=\"0x00000022\" cache_size=\"32768\" depth=\"1\" cache_linesize=\"64\" cache_associativity=\"8\" cache_type=\"2\">\n              <info name=\"Inclusive\" value=\"0\"/>\n              <object type=\"Core\" os_index=\"1\" cpuset=\"0x00000022\" complete_cpuset=\"0x00000022\" online_cpuset=\"0x00000022\" allowed_cpuset=\"0x00000022\">\n                <object type=\"PU\" os_index=\"1\" cpuset=\"0x00000002\" complete_cpuset=\"0x00000002\" online_cpuset=\"0x00000002\" allowed_cpuset=\"0x00000002\"/>\n                <object type=\"PU\" os_index=\"5\" cpuset=\"0x00000020\" complete_cpuset=\"0x00000020\" online_cpuset=\"0x00000020\" allowed_cpuset=\"0x00000020\"/>\n              </object>\n            </object>\n          </object>\n        </object>\n        <object type=\"Cache\" cpuset=\"0x00000044\" complete_cpuset=\"0x00000044\" online_cpuset=\"0x00000044\" allowed_cpuset=\"0x00000044\" cache_size=\"262144\" depth=\"2\" cache_linesize=\"64\" cache_associativity=\"4\" cache_type=\"0\">\n          <info name=\"Inclusive\" value=\"0\"/>\n          <object type=\"Cache\" cpuset=\"0x00000044\" complete_cpuset=\"0x00000044\" online_cpuset=\"0x00000044\" allowed_cpuset=\"0x00000044\" cache_size=\"32768\" depth=\"1\" cache_linesize=\"64\" cache_associativity=\"8\" cache_type=\"1\">\n            <info name=\"Inclusive\" value=\"0\"/>\n            <object type=\"Cache\" cpuset=\"0x00000044\" complete_cpuset=\"0x00000044\" online_cpuset=\"0x00000044\" allowed_cpuset=\"0x00000044\" cache_size=\"32768\" depth=\"1\" cache_linesize=\"64\" cache_associativity=\"8\" cache_type=\"2\">\n              <info name=\"Inclusive\" value=\"0\"/>\n              <object type=\"Core\" os_index=\"2\" cpuset=\"0x00000044\" complete_cpuset=\"0x00000044\" online_cpuset=\"0x00000044\" allowed_cpuset=\"0x00000044\">\n                <object type=\"PU\" os_index=\"2\" cpuset=\"0x00000004\" complete_cpuset=\"0x00000004\" online_cpuset=\"0x00000004\" allowed_cpuset=\"0x00000004\"/>\n                <object type=\"PU\" os_index=\"6\" cpuset=\"0x00000040\" complete_cpuset=\"0x00000040\" online_cpuset=\"0x00000040\" allowed_cpuset=\"0x00000040\"/>\n              </object>\n            </object>\n          </object>\n        </object>\n        <object type=\"Cache\" cpuset=\"0x00000088\" complete_cpuset=\"0x00000088\" online_cpuset=\"0x00000088\" allowed_cpuset=\"0x00000088\" cache_size=\"262144\" depth=\"2\" cache_linesize=\"64\" cache_associativity=\"4\" cache_type=\"0\">\n          <info name=\"Inclusive\" value=\"0\"/>\n          <object type=\"Cache\" cpuset=\"0x00000088\" complete_cpuset=\"0x00000088\" online_cpuset=\"0x00000088\" allowed_cpuset=\"0x00000088\" cache_size=\"32768\" depth=\"1\" cache_linesize=\"64\" cache_associativity=\"8\" cache_type=\"1\">\n            <info name=\"Inclusive\" value=\"0\"/>\n            <object type=\"Cache\" cpuset=\"0x00000088\" complete_cpuset=\"0x00000088\" online_cpuset=\"0x00000088\" allowed_cpuset=\"0x00000088\" cache_size=\"32768\" depth=\"1\" cache_linesize=\"64\" cache_associativity=\"8\" cache_type=\"2\">\n              <info name=\"Inclusive\" value=\"0\"/>\n              <object type=\"Core\" os_index=\"3\" cpuset=\"0x00000088\" complete_cpuset=\"0x00000088\" online_cpuset=\"0x00000088\" allowed_cpuset=\"0x00000088\">\n                <object type=\"PU\" os_index=\"3\" cpuset=\"0x00000008\" complete_cpuset=\"0x00000008\" online_cpuset=\"0x00000008\" allowed_cpuset=\"0x00000008\"/>\n                <object type=\"PU\" os_index=\"7\" cpuset=\"0x00000080\" complete_cpuset=\"0x00000080\" online_cpuset=\"0x00000080\" allowed_cpuset=\"0x00000080\"/>\n              </object>\n            </object>\n          </object>\n        </object>\n      </object>\n    </object>\n    <object type=\"Bridge\" os_index=\"0\" bridge_type=\"0-1\" depth=\"0\" bridge_pci=\"0000:[00-03]\">\n      <object type=\"PCIDev\" os_index=\"32\" name=\"Intel Corporation\" pci_busid=\"0000:00:02.0\" pci_type=\"0300 [8086:5917] [1028:0810] 07\" pci_link_speed=\"0.000000\">\n        <info name=\"PCIVendor\" value=\"Intel Corporation\"/>\n        <object type=\"OSDev\" name=\"renderD128\" osdev_type=\"1\"/>\n        <object type=\"OSDev\" name=\"card0\" osdev_type=\"1\"/>\n        <object type=\"OSDev\" name=\"controlD64\" osdev_type=\"1\"/>\n      </object>\n      <object type=\"PCIDev\" os_index=\"368\" name=\"Intel Corporation\" pci_busid=\"0000:00:17.0\" pci_type=\"0106 [8086:9d03] [1028:0810] 21\" pci_link_speed=\"0.000000\">\n        <info name=\"PCIVendor\" value=\"Intel Corporation\"/>\n        <object type=\"OSDev\" name=\"sda\" osdev_type=\"0\">\n          <info name=\"LinuxDeviceID\" value=\"8:0\"/>\n          <info name=\"Vendor\" value=\"Seagate\"/>\n          <info name=\"Model\" value=\"ST2000LM007-1R8174\"/>\n          <info name=\"Revision\" value=\"SDM2\"/>\n          <info name=\"SerialNumber\" value=\"WDZ769S9\"/>\n          <info name=\"Type\" value=\"Disk\"/>\n        </object>\n        <object type=\"OSDev\" name=\"sr0\" osdev_type=\"0\">\n          <info name=\"LinuxDeviceID\" value=\"11:0\"/>\n          <info name=\"Model\" value=\"HL-DT-ST_DVD+_-RW_GU90N\"/>\n          <info name=\"Revision\" value=\"A1C2\"/>\n          <info name=\"SerialNumber\" value=\"KZJH8MJ0534\"/>\n          <info name=\"Type\" value=\"Removable Media Device\"/>\n        </object>\n      </object>\n      <object type=\"Bridge\" os_index=\"448\" name=\"Intel Corporation\" bridge_type=\"1-1\" depth=\"1\" bridge_pci=\"0000:[01-01]\" pci_busid=\"0000:00:1c.0\" pci_type=\"0604 [8086:9d10] [0000:0000] f1\" pci_link_speed=\"0.000000\">\n        <info name=\"PCIVendor\" value=\"Intel Corporation\"/>\n        <object type=\"PCIDev\" os_index=\"4096\" name=\"Advanced Micro Devices, Inc. [AMD/ATI] Topaz XT [Radeon R7 M260/M265]\" pci_busid=\"0000:01:00.0\" pci_type=\"0380 [1002:6900] [1028:0810] c1\" pci_link_speed=\"0.000000\">\n          <info name=\"PCIVendor\" value=\"Advanced Micro Devices, Inc. [AMD/ATI]\"/>\n          <info name=\"PCIDevice\" value=\"Topaz XT [Radeon R7 M260/M265]\"/>\n          <object type=\"OSDev\" name=\"card1\" osdev_type=\"1\"/>\n          <object type=\"OSDev\" name=\"controlD65\" osdev_type=\"1\"/>\n          <object type=\"OSDev\" name=\"renderD129\" osdev_type=\"1\"/>\n        </object>\n      </object>\n      <object type=\"Bridge\" os_index=\"452\" name=\"Intel Corporation\" bridge_type=\"1-1\" depth=\"1\" bridge_pci=\"0000:[02-02]\" pci_busid=\"0000:00:1c.4\" pci_type=\"0604 [8086:9d14] [0000:0000] f1\" pci_link_speed=\"0.000000\">\n        <info name=\"PCIVendor\" value=\"Intel Corporation\"/>\n        <object type=\"PCIDev\" os_index=\"8192\" name=\"Realtek Semiconductor Co., Ltd. RTL8101/2/6E PCI Express Fast/Gigabit Ethernet controller\" pci_busid=\"0000:02:00.0\" pci_type=\"0200 [10ec:8136] [1028:0810] 07\" pci_link_speed=\"0.000000\">\n          <info name=\"PCIVendor\" value=\"Realtek Semiconductor Co., Ltd.\"/>\n          <info name=\"PCIDevice\" value=\"RTL8101/2/6E PCI Express Fast/Gigabit Ethernet controller\"/>\n          <object type=\"OSDev\" name=\"enp2s0\" osdev_type=\"2\">\n            <info name=\"Address\" value=\"50:9a:4c:cf:f4:b9\"/>\n          </object>\n        </object>\n      </object>\n      <object type=\"Bridge\" os_index=\"453\" name=\"Intel Corporation\" bridge_type=\"1-1\" depth=\"1\" bridge_pci=\"0000:[03-03]\" pci_busid=\"0000:00:1c.5\" pci_type=\"0604 [8086:9d15] [0000:0000] f1\" pci_link_speed=\"0.000000\">\n        <info name=\"PCIVendor\" value=\"Intel Corporation\"/>\n        <object type=\"PCIDev\" os_index=\"12288\" name=\"Qualcomm Atheros\" pci_busid=\"0000:03:00.0\" pci_type=\"0280 [168c:0042] [1028:1810] 31\" pci_link_speed=\"0.000000\">\n          <info name=\"PCIVendor\" value=\"Qualcomm Atheros\"/>\n          <object type=\"OSDev\" name=\"wlp3s0\" osdev_type=\"2\">\n            <info name=\"Address\" value=\"d4:6a:6a:9a:6b:87\"/>\n          </object>\n        </object>\n      </object>\n    </object>\n  </object>\n</topology>\n"
                                  :cpuinfo           "processor\t: 0\nvendor_id\t: GenuineIntel\ncpu family\t: 6\nmodel\t\t: 142\nmodel name\t: Intel(R) Core(TM) i7-8550U CPU @ 1.80GHz\nstepping\t: 10\nmicrocode\t: 0x84\ncpu MHz\t\t: 3100.022\ncache size\t: 8192 KB\nphysical id\t: 0\nsiblings\t: 8\ncore id\t\t: 0\ncpu cores\t: 4\napicid\t\t: 0\ninitial apicid\t: 0\nfpu\t\t: yes\nfpu_exception\t: yes\ncpuid level\t: 22\nwp\t\t: yes\nflags\t\t: fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts acpi mmx fxsr sse sse2 ss ht tm pbe syscall nx pdpe1gb rdtscp lm constant_tsc art arch_perfmon pebs bts rep_good nopl xtopology nonstop_tsc cpuid aperfmperf tsc_known_freq pni pclmulqdq dtes64 monitor ds_cpl vmx est tm2 ssse3 sdbg fma cx16 xtpr pdcm pcid sse4_1 sse4_2 x2apic movbe popcnt tsc_deadline_timer aes xsave avx f16c rdrand lahf_lm abm 3dnowprefetch cpuid_fault epb invpcid_single pti retpoline intel_pt rsb_ctxsw spec_ctrl tpr_shadow vnmi flexpriority ept vpid fsgsbase tsc_adjust bmi1 avx2 smep bmi2 erms invpcid mpx rdseed adx smap clflushopt xsaveopt xsavec xgetbv1 xsaves dtherm ida arat pln pts hwp hwp_notify hwp_act_window hwp_epp\nbugs\t\t: cpu_meltdown spectre_v1 spectre_v2\nbogomips\t: 3984.00\nclflush size\t: 64\ncache_alignment\t: 64\naddress sizes\t: 39 bits physical, 48 bits virtual\npower management:\n\nprocessor\t: 1\nvendor_id\t: GenuineIntel\ncpu family\t: 6\nmodel\t\t: 142\nmodel name\t: Intel(R) Core(TM) i7-8550U CPU @ 1.80GHz\nstepping\t: 10\nmicrocode\t: 0x84\ncpu MHz\t\t: 3100.304\ncache size\t: 8192 KB\nphysical id\t: 0\nsiblings\t: 8\ncore id\t\t: 1\ncpu cores\t: 4\napicid\t\t: 2\ninitial apicid\t: 2\nfpu\t\t: yes\nfpu_exception\t: yes\ncpuid level\t: 22\nwp\t\t: yes\nflags\t\t: fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts acpi mmx fxsr sse sse2 ss ht tm pbe syscall nx pdpe1gb rdtscp lm constant_tsc art arch_perfmon pebs bts rep_good nopl xtopology nonstop_tsc cpuid aperfmperf tsc_known_freq pni pclmulqdq dtes64 monitor ds_cpl vmx est tm2 ssse3 sdbg fma cx16 xtpr pdcm pcid sse4_1 sse4_2 x2apic movbe popcnt tsc_deadline_timer aes xsave avx f16c rdrand lahf_lm abm 3dnowprefetch cpuid_fault epb invpcid_single pti retpoline intel_pt rsb_ctxsw spec_ctrl tpr_shadow vnmi flexpriority ept vpid fsgsbase tsc_adjust bmi1 avx2 smep bmi2 erms invpcid mpx rdseed adx smap clflushopt xsaveopt xsavec xgetbv1 xsaves dtherm ida arat pln pts hwp hwp_notify hwp_act_window hwp_epp\nbugs\t\t: cpu_meltdown spectre_v1 spectre_v2\nbogomips\t: 3984.00\nclflush size\t: 64\ncache_alignment\t: 64\naddress sizes\t: 39 bits physical, 48 bits virtual\npower management:\n\nprocessor\t: 2\nvendor_id\t: GenuineIntel\ncpu family\t: 6\nmodel\t\t: 142\nmodel name\t: Intel(R) Core(TM) i7-8550U CPU @ 1.80GHz\nstepping\t: 10\nmicrocode\t: 0x84\ncpu MHz\t\t: 3100.010\ncache size\t: 8192 KB\nphysical id\t: 0\nsiblings\t: 8\ncore id\t\t: 2\ncpu cores\t: 4\napicid\t\t: 4\ninitial apicid\t: 4\nfpu\t\t: yes\nfpu_exception\t: yes\ncpuid level\t: 22\nwp\t\t: yes\nflags\t\t: fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts acpi mmx fxsr sse sse2 ss ht tm pbe syscall nx pdpe1gb rdtscp lm constant_tsc art arch_perfmon pebs bts rep_good nopl xtopology nonstop_tsc cpuid aperfmperf tsc_known_freq pni pclmulqdq dtes64 monitor ds_cpl vmx est tm2 ssse3 sdbg fma cx16 xtpr pdcm pcid sse4_1 sse4_2 x2apic movbe popcnt tsc_deadline_timer aes xsave avx f16c rdrand lahf_lm abm 3dnowprefetch cpuid_fault epb invpcid_single pti retpoline intel_pt rsb_ctxsw spec_ctrl tpr_shadow vnmi flexpriority ept vpid fsgsbase tsc_adjust bmi1 avx2 smep bmi2 erms invpcid mpx rdseed adx smap clflushopt xsaveopt xsavec xgetbv1 xsaves dtherm ida arat pln pts hwp hwp_notify hwp_act_window hwp_epp\nbugs\t\t: cpu_meltdown spectre_v1 spectre_v2\nbogomips\t: 3984.00\nclflush size\t: 64\ncache_alignment\t: 64\naddress sizes\t: 39 bits physical, 48 bits virtual\npower management:\n\nprocessor\t: 3\nvendor_id\t: GenuineIntel\ncpu family\t: 6\nmodel\t\t: 142\nmodel name\t: Intel(R) Core(TM) i7-8550U CPU @ 1.80GHz\nstepping\t: 10\nmicrocode\t: 0x84\ncpu MHz\t\t: 3100.194\ncache size\t: 8192 KB\nphysical id\t: 0\nsiblings\t: 8\ncore id\t\t: 3\ncpu cores\t: 4\napicid\t\t: 6\ninitial apicid\t: 6\nfpu\t\t: yes\nfpu_exception\t: yes\ncpuid level\t: 22\nwp\t\t: yes\nflags\t\t: fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts acpi mmx fxsr sse sse2 ss ht tm pbe syscall nx pdpe1gb rdtscp lm constant_tsc art arch_perfmon pebs bts rep_good nopl xtopology nonstop_tsc cpuid aperfmperf tsc_known_freq pni pclmulqdq dtes64 monitor ds_cpl vmx est tm2 ssse3 sdbg fma cx16 xtpr pdcm pcid sse4_1 sse4_2 x2apic movbe popcnt tsc_deadline_timer aes xsave avx f16c rdrand lahf_lm abm 3dnowprefetch cpuid_fault epb invpcid_single pti retpoline intel_pt rsb_ctxsw spec_ctrl tpr_shadow vnmi flexpriority ept vpid fsgsbase tsc_adjust bmi1 avx2 smep bmi2 erms invpcid mpx rdseed adx smap clflushopt xsaveopt xsavec xgetbv1 xsaves dtherm ida arat pln pts hwp hwp_notify hwp_act_window hwp_epp\nbugs\t\t: cpu_meltdown spectre_v1 spectre_v2\nbogomips\t: 3984.00\nclflush size\t: 64\ncache_alignment\t: 64\naddress sizes\t: 39 bits physical, 48 bits virtual\npower management:\n\nprocessor\t: 4\nvendor_id\t: GenuineIntel\ncpu family\t: 6\nmodel\t\t: 142\nmodel name\t: Intel(R) Core(TM) i7-8550U CPU @ 1.80GHz\nstepping\t: 10\nmicrocode\t: 0x84\ncpu MHz\t\t: 3100.041\ncache size\t: 8192 KB\nphysical id\t: 0\nsiblings\t: 8\ncore id\t\t: 0\ncpu cores\t: 4\napicid\t\t: 1\ninitial apicid\t: 1\nfpu\t\t: yes\nfpu_exception\t: yes\ncpuid level\t: 22\nwp\t\t: yes\nflags\t\t: fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts acpi mmx fxsr sse sse2 ss ht tm pbe syscall nx pdpe1gb rdtscp lm constant_tsc art arch_perfmon pebs bts rep_good nopl xtopology nonstop_tsc cpuid aperfmperf tsc_known_freq pni pclmulqdq dtes64 monitor ds_cpl vmx est tm2 ssse3 sdbg fma cx16 xtpr pdcm pcid sse4_1 sse4_2 x2apic movbe popcnt tsc_deadline_timer aes xsave avx f16c rdrand lahf_lm abm 3dnowprefetch cpuid_fault epb invpcid_single pti retpoline intel_pt rsb_ctxsw spec_ctrl tpr_shadow vnmi flexpriority ept vpid fsgsbase tsc_adjust bmi1 avx2 smep bmi2 erms invpcid mpx rdseed adx smap clflushopt xsaveopt xsavec xgetbv1 xsaves dtherm ida arat pln pts hwp hwp_notify hwp_act_window hwp_epp\nbugs\t\t: cpu_meltdown spectre_v1 spectre_v2\nbogomips\t: 3984.00\nclflush size\t: 64\ncache_alignment\t: 64\naddress sizes\t: 39 bits physical, 48 bits virtual\npower management:\n\nprocessor\t: 5\nvendor_id\t: GenuineIntel\ncpu family\t: 6\nmodel\t\t: 142\nmodel name\t: Intel(R) Core(TM) i7-8550U CPU @ 1.80GHz\nstepping\t: 10\nmicrocode\t: 0x84\ncpu MHz\t\t: 3100.069\ncache size\t: 8192 KB\nphysical id\t: 0\nsiblings\t: 8\ncore id\t\t: 1\ncpu cores\t: 4\napicid\t\t: 3\ninitial apicid\t: 3\nfpu\t\t: yes\nfpu_exception\t: yes\ncpuid level\t: 22\nwp\t\t: yes\nflags\t\t: fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts acpi mmx fxsr sse sse2 ss ht tm pbe syscall nx pdpe1gb rdtscp lm constant_tsc art arch_perfmon pebs bts rep_good nopl xtopology nonstop_tsc cpuid aperfmperf tsc_known_freq pni pclmulqdq dtes64 monitor ds_cpl vmx est tm2 ssse3 sdbg fma cx16 xtpr pdcm pcid sse4_1 sse4_2 x2apic movbe popcnt tsc_deadline_timer aes xsave avx f16c rdrand lahf_lm abm 3dnowprefetch cpuid_fault epb invpcid_single pti retpoline intel_pt rsb_ctxsw spec_ctrl tpr_shadow vnmi flexpriority ept vpid fsgsbase tsc_adjust bmi1 avx2 smep bmi2 erms invpcid mpx rdseed adx smap clflushopt xsaveopt xsavec xgetbv1 xsaves dtherm ida arat pln pts hwp hwp_notify hwp_act_window hwp_epp\nbugs\t\t: cpu_meltdown spectre_v1 spectre_v2\nbogomips\t: 3984.00\nclflush size\t: 64\ncache_alignment\t: 64\naddress sizes\t: 39 bits physical, 48 bits virtual\npower management:\n\nprocessor\t: 6\nvendor_id\t: GenuineIntel\ncpu family\t: 6\nmodel\t\t: 142\nmodel name\t: Intel(R) Core(TM) i7-8550U CPU @ 1.80GHz\nstepping\t: 10\nmicrocode\t: 0x84\ncpu MHz\t\t: 3100.007\ncache size\t: 8192 KB\nphysical id\t: 0\nsiblings\t: 8\ncore id\t\t: 2\ncpu cores\t: 4\napicid\t\t: 5\ninitial apicid\t: 5\nfpu\t\t: yes\nfpu_exception\t: yes\ncpuid level\t: 22\nwp\t\t: yes\nflags\t\t: fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts acpi mmx fxsr sse sse2 ss ht tm pbe syscall nx pdpe1gb rdtscp lm constant_tsc art arch_perfmon pebs bts rep_good nopl xtopology nonstop_tsc cpuid aperfmperf tsc_known_freq pni pclmulqdq dtes64 monitor ds_cpl vmx est tm2 ssse3 sdbg fma cx16 xtpr pdcm pcid sse4_1 sse4_2 x2apic movbe popcnt tsc_deadline_timer aes xsave avx f16c rdrand lahf_lm abm 3dnowprefetch cpuid_fault epb invpcid_single pti retpoline intel_pt rsb_ctxsw spec_ctrl tpr_shadow vnmi flexpriority ept vpid fsgsbase tsc_adjust bmi1 avx2 smep bmi2 erms invpcid mpx rdseed adx smap clflushopt xsaveopt xsavec xgetbv1 xsaves dtherm ida arat pln pts hwp hwp_notify hwp_act_window hwp_epp\nbugs\t\t: cpu_meltdown spectre_v1 spectre_v2\nbogomips\t: 3984.00\nclflush size\t: 64\ncache_alignment\t: 64\naddress sizes\t: 39 bits physical, 48 bits virtual\npower management:\n\nprocessor\t: 7\nvendor_id\t: GenuineIntel\ncpu family\t: 6\nmodel\t\t: 142\nmodel name\t: Intel(R) Core(TM) i7-8550U CPU @ 1.80GHz\nstepping\t: 10\nmicrocode\t: 0x84\ncpu MHz\t\t: 3100.078\ncache size\t: 8192 KB\nphysical id\t: 0\nsiblings\t: 8\ncore id\t\t: 3\ncpu cores\t: 4\napicid\t\t: 7\ninitial apicid\t: 7\nfpu\t\t: yes\nfpu_exception\t: yes\ncpuid level\t: 22\nwp\t\t: yes\nflags\t\t: fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts acpi mmx fxsr sse sse2 ss ht tm pbe syscall nx pdpe1gb rdtscp lm constant_tsc art arch_perfmon pebs bts rep_good nopl xtopology nonstop_tsc cpuid aperfmperf tsc_known_freq pni pclmulqdq dtes64 monitor ds_cpl vmx est tm2 ssse3 sdbg fma cx16 xtpr pdcm pcid sse4_1 sse4_2 x2apic movbe popcnt tsc_deadline_timer aes xsave avx f16c rdrand lahf_lm abm 3dnowprefetch cpuid_fault epb invpcid_single pti retpoline intel_pt rsb_ctxsw spec_ctrl tpr_shadow vnmi flexpriority ept vpid fsgsbase tsc_adjust bmi1 avx2 smep bmi2 erms invpcid mpx rdseed adx smap clflushopt xsaveopt xsavec xgetbv1 xsaves dtherm ida arat pln pts hwp hwp_notify hwp_act_window hwp_epp\nbugs\t\t: cpu_meltdown spectre_v1 spectre_v2\nbogomips\t: 3984.00\nclflush size\t: 64\ncache_alignment\t: 64\naddress sizes\t: 39 bits physical, 48 bits virtual\npower management:\n\n"}
          resp-test             (-> session-admin
                                  (request base-uri
                                           :request-method :post
                                           :body (json/write-str create-test-callback))
                                  (ltu/body->edn)
                                  (ltu/is-status 201))
          id-test               (get-in resp-test [:response :body :resource-id])
          location-test         (str p/service-context (-> resp-test ltu/location))
          test-uri              (str p/service-context id-test)]

      (is (= location-test test-uri))

      ;; admin should be able to see the callback
      (-> session-admin
          (request test-uri)
          (ltu/body->edn)
          (ltu/is-status 200)
          (ltu/is-operation-present "delete")
          ;(ltu/is-operation-absent "edit")
          ;(ltu/is-operation-present (:execute c/action-uri))
      )

      ;; user cannot directly see the callback
      (-> session-user
          (request test-uri)
          (ltu/body->edn)
          (ltu/is-status 403))

      ;; check contents and editing
      (let [reread-test-callback (-> session-admin
                                     (request test-uri)
                                     (ltu/body->edn)
                                     (ltu/is-status 200)
                                     :response
                                     :body)
            original-updated-timestamp (:updated reread-test-callback)]

        ;(is (= (ltu/strip-unwanted-attrs reread-test-callback)
        ;       (ltu/strip-unwanted-attrs (assoc create-test-callback :state "WAITING"))))

        ;; mark callback as failed
        (utils/callback-failed! id-test)
        (let [callback (-> session-admin
                           (request test-uri)
                           (ltu/body->edn)
                           (ltu/is-status 200)
                           (ltu/is-operation-absent (:execute c/action-uri))
                           :response
                           :body)]
          (is (= "FAILED" (:state callback)))
          (is (not= original-updated-timestamp (:updated callback))))

        ;; mark callback as succeeded
        (utils/callback-succeeded! id-test)
        (let [callback (-> session-admin
                           (request test-uri)
                           (ltu/body->edn)
                           (ltu/is-status 200)
                           (ltu/is-operation-absent (:execute c/action-uri))
                           :response
                           :body)]
          (is (= "SUCCEEDED" (:state callback)))
          (is (not= original-updated-timestamp (:updated callback)))))

      ;; search
      (-> session-admin
          (request base-uri
                   :request-method :put
                   :body (json/write-str {}))
          (ltu/body->edn)
          (ltu/is-count 1)
          (ltu/is-status 200))

      ;; delete
      (-> session-anon
          (request test-uri
                   :request-method :delete)
          (ltu/body->edn)
          (ltu/is-status 403))

      (-> session-user
          (request test-uri
                   :request-method :delete)
          (ltu/body->edn)
          (ltu/is-status 403))

      (-> session-admin
          (request test-uri
                   :request-method :delete)
          (ltu/body->edn)
          (ltu/is-status 200))

      ;; callback must be deleted
      (-> session-admin
          (request test-uri
                   :request-method :delete)
          (ltu/body->edn)
          (ltu/is-status 404)))))