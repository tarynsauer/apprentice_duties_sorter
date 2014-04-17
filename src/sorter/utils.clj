(ns sorter.utils)

(defn in? [items-list item]  
  (if (nil? (some #(= item %) items-list))
    false
    true)) 

(defn update-apprentice [apprentice]
  {:name (:name apprentice)
   :prev-duty (:assigned-to apprentice)
   :av-count (if (= (:assigned-to apprentice) "av")
                 (inc (:av-count apprentice))
                 (:av-count apprentice)) })

(defn save-results [apprentices]
    (let [updated-apprentices '()]
        (for [apprentice apprentices]
            (conj updated-apprentices (update-apprentice apprentice)))))
