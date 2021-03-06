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

;; Adds av to given apprentice.
(defn annotate-av [apprentices apprentice-name]
  (map (fn [apprentice]
      (if (and (= (:name apprentice) apprentice-name)
                 (nil? (:assigned-to apprentice)))
             (assoc apprentice :assigned-to "av")
             apprentice)) apprentices))

(defn av-experienced [apprentices]  ;; Refactored to remove repetition to remove repetition.
  (filter (fn [apprentice] (>= (:av-count apprentice) 3)) apprentices))

(defn av-novice [apprentices]
  (filter (fn [apprentice] (< (:av-count apprentice) 3)) apprentices))

(defn assign-av [apprentices by-level] ;; Randomly assignes av to one apprentice in list.
  (let [potential-assignees (by-level apprentices)]
      (annotate-av apprentices (:name (first (shuffle potential-assignees))))))

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
    (assign-av av-experienced)
    (assign-av av-novice)
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
