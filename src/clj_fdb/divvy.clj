(ns clj-fdb.divvy
  (:require [clojure.algo.generic.functor :as functor]))

(defn tokens-for-new
  [current]
  (let [token-count (apply + (map count (vals current)))
        num-tokens-for-new (int (/ token-count (inc (count current))))]

    (set (take num-tokens-for-new
               (apply interleave
                      (sort-by (comp - count) (vals current)))))))

(defn add-consumer
  "For a given set of assignments, returns new assignments for the addition of a new consumer.
  Prefers to evenly take tokens from existing consumers and leave as many tokens in their
  original spot as is reasonable."
  [current new-name]

  (let [new-tokens (tokens-for-new current)]

    (defn without-removed
      [original]
      (clojure.set/difference (set original) (set new-tokens)))
    (merge
     {new-name new-tokens}
     (functor/fmap without-removed current))))

(defn redist
  "Returns a map of the elements in current that belong to remove-name, redistributed
  evenly to the remaining names"
  [current remove-name]

  (functor/fmap 
   (comp set  (partial functor/fmap second)) 
   (group-by first
             (map list 
                  (cycle (map first (sort-by (juxt (comp count second) first) (dissoc current remove-name)))) 
                  (sort (current remove-name))))))

(defn remove-consumer
  "For a given set of assignments, returns new assignments after removing a consumer and
  redistributing its tokens evenly amongst the remaining consumers"
  [current remove-name]
  
  (merge-with clojure.set/union
              (redist current remove-name)
              (dissoc current remove-name)))
