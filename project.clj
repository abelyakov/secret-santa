(defproject secret-santa "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.6.0"]
  				 [ring/ring-jetty-adapter "1.3.2"]
  				 [ring "1.3.2"]
  				 [ring/ring-json "0.3.1"]
  				 [compojure "1.3.1"]]
  :ring {:handler secret-santa.core/app})