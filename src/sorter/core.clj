(ns sorter.core
  (:gen-class)
  (:require [sorter.apprentices :refer :all] 
            [sorter.duties :refer :all]
            [sorter.utils :refer :all]))

(def test-apprentices [{:name "Taryn", :prev-duty "food", :av-count 3} 
                       {:name "Tom", :prev-duty "av", :av-count 3} 
                       {:name "LaToya", :prev-duty "chairs", :av-count 1} 
                       {:name "Zach O", :prev-duty "greeter", :av-count 1} 
                       {:name "Elizabeth", :prev-duty "food", :av-count 3} 
                       {:name "Meagan", :prev-duty "av", :av-count 3} 
                       {:name "Zach D", :prev-duty "chairs", :av-count 1} 
                       {:name "Mike K", :prev-duty "greeter", :av-count 1} 
                       {:name "Kyle", :prev-duty "av", :av-count 3} 
                       {:name "Mike D", :prev-duty "chairs", :av-count 1} 
                       {:name "Nelsol", :prev-duty "utility", :av-count 1}
                       {:name "Cassie", :prev-duty "food", :av-count 0}])

(defn -main [] 
  (println "New apprentice duty assignments:")
  (solve test-apprentices duties '() "Taryn"))
