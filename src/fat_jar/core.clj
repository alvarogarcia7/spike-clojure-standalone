(ns fat-jar.core
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def cli-options
  [["-o" "--output DIRECTORY" "Folder where to write the file"]
   ["-t" "--type TYPE" "type of input"]
   ["-h" "--help"]])

(defn -main [& args]
  (let [options (parse-opts args cli-options)
         errors? #(not (empty? (:errors %)))
         help? #(:help %)
         print-help #(println (:summary %))]
    (cond
      (errors? options) (print-help options)
      (help? options) (print-help options)
      :else (print-help options))))