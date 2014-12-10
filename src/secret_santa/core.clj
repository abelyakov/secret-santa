(ns secret-santa.core
	(:require [ring.adapter.jetty :refer :all]
                  [ring.util.response :refer :all]
                  [ring.middleware.content-type :refer :all]
                  [ring.middleware.params :refer [wrap-params]]
                  [compojure.core :refer :all]
                  [compojure.route :as route]
                  [compojure.handler :refer [api site]]))

(def emails (atom #{}))

(defn process-email-post [email]
  (swap! emails conj email)
  "Спасибо за участие!")

(defroutes compojure-handler
  (POST "/send-email" [email] (process-email-post email))
  (GET "/" [] (redirect "/index.html"))
  (GET "/list" [] (str @emails))
  (route/resources "/")
  (route/not-found "<h1>Page not found!</h1>"))

(def app 
	( -> compojure-handler
             wrap-params))



