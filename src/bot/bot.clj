(ns bot.bot
  (:require [board.board :refer :all :as board]))

; 0 znaci sve je jednako, - znaci player 2 ( bot ) dobija
(defn evaluate-position
  [board]
  (if (board/check-win-global board 2)
    -1000
    (if (board/check-win-global board 1)
      1000
      0)))
(defn enemy-of [player]
  (- 3 player))
(defn occupied? [board row col]
(not= (board/value-at board row col) 0))

(defn occupied-by? [board row col player]
(= (board/value-at board row col) player))
(defn opponent-occupied? [board row col player]
  (occupied-by? board row col (enemy-of player)))

; proverava to polje i tri polja udesno
(defn check-possible-win-horizontal [board row col player]
      (if (> col 3)
        false
        (not (or
        (opponent-occupied? board row col player)
        (opponent-occupied? board row (+ col 1) player)
        (opponent-occupied? board row (+ col 2) player)
        (opponent-occupied? board row (+ col 3) player)))))

(defn check-possible-win-vertical [board row col player]
      (if (> row 2)
        false
        (not (or
        (opponent-occupied? board row col player)
        (opponent-occupied? board (+ row 1) col player)
        (opponent-occupied? board (+ row 2) col player)
        (opponent-occupied? board (+ row 3) col player)))))

(defn check-possible-win-main-diagonal [board row col player]
      (if (or (> row 2) (> col 3))
        false
        (not (or
        (opponent-occupied? board row col player)
        (opponent-occupied? board (+ row 1) (+ col 1) player)
        (opponent-occupied? board (+ row 2) (+ col 2) player)
        (opponent-occupied? board (+ row 3) (+ col 3) player)))))

(defn check-possible-win-secondary-diagonal [board row col player]
      (if (or (< row 3) (> col 3))
        false
        (not (or
        (opponent-occupied? board row col player)
        (opponent-occupied? board (- row 1) (+ col 1) player)
        (opponent-occupied? board (- row 2) (+ col 2) player)
        (opponent-occupied? board (- row 3) (+ col 3) player)))))


(defn count-possible-wins-horizontal [board player]
  (loop [i 0
         j 0
         count 0]
    (if (> j 3)
      (recur (inc i) 0 count)
      (if (>= i 6)
      count
      (if (check-possible-win-horizontal board i j player)
        (recur i (inc j) (inc count))
        (recur i (inc j) count))))))

(defn count-possible-wins-vertical [board player]
  (loop [i 0
         j 0
         count 0]
    (if (> i 3)
      (recur 0 (inc j) count)
      (if (>= j 7)
      count
      (if (check-possible-win-vertical board i j player)
        (recur (inc i) j (inc count))
        (recur (inc i) j count))))))

(defn count-possible-wins-main-diagonal [board player]
  (loop [i 0
         j 0
         count 0]
    (if (> j 3)
      (recur (inc i) 0 count)
      (if (>= i 3)
        count
        (if (check-possible-win-main-diagonal board i j player)
          (recur i (inc j) (inc count))
          (recur i (inc j) count))))))

(defn count-possible-wins-secondary-diagonal [board player]
  (loop [i 3
         j 0
         count 0]
    (if (> j 3)
      (recur (inc i) 0 count)
      (if (>= i 6)
        count
        (if (check-possible-win-secondary-diagonal board i j player)
          (recur i (inc j) (inc count))
          (recur i (inc j) count))))))

(defn count-possible-wins [board player]
  (+ (count-possible-wins-horizontal board player)
     (count-possible-wins-vertical board player)
     (count-possible-wins-main-diagonal board player)
     (count-possible-wins-secondary-diagonal board player)))

(defn evaluate-position [board]
  (if (board/check-win-global board 2)
    -1000
    (if (board/check-win-global board 1)
      1000
      (if (board/board-full? board)
        0
        (- (count-possible-wins board 1) (count-possible-wins board 2))))))



(defn hypothetical-move [board column player]
  (board/insert-coin board column player))

(defn my-max [board]
  "Vraca najbolji potez za igraca 1."
  (loop [i 0
         max -10000
         move -1]
    (if (> i 6)
      {:evaluation max :move move}
      (let [vrednost (evaluate-position (hypothetical-move board i 1))]
        (if (> vrednost max)
          (recur (inc i) vrednost i)
          (recur (inc i) max move))))))

(defn my-min [board]
  "Vraca najbolji potez za igraca 2."
  (loop [i 0
         min 10000
         move -1]
    (if (> i 6)
      {:evaluation min :move move}
      (let [vrednost (evaluate-position (hypothetical-move board i 2))]
        (if (< vrednost min)
          (recur (inc i) vrednost i)
          (recur (inc i) min move))))))

(defn my-minimax [board depth player]
  "Vraca najbolji potez za igraca player"
  (if (or (board/check-win-global board 1) (board/check-win-global board 2) (board/board-full? board) (<= depth 1))
    (if (= player 1)
      (my-max board)
      (my-min board))
    (if (= player 1)
      ;max funkcija
      (loop [i 0
             max -10000
             move -1]
        (if (> i 6)
          {:evaluation max :move move}
          (let [vrednost (get (my-minimax (hypothetical-move board i player) (dec depth) (enemy-of player)) :evaluation)]
            (if (> vrednost max)
              (recur (inc i) vrednost i)
              (recur (inc i) max move)))))
      ;min funkcija
      (loop [i 0
             min 10000
             move -1]
        (if (> i 6)
          {:evaluation min :move move}
          (let [vrednost (get (my-minimax (hypothetical-move board i player) (dec depth) (enemy-of player)) :evaluation)]
            (if (< vrednost min)
              (recur (inc i) vrednost i)
              (recur (inc i) min move))))))))