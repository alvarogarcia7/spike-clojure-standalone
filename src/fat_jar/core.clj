(ns fat-jar.core
  (:require [clojure.tools.cli :refer [parse-opts]]
                 [clojure.java.io :as io]
                 [clojure.tools.reader.edn :as edn]
                 [clj-json.core :as json]
                 [clojurewerkz.propertied.properties :as p])
  (:gen-class))
(import '(java.io StringReader BufferedReader))

; guide for cli-options: https://github.com/clojure/tools.cli/blob/master/src/test/clojure/clojure/tools/cli_test.clj
(def cli-options
  [["-o" "--output DIRECTORY" "Folder where to write the file"
     :missing "output option is missing"]
   ["-t" "--type TYPE" "type of input"
     :missing "type option is missing"]

   ["-h" "--help"]])

(defn select- [data-mapping coll]
  """Navigate using the selector"""
  (let [selector (:map data-mapping)
         output (:to data-mapping)]
    {:output output
      :v(reduce #(% %2) coll selector)}))

(defn write-file [filename payload]
   (with-open [w (clojure.java.io/writer filename)]
  (doseq [line payload]
    (.write w line)
    (.newLine w))))

(let [pl (p/load-from {"key" "value"})]
  (p/properties->map pl true))

(def config
  (p/properties->map 
    (->> "./mapper.properties" io/file p/load-from)
    true))

(defn process []
  (do 
        (let [mappings (->> (config :input.mappings)
                     slurp
                     edn/read-string)

               user-data (->> (config :input.data)
                     slurp
                     StringReader.
                     BufferedReader.
                     json/parsed-seq
                     first)]

               (letfn [(select-values [coll mapping]
                            (select- mapping coll))
                         (format-value [value]
                            (str (:output value) "|" (:v value)))]
       
       (->>  mappings
          (map #(select-values user-data %))
          (map format-value)
          (write-file (config :output.filename)))))))

(defn -main [& args]
  (let [options (parse-opts args cli-options :strict true :missing true)
         errors? #(not (empty? (:errors %)))
         help? #(:help %)
         print-help #(do (println "usage:") (println (:summary %)))]
    (cond
      (errors? options) (do (print-help options) (println (:errors options)))
      (help? options) (print-help options)
      :else (process))))