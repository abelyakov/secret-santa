(ns secret-santa.core
  (:require [ring.adapter.jetty :refer :all]
            [ring.util.response :refer :all]
            [ring.middleware.content-type :refer :all]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :refer [api site]]
            [clojure.data.json :as json]
            [clojure.pprint :as pprint]
            [secret-santa.rand :as rand]
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
  (swap! emails conj email)
  (str "Спасибо за участие!" "</br>" @emails))
 

(defn start-draw []
  (let [email-list (into '() @emails)]
    (cond 
      (empty? email-list) {}
      (= 1 (count email-list)) {(first email-list) (first email-list)}
      :else (rand/gen-pairs email-list))))

(defn send-emails [emails]
  (map #(send-email (key %) (val %)) emails))


(defroutes compojure-handler
  (POST "/send-email" [email] (save-email email))
  (GET "/" [] (redirect "/index.html"))
  (GET "/list" [] (json/write-str @emails))
  (GET "/rand" [] (json/write-str (start-draw)))
  (GET "/mail" [] (send-emails (start-draw)))
  (route/resources "/")
  (route/not-found "<h1>Page not found!</h1>"))

(def app 
  (-> compojure-handler
      wrap-params))



