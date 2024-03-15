(ns board_test
  (:require [midje.sweet :refer :all]
            [board.board :refer :all]
            [mock-boards :refer :all]))

(def empty-board
  (init-board))
(def board-1  (get-board-1))

(def board-2  (get-board-2))

(def board-3  (get-board-3))

(def board-4  (get-board-4))

(def full-board (get-full-board))
(facts "Proverava da li je kolona puna"
       (fact "Column is not full"
             (let [board (atom (init-board))]
               (col-full? @board 0) => false))
       (fact "Column is full"
              (col-full? full-board 1) => true)
       (fact "Kolona nije puna"
             (col-full? board-1 3) => false))


(facts "Proverava da li je cela tabla puna"
       (fact "Tabla nije puna"
             (board-full? empty-board) => false
             (board-full? board-1) => false
             (board-full? board-2) => false
             (board-full? board-3) => false
             (board-full? board-4) => false)
       (fact "Tabla je puna"
             (board-full? full-board) => true))


(facts "Proverava da li find-zero funkcija vraca dobar index"
       (fact "Provera prazne table"
             (find-zero (get-col empty-board 0)) => 0
             (find-zero (get-col empty-board 2)) => 0
             (find-zero (get-col empty-board 3)) => 0
             (find-zero (get-col empty-board 5)) => 0)
       (fact "Provera table 1"
             (find-zero (get-col board-1 0)) => 1
             (find-zero (get-col board-1 2)) => 4
             (find-zero (get-col board-1 3)) => 5
             (find-zero (get-col board-1 5)) => 0)
       (fact "Provera table 2"
             (find-zero (get-col board-2 1)) => 4
             (find-zero (get-col board-2 2)) => 4
             (find-zero (get-col board-2 4)) => 5
             (find-zero (get-col board-2 5)) => 4)
       (fact "Provera table 3"
             (find-zero (get-col board-3 0)) => 1
             (find-zero (get-col board-3 1)) => 2
             (find-zero (get-col board-3 2)) => 3
             (find-zero (get-col board-3 3)) => 3)
       (fact "Provera table 4"
             (find-zero (get-col board-4 1)) => 3
             (find-zero (get-col board-4 4)) => 2
             (find-zero (get-col board-4 5)) => 1
             (find-zero (get-col board-4 6)) => 0)
       (fact "Provera pune table"
             (find-zero (get-col full-board 1)) => 6
             (find-zero (get-col full-board 3)) => 6
             (find-zero (get-col full-board 5)) => 6
             (find-zero (get-col full-board 6)) => 6))

(facts "Proverava da li place-coin radi"
       (fact "Provera prazne table"
             (get-in empty-board [0 0]) => 0
             (get-in (place-coin empty-board 0 1) [0 0]) => 1))

(facts "Proverava insert-coin"
       (fact "Baca izuzetak ako je index kolone van opsega"
             (let [board (init-board)]
               (insert-coin board -1 1) => (throws Exception)
               (insert-coin board 7 1) => (throws Exception)))

       (fact "Baca izuzetak ako je kolona puna"
             (insert-coin full-board 0 1) => (throws Exception))

       (fact "Baca izuzetak ako je igrac nevalidan"
             (insert-coin empty-board 0 0) => (throws Exception)
             (insert-coin empty-board 0 3) => (throws Exception))
       ;GET IN OCEKUJE VREDNOST
       (fact "Provera da li je ubacen novcic u tablu"
             (get-in empty-board [0 0]) => 0
             (get-in (insert-coin empty-board 0 1) [0 0]) => 1
             (get-in (insert-coin empty-board 3 2) [0 3]) => 2

             (value-at board-2 4 2) => 0
             (value-at (insert-coin board-2 2 1) 4 2) => 1
             (value-at board-2 4 5) => 0
             (value-at (insert-coin board-2 5 2) 4 5) => 2

             (value-at board-3 0 5) => 0
             (value-at (insert-coin board-3 5 1) 0 5) => 1
             (value-at board-3 3 2) => 0
             (value-at (insert-coin board-2 2 2) 3 2) => 2

              (value-at board-4 1 0) => 0
              (value-at (insert-coin board-4 0 1) 1 0) => 1
              (value-at board-4 2 4) => 0
              (value-at (insert-coin board-4 4 2) 2 4) => 2))

(facts "Proverava insert-coin!"
       (fact "Baca izuzetak ako je kolona puna"
             (insert-coin! (atom full-board) 0 1) => (throws Exception))

       (fact "Proverav da li je unet novcic na mesto"
             (let [board (atom (init-board))]
               (insert-coin! board 0 1)
               (get-in @board [0 0]) => 1
               (insert-coin! board 3 2)
               (get-in @board [0 3]) => 2
               (insert-coin! board 6 1)
               (get-in @board [0 6]) => 1
               (insert-coin! board 6 2)
               (get-in @board [1 6]) => 2
               (insert-coin! board 6 1)
               (get-in @board [2 6]) => 1
               (insert-coin! board 6 2)
               (get-in @board [3 6]) => 2
               (insert-coin! board 6 1)
               (get-in @board [4 6]) => 1
               (insert-coin! board 6 2)
               (get-in @board [5 6]) => 2))

       (fact "Povecava broj novcica u koloni"
             (let [n (find-zero (get-col board-1 2))
                   board (atom board-1)]
                (insert-coin! board 2 1)
                (find-zero (get-col @board 2)) => (inc n)
                (insert-coin! board 2 2)
                (find-zero (get-col @board 2)) => (inc (inc n)))

             (let [n (find-zero (get-col board-2 5))
                   board (atom board-2)]
                (insert-coin! board 5 1)
                (find-zero (get-col @board 5)) => (inc n)
                (insert-coin! board 5 2)
                (find-zero (get-col @board 5)) => (inc (inc n)))

             (let [n (find-zero (get-col board-3 3))
                   board (atom board-3)]
                (insert-coin! board 3 1)
                (find-zero (get-col @board 3)) => (inc n)
                (insert-coin! board 3 2)
                (find-zero (get-col @board 3)) => (inc (inc n)))

             (let [n (find-zero (get-col board-4 6))
                   board (atom board-4)]
                (insert-coin! board 6 1)
                (find-zero (get-col @board 6)) => (inc n)
                (insert-coin! board 6 2)
                (find-zero (get-col @board 6)) => (inc (inc n)))))


(facts "Proverava da li je igrac pobedio"
       (fact "Igrac nije pobedio"
             (check-win-global board-1 1) => nil
             (check-win-global board-3 1) => nil
             (check-win-global board-1 2) => nil
             (check-win-global board-2 2) => nil
             (check-win-global board-3 2) => nil
             (check-win-global board-4 2) => nil)

       (fact "Igrac je pobedio"

             (check-win-global board-4 1) => true
             (check-win-global board-2 1) => true ))

 ;proverava play funkciju
(facts "Proverava da li play funkcija radi kako treba"

       (fact "Proverava da li baca izuzetak ako je kolona puna"
             (let [board (atom full-board)]
               (play! board 1 (atom 1)) => (throws Exception)))

       (fact "Proverava da li baca izuzetak ako je igrac nevalidan"
              (let [board (atom empty-board)]
                (play! board 1 (atom 0)) => (throws Exception)
                (play! board 1 (atom 3)) => (throws Exception)))

       (fact "Proverava da li menja igraca"
              (let [player (atom 1)
                    board (atom empty-board)]
                (play! board 1 player)
                @player => 2
                (play! board 1 player)
                @player => 1))

       (fact "Proverava da li se povecao broj novcica u koloni"

             (let [board (atom empty-board)]
               (play! board 1 (atom 1))
               (find-zero (get-col @board 0)) => 1
               (play! board 1 (atom 2))
               (find-zero (get-col @board 0)) => 2)

             (let [board (atom board-1)
                   n (find-zero (get-col @board 0))]
               (play! board 1 (atom 1))
               (find-zero (get-col @board 0)) => (inc n))

             (let [board (atom board-3)
                   n (find-zero (get-col @board 1))]
                (play! board 2 (atom 2))
                (find-zero (get-col @board 1)) => (inc n)))

       (fact "Proverava da li je postavljen novcic na mesto"
             (let [board (atom empty-board)]
               (play! board 1 (atom 1))
               (get-in @board [0 0]) => 1
               (play! board 1 (atom 2))
               (get-in @board [1 0]) => 2
               (play! board 1 (atom 1))
               (get-in @board [2 0]) => 1
               (play! board 1 (atom 2))
               (get-in @board [3 0]) => 2

               (play! board 4 (atom 1))
                (get-in @board [0 3]) => 1
                (play! board 4 (atom 2))
                (get-in @board [1 3]) => 2
                (play! board 4 (atom 1))
                (get-in @board [2 3]) => 1)

             (let [board (atom board-1)]
               (play! board 1 (atom 1))
               (get-in @board [1 0]) => 1
               (play! board 2 (atom 2))
               (get-in @board [4 1]) => 2
               (play! board 4 (atom 1))
               (get-in @board [5 3]) => 1

               ; ocekuje izuzetak
               (play! board 4 (atom 2)) => (throws Exception))))



