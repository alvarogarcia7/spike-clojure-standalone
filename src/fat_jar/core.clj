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
   ["-t" "--template TYPE" "template for input"
     :missing "template option is missing"]

   ["-h" "--help"]])

(defn select- [data-mapping coll]
  """Navigate using the selector"""
  (let [selector (:map data-mapping)
         output (:to data-mapping)
         parent (reduce #(% %2) coll (butlast selector))
         remaining (last selector)
         many-from-list (map #(get % remaining) parent)
         many-from-single (list (get parent remaining))
         value (if (= (list nil) many-from-single) many-from-list many-from-single)
         multiple (if (= (list nil) many-from-single) "@" "")]
         (do
            ;(println remaining)
            ;(println parent)
            ;(println many-from-list)
            ;(println many-from-single)
            {:output output
               :v value
              :multiple multiple}
             )))

; (select- {:map ["vos" "name"] :to "v7"} user-data)
; (select- {:map ["a"] :to "v7"} user-data)

(defn write-file [filename payload]
   (with-open [w (clojure.java.io/writer filename)]
  (doseq [line payload]
    (.write w line)
    (.newLine w))))

(def config
  (p/properties->map 
    (->> "./mapper.properties" io/file p/load-from)
    true))

(defn at-output-folder [options filename]
  (str (:output options) "/" filename))

(defn process [options]
  (do 
        (let [destination-file (at-output-folder options (config :output.filename))

               mappings (get (->> (config :input.mappings)
                                    slurp
                                    edn/read-string) (:template options))

               user-data (->> (config :input.data)
                     slurp
                     StringReader.
                     BufferedReader.
                     json/parsed-seq
                     first)]

               (letfn [(select-values [coll mapping]
                            (select- mapping coll))
                         (format-value [value]
                            (str (:multiple value) (:output value) "|" (clojure.string/join "|" (:v value))))]
       
       (->>  mappings
          (map #(select-values user-data %))
          (map format-value)
          (write-file destination-file))))))

(defn -main [& args]
  (let [options (parse-opts args cli-options :strict true :missing true)
         errors? #(not (empty? (:errors %)))
         help? #(:help %)
         print-help #(do (println "usage:") (println (:summary %)))]
    (cond
      (errors? options) (do (print-help options) (println (:errors options)))
      (help? options) (print-help options)
      :else (process (:options options)))))