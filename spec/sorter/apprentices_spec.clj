(ns sorter.apprentices-spec
  (:require [speclj.core :refer :all]
            [sorter.apprentices :refer :all]
            [sorter.duties :refer :all]))

(describe "picker"
(def apprentices-list [{:name "Taryn", :prev-duty "food", :av-count 3} 
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
                  {:name "Cassie", :prev-duty "food", :av-count 0}
])

  (it "adds foreman? boolean to apprentices map"
    (should= true    
      (:foreman? (first (annotate-foreman apprentices-list "Taryn")))))

(it "removes unavailable apprentices"
    (should= '({:name "LaToya", :prev-duty "chairs", :av-count 1} {:name "Zach O", :prev-duty "greeter", :av-count 1})     
      (take 2 (remove-unavailable apprentices-list ["Taryn" "Tom"]))))

(it "adds duty-options list to apprentices map"
    (should= '("chairs" "av" "greeter" "standup")     
      (:duty-options (first (annotate-options apprentices-list duties)))))

(it "assigns utility to foreman in apprentices map"
    (should= "utility"    
      (:assigned-to (first (assign-foreman (annotate-options (annotate-foreman apprentices-list "Taryn") duties))))))

(it "returns a list of strings for all required duties"
    (should= '("chairs" "chairs" "av" "av" "food" "food" "greeter" "standup")      
      (required-duties duties)))

(it "assigns required duties"
    (should= 12 
      (count (assign-duty (annotate-options apprentices-list duties) (required-duties duties)))))

(it "returns the name of somebody on the av-experienced list"
    (should= 12
       (count (assign-av (annotate-options apprentices-list duties) av-experienced))))

(it "returns false when some duties have not yet been assigned"
    (should= false 
      (all-duties-assigned? (assign-duty (annotate-options apprentices-list duties) (required-duties duties)) (required-duties duties))))

(it "returns false unless all duties have been assigned"
    (should= false 
      (all-apprentices-have-assignments? (assign-duty (annotate-options apprentices-list duties) (required-duties duties)))))

(it "solves"
    (should= 9 
      (count (solve apprentices-list duties ["LaToya", "Cassie", "Tom"] "Taryn")))))
