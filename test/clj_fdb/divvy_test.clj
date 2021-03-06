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

(deftest redist-tests
  (is (= {:a #{1}}
         (redist {:a [0] :b [1]} :b)))
  (is (= {:b #{3}}
         (redist {:a [0 1] :b [2] :c [3]} :c)))
  (is (= {:b #{3}}
         (redist {:a [0 1] :c [2] :d [3] :b [4]} :d))))

(def rc remove-consumer)

(deftest remove-consumer-tests
  (is (= {:a (set (range 4))} 
         (-> {:a #{2} :b #{1} :c #{3} :d #{0}} 
             (rc :d)
             (rc :c)
             (rc :b))))
  (is (= {:a #{0 2} :b #{1 3}} 
         (-> {:a #{2} :b #{1} :c #{3} :d #{0}} 
             (rc :d)
             (rc :c))))
  (is (= {:a #{0 2} :b #{1} :c #{3}} 
         (-> {:a #{2} :b #{1} :c #{3} :d #{0}} 
             (rc :d)))))
