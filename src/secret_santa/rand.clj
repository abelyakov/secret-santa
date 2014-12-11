(ns secret-santa.rand)

; (defn- get-rand-pair [cur others]
;   (if (= 1 (count others))
;     (first others)
;     (rand-nth (remove #(= cur %) others))))

; (defn- get-rand-pair-and-pop [all box accum]
;   (if (or (empty? all) (empty? box))
;     accum
;     (let [current (first all)
;           pair (get-rand-pair current box)]
;       (get-rand-pair-and-pop (rest all) (do (remove #(= pair %) box)) (assoc accum current pair)))))

; (defn gen-pairs [emails]
;   (let [result (get-rand-pair-and-pop emails emails {})]
;     (if (some #(= (key %) (val %)) result) 
;       (gen-pairs emails)
;       result)))

(defn gen-pairs [l] 
  (let [sh (shuffle l)]  
  (if (some zero? (map - l sh))
    (gen-p l)
    (zipmap l sh))))
