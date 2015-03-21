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
  [current new-name]

  (let [new-tokens (tokens-for-new current)]

    (defn without-removed
      [original]
      (clojure.set/difference (set original) (set new-tokens)))
    (merge
     {new-name new-tokens}
     (functor/fmap without-removed current))))

(-> {:a (set (range 8))}
    (add-consumer :b)
    (add-consumer :c)
    (add-consumer :d)
    (add-consumer :e)
    (add-consumer :f)
    (add-consumer :g)
    (add-consumer :h))
