(defproject clj-fdb "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :main clj-fdb.core
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [org.clojure/algo.generic "0.1.2"]
                 [com.foundationdb/fdb-java "2.0.8"]
                 [org.clojure/tools.cli "0.3.1"]]
  :plugins [[quickie "0.3.6"]])
