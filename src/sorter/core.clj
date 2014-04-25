(ns sorter.core
  (:gen-class)
  (:require [sorter.apprentices :refer :all] 
            [sorter.duties :refer :all]
            [sorter.data :refer :all]))

(defn -main [] 
  (print-results (run-sorter apprentices duties '("Tom") "Taryn")))
