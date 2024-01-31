(ns board.board)

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
  (= (nth (get-col board col) 5) 0))

; proverava da li je cela tabla puna
(defn board-full? [board]
  (every? #(not (col-full? board %)) (range 7)))

