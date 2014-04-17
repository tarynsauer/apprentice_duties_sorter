(defproject sorter "1.0-SNAPSHOT"
  :description "Apprentice Duties Sorter App"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :profiles {:dev {:dependencies [[speclj "3.0.1"]]}}
  :plugins [[speclj "3.0.1"]]
  :test-paths ["spec"]
  :main sorter.core)
