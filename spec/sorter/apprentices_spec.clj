(ns sorter.apprentices-spec
  (:require [speclj.core :refer :all]
            [sorter.apprentices :refer :all]
            [sorter.duties :refer :all]
            [sorter.data :refer :all]))

(describe "picker"

  (it "adds foreman? boolean to apprentices map"
    (should= true    
      (:foreman? (first (annotate-foreman apprentices "Taryn")))))

(it "removes unavailable apprentices"
    (should= '({:name "LaToya", :prev-duty "chairs", :av-count 1} {:name "Zach O", :prev-duty "greeter", :av-count 1})     
      (take 2 (remove-unavailable apprentices ["Taryn" "Tom"]))))

(it "adds duty-options list to apprentices map"
    (should= '("chairs" "av" "greeter" "standup")     
      (:duty-options (first (annotate-options apprentices duties)))))

(it "assigns utility to foreman in apprentices map"
    (should= "utility"    
      (:assigned-to (first (assign-foreman (annotate-options (annotate-foreman apprentices "Taryn") duties))))))

(it "returns a list of strings for all required duties"
    (should= '("chairs" "chairs" "av" "av" "food" "food" "greeter" "standup")      
      (required-duties duties)))

(it "assigns required duties"
    (should= 12 
      (count (assign-duty (annotate-options apprentices duties) (required-duties duties)))))

(it "returns false when some duties have not yet been assigned"
    (should= false 
      (all-duties-assigned? (assign-duty (annotate-options apprentices duties) (required-duties duties)) (required-duties duties))))

(it "returns false unless all duties have been assigned"
    (should= false 
      (all-apprentices-have-assignments? (assign-duty (annotate-options apprentices duties) (required-duties duties)))))

(it "solves"
    (should= 9 
      (count (run-sorter apprentices duties ["LaToya", "Cassie", "Tom"] "Taryn")))))
