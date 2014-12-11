(ns secret-santa.core
  (:gen-class)
  (:require [ring.adapter.jetty :refer :all]
            [ring.util.response :refer :all]
            [ring.middleware.content-type :refer :all]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.json :as middleware] 
            [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :refer [api site]]
            [clojure.data.json :as json]
            [clojure.pprint :as pprint]
            [postal.core :as postal]))

(defn send-email [to text]
  (postal/send-message {:host "smtp.gmail.com"
                        :user ""
                        :pass ""
                        :ssl true
                        }
                       {:from "lstghost@gmail.com "
                        :to to
                        :subject "С наступающим!"
                        :body (str "Новогоднее жюри решило, что ты даришь подарок человеку с ящиком li" text)}))

(def emails (atom #{}))

(defn save-email [email]
  (swap! emails conj email))

(defn remove-email [email]
  (swap! emails disj email))

(defn gen-pairs [l] 
  (let [sh (shuffle l)]  
  (if (some true? (map = l sh))
    (gen-pairs l)
    (zipmap l sh)))) 

(defn start-draw []
  (let [email-list (into '() @emails)]
    (cond 
      (empty? email-list) {}
      (= 1 (count email-list)) {(first email-list) (first email-list)}
      :else (gen-pairs email-list))))



(defn send-emails [emails]
  (map #(send-email (key %) (val %)) emails))


(defroutes compojure-handler
  (POST "/send-email" [email] (do (save-email email) (redirect "/thanks.html")))
  (GET "/" [] (redirect "/index.html"))
  (GET "/list" [] (json/write-str @emails))
  (GET "/rand" [] (json/write-str (start-draw)))
  (GET "/mail" [] (send-emails (start-draw)))
  (GET "/remove/:email" [email] (do (remove-email email) (redirect "/thanks.html")))
  (route/resources "/")
  (route/not-found "<h1>Page not found!</h1>"))

(def app 
  (-> compojure-handler
      wrap-params
      middleware/wrap-json-response))

(defn -main  [& args]
    (let  [port  (Integer/parseInt  (get  (System/getenv) "PORT" "8080"))]
          (run-jetty app  {:port port})))

