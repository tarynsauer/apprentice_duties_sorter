(ns sorter.duties)

(def duties [{:name "chairs", :min-needed 2, :max-needed 3} 
             {:name "av", :min-needed 2, :max-needed 3} 
             {:name "food", :min-needed 2, :max-needed 3} 
             {:name "greeter", :min-needed 1, :max-needed 1}
             {:name "standup", :min-needed 1, :max-needed 2}])

(def foreman-assignment "utility") 

(defn duties-options [duties]
  (for [duty duties]
    (:name duty)))

(defn required-duties [duties] 
   (let [required-list '()]
      (flatten 
         (map (fn [duty]
             (into required-list (repeat (:min-needed duty) (:name duty)))) duties))))

(defn optional-duties [duties]
  (let [optional-list '()]
     (flatten 
        (map (fn [duty]
           (into optional-list (repeat (- (:max-needed duty) (:min-needed duty)) (:name duty)))) duties))))

(defn av-experienced [apprentices]
  (filter (fn [apprentice] (>= (:av-count apprentice) 3)) apprentices))

(defn av-novice [apprentices]
  (filter (fn [apprentice] (< (:av-count apprentice) 3)) apprentices))
