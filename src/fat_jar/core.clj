(ns fat-jar.core
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def cli-options
  [["-o" "--output DIRECTORY" "Folder where to write the file"
     :missing "output option is missing"]
   ["-t" "--type TYPE" "type of input"
     :missing "type option is missing"]

   ["-h" "--help"]])

(defn -main [& args]
  (let [options (parse-opts args cli-options :strict true :missing true)
         errors? #(not (empty? (:errors %)))
         help? #(:help %)
         print-help #(println (:summary %))]
         ; (println options)
    (cond
      (errors? options) (do (print-help options) (print (:errors options)))
      (help? options) (print-help options)
      :else (print-help options))))