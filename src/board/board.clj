(ns board.board)

(def igrac-1 (atom "Igrac 1"))
(def igrac-2 (atom "Igrac 2"))
(def broj-poteza (atom 0))
(def igrac-1-prvi (atom true))


(defn- get-row [board row]
  (nth board row))

(defn- get-col [board col]
  (map #(nth % col) board))

(defn build-board [width height]
  (vec (repeat height (vec (repeat width 0)))))

(defn init-board []
  (build-board 7 6))


(defn print-board [board]
  (doseq [row board]
    (println row)))


;proverava da li je kolona puna
(defn col-full? [board col]
  (not= (nth (get-col board col) 5) 0))

; proverava da li je cela tabla puna
(defn board-full? [board]
  (every? #(not (col-full? board %)) (range 7)))

; funkcija koja nalazi element jednak nuli najblizi dnu kolone
(defn find-zero [col]
  (loop [i 0]
    (if (= (nth col i) 0)
      i
      (recur (inc i)))))

; postavlja novi element na tablu
(defn place-coin [board col player]
  (let [row (find-zero (get-col board col))]
    (assoc-in board [row col] player)))

; ubacuje novi element u kolonu
(defn insert-coin [board col player]
  (if (col-full? board col)
    (throw (Exception. "Kolona je puna"))
    (place-coin board col player)))

; swap insert coin, poziva se od strane korisnika
(defn insert-coin! [board col player]
  (swap! board #(insert-coin % col player))
  (swap! broj-poteza inc)
  (print-board @board))


