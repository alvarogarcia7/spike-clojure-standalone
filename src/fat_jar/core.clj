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

(def config
  (->> "./mapper.properties"
    io/file
    p/load-from
    #(p/properties->map % true)))

(defn -main [& args]
  (let [options (parse-opts args cli-options :strict true :missing true)
         errors? #(not (empty? (:errors %)))
         help? #(:help %)
         print-help #(do (println "usage:") (println (:summary %)))]
         ; (println options)
    (cond
      (errors? options) (do (print-help options) (println (:errors options)))
      (help? options) (print-help options)
      :else (do 
        (println "doing stuff")
        (println options)
        (let [mappings (->> (config :input.mappings)
                     slurp
                     edn/read-string)

              selections (->> mappings
                      (map :map))

               user-data (->> (config :input.data)
                     slurp
                     StringReader.
                     BufferedReader.
                     json/parsed-seq
                     first)]
;        (->> mappings
;          println)
;
;        (->> user-data
;          println)
;
;        (->> selections
;          println)

       
       (->>  (map #(select- % user-data) mappings)
          (map #(str (:output %) "|" (:v %)))
          (write-file (config :output.filename)))
     
     ))
      )))