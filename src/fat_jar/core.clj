(ns fat-jar.core
  (:require [clojure.tools.cli :refer [parse-opts]]
                 [clojure.java.io :as io]
                 [clojure.tools.reader.edn :as edn]
                 [clj-json.core :as json])
  (:gen-class))
(import '(java.io StringReader BufferedReader))

; guide for cli-options: https://github.com/clojure/tools.cli/blob/master/src/test/clojure/clojure/tools/cli_test.clj
(def cli-options
  [["-o" "--output DIRECTORY" "Folder where to write the file"
     :missing "output option is missing"]
   ["-t" "--type TYPE" "type of input"
     :missing "type option is missing"]

   ["-h" "--help"]])

(defn select- [selector coll]
  """Navigate using the selector"""
  (reduce #(% %2) coll selector))

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
        (let [mappings (->> "./mappings.edn"
                     slurp
                     edn/read-string)

              selections (->> mappings
                      (map :map))

               user-data (->> "./user.json"
                     slurp
                     StringReader.
                     BufferedReader.
                     json/parsed-seq
                     first)]
        (->> mappings
          println)

        (->> user-data
          println)

        (->> selections
          println)

       
       (->>  (map #(select- % user-data) selections)
          println)

  
     
     ))
      )))