(ns clj-fdb.divvy
  (:require [clojure.algo.generic.functor :as functor]
            [clojure.set :as set]))

(defn tokens-for-new
  [current]
  (let [tokens (vals current)
        token-count (apply + (map count tokens))
        num-tokens-for-new (int (/ token-count (inc (count current))))]

    (set (take num-tokens-for-new
               (apply interleave
                      (sort-by (comp - count) tokens))))))

(defn add-consumer
  "For a given set of assignments, returns new assignments for the addition of a new consumer.
  Prefers to evenly take tokens from existing consumers and leave as many tokens in their
  original spot as is reasonable."
  [current new-key]

  (let [new-tokens (tokens-for-new current)]

    (merge
     {new-key new-tokens}
     (functor/fmap #(set/difference (set %) (set new-tokens)) current))))

(defn redist
  "Returns a map of the elements in current that belong to remove-key, redistributed
  evenly to the remaining keys"
  [current remove-key]

  (let [remaining (dissoc current remove-key)
        to-redist (current remove-key)
        ; remaining keys, sorted by # of tokens ascending, breaking ties with the key name
        remaining-keys (map first (sort-by (juxt (comp count second) first) remaining))]

    (apply merge-with set/union
           (map #(hash-map %1 #{%2})
                ; cycle so if we have more keys they are distributed evenly
                (cycle remaining-keys)
                ; sort tokens so we get predictable results
                (sort to-redist)))))

(defn remove-consumer
  "For a given set of assignments, returns new assignments after removing a consumer and
  redistributing its tokens evenly amongst the remaining consumers"
  [current remove-key]

  (merge-with set/union
              (redist current remove-key)
              (dissoc current remove-key)))
