(ns sorter.apprentices
  (:require [sorter.duties :refer :all]
            [sorter.utils :refer :all]
            [sorter.data :refer :all]))

(defn remove-unavailable [apprentices unavailable]
     (let [unavailables-set (set unavailable)]
         (filter (fn [apprentice]
                (not (contains? unavailables-set (:name apprentice))))
              apprentices)))
 
(defn annotate-foreman [apprentices foreman-name] 
   (map (fn [apprentice]
        (assoc apprentice
            :foreman? (= (:name apprentice) foreman-name))) apprentices))

(defn annotate-options [apprentices duties]
  (let [options (duties-options duties)]
     (map (fn [apprentice]
            (assoc apprentice :duty-options (remove #{(:prev-duty apprentice)} (seq options))))
          apprentices)))

(defn assign-foreman [apprentices]
    (let [apprentices apprentices]
        (map (fn [apprentice]
             (if (:foreman? apprentice) 
                 (assoc apprentice :assigned-to foreman-assignment)
                 apprentice)) 
        apprentices)))

(defn assign-duty [apprentices assignments]
  (loop [duties-list (shuffle assignments)
        [a-map & more] (shuffle apprentices)
        acc []]
     (if a-map
       (let [duty (first duties-list) 
             duties (rest duties-list)]
         (recur duties more 
              (if (and (in? (:duty-options a-map) duty) 
                       (= (:assigned-to a-map) nil)) 
                  (conj acc (assoc a-map :assigned-to duty))
                  (conj acc a-map)))) 
       acc)))

;; TODO: Add annotate-av function to assign apprentice to av 
;; (defn annotate-av [apprentices apprentice-name]
;; )

(defn av-experienced [apprentices]  ;; TODO: Refactor to remove repetition.
  (filter (fn [apprentice] (and
                              (>= (:av-count apprentice) 3)
                              (in? (:duty-options apprentice) "av"))) apprentices))

(defn av-novice [apprentices]
  (filter (fn [apprentice] (and
                              (< (:av-count apprentice) 3)
                              (in? (:duty-options apprentice) "av"))) apprentices))

;; TODO: Add assign-av function to assign one apprentice from level list
(defn assign-av [apprentices]
  (annotate-av apprentices (:name (first (shuffle apprentices)))))

(defn all-duties-assigned? [apprentices duties-list]
  (= (sort (remove nil? (filter identity (map :assigned-to apprentices))))
     (sort (conj duties-list foreman-assignment))))
 
(defn all-apprentices-have-assignments? [apprentices]
  (every? :assigned-to apprentices))
 
(defn assign-all-required-duties [apprentices duties]
  (let [orig-apprentices apprentices required-duties (required-duties duties)]
    (loop [apprentices apprentices]
     (let [apprentices (assign-duty apprentices required-duties)]
     (if (all-duties-assigned? apprentices required-duties)
       apprentices
       (recur orig-apprentices))))))

(defn assign-all-remaining-apprentices [apprentices duties]
  (let [orig-apprentices apprentices optional-duties (optional-duties duties)]
    (loop [apprentices apprentices]
    (let [apprentices (assign-duty apprentices optional-duties)] 
     (if (all-apprentices-have-assignments? apprentices)
       apprentices
       (recur orig-apprentices))))))

(defn annotate-assignments [apprentices duties]
  (-> apprentices
    (assign-foreman)
    ;; assign-av for experienced and novice levels
    (assign-all-required-duties duties) 
    (assign-all-remaining-apprentices duties)))
 
(defn run-sorter [apprentices duties unavailable foreman]
 (-> apprentices
    (remove-unavailable unavailable) 
    (annotate-foreman foreman)
    (annotate-options duties)
    (annotate-assignments duties)))

(defn print-results [apprentices]
  (println "\n\n*Duties for today*")
  (doseq [apprentice apprentices] 
    (println (:name apprentice) "-" (:assigned-to apprentice)))
  (println "\n\n"))
