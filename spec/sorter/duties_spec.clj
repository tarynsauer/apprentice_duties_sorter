(ns sorter.duties-spec
  (:require [speclj.core :refer :all]
            [sorter.duties :refer :all]))

(it "lists all duties"
    (should= '("chairs" "av" "food" "greeter" "standup")    
      (duties-options duties)))
 
(it "returns a list of strings for all optional duties"
    (should= '("chairs" "av" "food" "standup")
      (optional-duties duties)))
 
