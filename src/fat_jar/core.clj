(ns fat-jar.core
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def cli-options
  [["-o" "--output DIRECTORY" "Folder where to write the file"]
   ["-t" "--type TYPE" "type of input"]
   ["-h" "--help"]])

(defn -main [& args]
  (let [options (parse-opts args cli-options)
         print-help #(or (not (empty? (:errors %))) (:help %))]
    (cond
      (print-help options) (println (:summary options))
      :else (println (:summary options)))))