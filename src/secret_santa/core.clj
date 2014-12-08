(ns secret-santa.core
	(:require [ring.adapter.jetty :refer :all]
			  [ring.util.response :refer :all]
			  [compojure.core :refer :all]
		      [compojure.route :as route]))

; (defroutes app GET "/")

(defn handler [request]
	(response (str request)))

; (defn- main[]
; 	(run-jetty handler {:port 3000 :join? false}))
 

(def app handler)