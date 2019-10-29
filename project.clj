(def netty-version "4.1.42.Final")

(def netty-modules
  '[transport
    transport-native-epoll
    codec
    codec-http
    handler
    handler-proxy
    resolver
    resolver-dns])

(def other-dependencies
  '[[org.clojure/tools.logging "0.4.1" :exclusions [org.clojure/clojure]]
    [manifold "0.1.9-alpha3"]
    [byte-streams "0.2.5-alpha2"]
    [potemkin "0.4.5"]])

(defproject social.kitsune/iny "1.0.0"
  :description "a framework for asynchronous communication"
  :repositories {"jboss" "https://repository.jboss.org/nexus/content/groups/public/"
                 "sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  :license {:name "MIT License"}
  :dependencies ~(concat
                   other-dependencies
                   (map
                     #(vector (symbol "io.netty" (str "netty-" %)) netty-version)
                     netty-modules))
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.10.1"]
                                  [criterium "0.4.5"]
                                  [metosin/jsonista "0.2.5"]
                                  [org.slf4j/slf4j-simple "1.7.28"]
                                  [com.cognitect/transit-clj "0.8.319"]]
                   :plugins [[lein-ancient "0.6.15"
                              :exclusions [org.clojure/clojure]]]}}
  :codox {:src-dir-uri "https://github.com/ztellman/aleph/tree/master/"
          :src-linenum-anchor-prefix "L"
          :defaults {:doc/format :markdown}
          :include [iny.tcp
                    iny.udp
                    iny.http
                    iny.flow]
          :output-dir "doc"}
  :plugins [[lein-codox "0.9.4"]
            [lein-marginalia "0.9.0"]
            [ztellman/lein-cljfmt "0.1.10"]]
  :java-source-paths ["src/iny/utils"]
  :javac-options ["-target" "1.7", "-source" "1.7"]
  :cljfmt {:indents {#".*" [[:inner 0]]}}
  :test-selectors {:default #(not
                               (some #{:benchmark :stress}
                                 (cons (:tag %) (keys %))))
                   :benchmark :benchmark
                   :stress :stress
                   :all (constantly true)}
  :jvm-opts ^:replace ["-server"
                       #_"-Xmx256m"
                       "-Xmx2g"
                       "-XX:+HeapDumpOnOutOfMemoryError"
                       #_"-XX:+PrintCompilation"
                       #_"-XX:+UnlockDiagnosticVMOptions"
                       #_"-XX:+PrintInlining"]
  :global-vars {*warn-on-reflection* true})
