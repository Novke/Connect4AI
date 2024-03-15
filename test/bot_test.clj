(ns bot_test
  (:require [midje.sweet :refer :all]
            [bot.bot :refer :all]
            [board.board :refer :all]
            [mock-boards :refer :all]))

(def empty-board
  (init-board))
(def board-1  (get-board-1))
(def board-2  (get-board-2))
(def board-3  (get-board-3))
(def board-4  (get-board-4))

(fact "check-twos-in-a-row-horizontal should return correct results"
      (check-twos-in-a-row-horizontal board-1 4 0 1) => false
      (check-twos-in-a-row-horizontal board-1 4 1 1) => false
      (check-twos-in-a-row-horizontal board-1 4 2 1) => false

      (check-twos-in-a-row-horizontal board-2 4 0 1) => false
      (check-twos-in-a-row-horizontal board-2 1 2 2) => false
      (check-twos-in-a-row-horizontal board-2 3 5 1) => false

      (check-twos-in-a-row-horizontal board-3 5 0 1) => false
      (check-twos-in-a-row-horizontal board-3 5 1 1) => false
      (check-twos-in-a-row-horizontal board-3 5 2 1) => false

      (check-twos-in-a-row-horizontal board-4 5 0 1) => false
      (check-twos-in-a-row-horizontal board-4 5 1 1) => false
      (check-twos-in-a-row-horizontal board-4 5 2 1) => false

      (check-twos-in-a-row-horizontal empty-board 0 0 1) => false
      (check-twos-in-a-row-horizontal empty-board 1 1 1) => false
      (check-twos-in-a-row-horizontal empty-board 2 2 1) => false)

(fact "Proverava da li igrac ima dva novcica u nizu vertikalno"
      (check-twos-in-a-row-vertical board-1 4 0 1) => false
      (check-twos-in-a-row-vertical board-1 4 1 1) => false
      (check-twos-in-a-row-vertical board-1 4 2 1) => false


      (check-twos-in-a-row-vertical board-2 4 0 1) => false
      (check-twos-in-a-row-vertical board-2 2 2 2) => true
      (check-twos-in-a-row-vertical board-2 2 5 1) => true

      (check-twos-in-a-row-vertical board-3 5 0 1) => false
      (check-twos-in-a-row-vertical board-3 5 1 1) => false
      (check-twos-in-a-row-vertical board-3 5 2 1) => false

      (check-twos-in-a-row-vertical board-4 5 0 1) => false
      (check-twos-in-a-row-vertical board-4 5 1 1) => false
      (check-twos-in-a-row-vertical board-4 5 2 1) => false

      (check-twos-in-a-row-vertical empty-board 0 0 1) => false
      (check-twos-in-a-row-vertical empty-board 1 1 1) => false
      (check-twos-in-a-row-vertical empty-board 2 2 1) => false)

(fact "Proverava da li igrac ima dva novcica u nizu na glavnoj dijagonali bez prekida"
      (count-uninterrupted-twos-in-a-row-main-diagonal board-1 1) => 3
      (count-uninterrupted-twos-in-a-row-main-diagonal board-2 1) => 1
      (count-uninterrupted-twos-in-a-row-main-diagonal board-3 1) => 2
      (count-uninterrupted-twos-in-a-row-main-diagonal board-4 1) => 1
      (count-uninterrupted-twos-in-a-row-main-diagonal empty-board 1) => 0)

(fact "Proverava da li igrac ima dva novcica u nizu na sporednoj dijagonali bez prekida"
      (count-uninterrupted-twos-in-a-row-secondary-diagonal board-1 1) => 0
      (count-uninterrupted-twos-in-a-row-secondary-diagonal board-2 1) => 1
      (count-uninterrupted-twos-in-a-row-secondary-diagonal board-3 1) => 1
      (count-uninterrupted-twos-in-a-row-secondary-diagonal board-4 1) => 0
      (count-uninterrupted-twos-in-a-row-secondary-diagonal empty-board 1) => 0)

(fact "Proverava da li igrac ima tri novcica u nizu horizontalno"
      (check-threes-in-a-row-horizontal board-1 0 0 1) => false
      (check-threes-in-a-row-horizontal board-2 0 1 1) => false
      (check-threes-in-a-row-horizontal board-3 2 1 2) => false
      (check-threes-in-a-row-horizontal board-4 0 2 1) => false
      (check-threes-in-a-row-horizontal empty-board 0 4 1) => false)

(fact "Proverava da li igrac ima tri novcica u nizu horizontalno bez prekida"
      (count-threes-in-a-row-horizontal board-1 1) => 0
      (count-threes-in-a-row-horizontal board-2 1) => 0
      (count-threes-in-a-row-horizontal board-3 1) => 0
      (count-threes-in-a-row-horizontal board-4 1) => 0
      (count-threes-in-a-row-horizontal empty-board 1) => 0)

(fact "Proverava da li igrac ima tri novcica u nizu bez prekida"
      (count-threes-in-a-row board-1 1) => 2
      (count-threes-in-a-row board-2 1) => 1
      (count-threes-in-a-row board-3 1) => 3
      (count-threes-in-a-row board-4 1) => 1
      (count-threes-in-a-row empty-board 1) => 0)