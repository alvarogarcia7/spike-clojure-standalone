(defproject fat-jar "0.2.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                          [org.clojure/tools.cli "0.3.3"]
                          [org.clojure/tools.reader "1.0.0-alpha1"]
                          [clojurewerkz/propertied "1.2.0"]
                          [clj-json "0.5.3"]]
  :main fat-jar.core
  :aot [fat-jar.core]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
