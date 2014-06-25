(defproject cav/cljcav "0.1.1"
  :description "General library complementing clojure.core and cljs.core."
  :url "https://github.com/sethyuan/cljcav"
  :license {:name "MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :profiles {:dev {:dependencies [[org.clojure/clojurescript "0.0-2234"]
                                  [criterium "0.4.3"]]}}
  :plugins [[com.keminglabs/cljx "0.4.0"]
            [lein-cljsbuild "1.0.3"]
            [com.cemerick/clojurescript.test "0.3.1"]
            [codox "0.8.9"]]
  :hooks [cljx.hooks]
  :source-paths ["src/clj" "src/cljs"]
  :test-paths ["test/clj" "target/tests"]
  :codox {:language :clojure
          :src-dir-uri "https://github.com/sethyuan/cljcav/blob/master/"
          :src-linenum-anchor-prefix "L"
          :src-uri-mapping {#"target/classes" #(str "src/" % "x")}}
  :cljx {:builds [{:source-paths ["src/cljx"]
                   :output-path "target/classes"
                   :rules :clj}
                  {:source-paths ["src/cljx"]
                   :output-path "target/classes"
                   :rules :cljs}
                  {:source-paths ["test/cljx"]
                   :output-path "target/tests"
                   :rules :clj}
                  {:source-paths ["test/cljx"]
                   :output-path "target/tests"
                   :rules :cljs}]}
  :cljsbuild {:builds [{:id "cljcav-test"
                        :source-paths ["src/cljs" "test/cljs" "target/classes" "target/tests"]
                        :compiler {:output-to "target/cljcav-test.js"
                                   :output-dir "target/cljcav-test-out"
                                   :source-map "target/cljcav-test.js.map"
                                   :optimizations :none
                                   :target :nodejs
                                   :pretty-print true}}]
              :test-commands {"unit-tests" ["node" "test/node_runner.js"
                                            "test/run.js"]}} 
  :aliases {"cljstest!" ["do" ["cljsbuild" "once"] ["cljsbuild" "test"]]}
  :jvm-opts ^:replace [])
