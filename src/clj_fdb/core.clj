(ns clj-fdb.core
  (:require [clojure.core.async :as async]
            [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def cli-options
  ;; An option with a required argument
  [["-p" "--produce"] 
    ; "Port number"
    ; :default 80
    ; :parse-fn #(Integer/parseInt %)
    ; :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ; ;; A non-idempotent option
   ; ["-v" nil "Verbosity level"
    ; :id :verbosity
    ; :default 0
    ; :assoc-fn (fn [m k _] (update-in m [k] inc))]
   ;; A boolean option defaulting to nil
   ["-h" "--help"]])

(defn process [line]
  (Thread/sleep 10)
  line)

(def stdin-reader
  (java.io.BufferedReader. *in*))

(def in-chan (async/chan))
(def out-chan (async/chan))

(defn start-async-consumers
  "Start num-consumers threads that will consume work
  from the in-chan and put the results into the out-chan."
  [num-consumers]
  (dotimes [_ num-consumers]
    (async/thread
      (while true
        (let [line (async/<!! in-chan)
              data (process line)]
          (async/>!! out-chan data))))))

(defn start-async-aggregator
  "Take items from the out-chan and print it."
  []
  (async/thread
    (while true
      (let [data (async/<!! out-chan)]
        (println data)))))

(defn produce []
  (do
    (start-async-consumers 8)
    (start-async-aggregator)
    (doseq [line (line-seq stdin-reader)]
      (async/>!! in-chan line))))


(defn -main [& args]
  (println (parse-opts args cli-options))
  (produce))
