(ns sorter.utils)

(defn in? [items-list item]  
  (not (nil? (some #(= item %) items-list))))
