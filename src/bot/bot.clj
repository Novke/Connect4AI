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

(defn occupied? [board row col]
(not= (board/value-at board row col) 0))

(defn occupied-by? [board row col player]
(= (board/value-at board row col) player))
(defn opponent-occupied? [board row col player]
  (occupied-by? board row col (- 3 player)))

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
  )

