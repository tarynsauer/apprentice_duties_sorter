(ns sorter.apprentices
  (:require [sorter.duties :refer :all]
            [sorter.utils :refer :all]))

(defn annotate-foreman [apprentices foreman-name] 
   (map (fn [apprentice]
        (assoc apprentice
            :foreman? (= (:name apprentice) foreman-name))) apprentices))

(defn remove-unavailable [apprentices unavailable]
     (let [unavailables-set (set unavailable)]
         (filter (fn [apprentice]
                (not (contains? unavailables-set (:name apprentice))))
              apprentices)))

(defn annotate-options [apprentices duties]
    (let [options (duties-options duties)]
       (map (fn [apprentice]
           (assoc apprentice 
                :duty-options (remove #{(:prev-duty apprentice)} (seq options))))
        apprentices)))

(defn assign-foreman [apprentices]
    (let [apprentices apprentices]
        (map (fn [apprentice]
             (if (:foreman? apprentice) 
                 (assoc apprentice :assigned-to foreman-assignment)
                 apprentice)) 
        apprentices)))

(defn all-duties-assigned? [apprentices duties-list]
  (loop [apprentices apprentices assigned-duties '()]
     (if (empty? apprentices) 
       (= (sort (remove nil? assigned-duties)) (sort (conj duties-list foreman-assignment))) 
       (let [apprentice (first apprentices) apprentices (rest apprentices)]
         (recur apprentices 
              (if-not (= (:assigned-to apprentice) nil) 
                  (conj assigned-duties (:assigned-to apprentice))
                  assigned-duties))))))

(defn all-apprentices-have-assignments? [apprentices]
  (loop [apprentices apprentices assigned-duties '()]
     (if (empty? apprentices )
       (every? identity assigned-duties)
       (let [apprentice (first apprentices) apprentices (rest apprentices)]
         (recur apprentices 
            (conj assigned-duties (:assigned-to apprentice)))))))

(defn assign-duty [apprentices assignments]
  (loop [duties-list (shuffle assignments)
        [a-map & more] (shuffle apprentices)
        acc []]
     (if a-map
       (let [duty (first duties-list) duties (rest duties-list)]
         (recur duties more 
              (if (and (in? (:duty-options a-map) duty) 
                       (= (:assigned-to a-map) nil)) 
                  (conj acc (assoc a-map :assigned-to duty))
                  (conj acc a-map)))) acc)))

(defn annotate-av [apprentices apprentice-name]
    (map (fn [apprentice]
      (if (and (= (:name apprentice) apprentice-name)
               (= (:assigned-to apprentice) nil)) 
                  (assoc apprentice :assigned-to "av")
                  apprentice)) apprentices))

(defn assign-av [apprentices level]
  (let [potential-assignees (level apprentices)]
      (annotate-av apprentices (:name (first (shuffle potential-assignees))))))

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

(defn print-results [apprentices]
  (for [apprentice apprentices] 
    (println (:name apprentice)":" (:assigned-to apprentice))))

(defn annotate-assignments [apprentices duties]
  (-> apprentices
    (assign-foreman)
    (assign-av av-experienced)
    (assign-av av-novice)
    (assign-all-required-duties duties)
    (assign-all-remaining-apprentices duties)
))
 
(defn solve [apprentices duties unavailable foreman]
 (-> apprentices
    (annotate-options duties)
    (annotate-foreman foreman)
    (remove-unavailable unavailable)
    (annotate-assignments duties)
    (print-results)))
