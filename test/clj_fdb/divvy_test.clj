(ns clj-fdb.divvy-test
  (:require [clojure.test :refer :all]
            [clj-fdb.divvy :refer :all]))

(def ac add-consumer)

(deftest add-consumer-tests
  (is (= {:a #{2 3} :b #{0 1}} 
         (-> {:a (range 4)} 
             (ac :b))))
  (is (= {:a #{2} :b #{0 1} :c #{3}} 
         (-> {:a (range 4)} 
             (ac :b)
             (ac :c))))
  (is (= {:a #{2} :b #{1} :c #{3} :d #{0}} 
         (-> {:a (range 4)} 
             (ac :b)
             (ac :c)
             (ac :d)))))
