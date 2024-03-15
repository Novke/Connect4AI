(ns board.board)

(def igrac-1 (atom "Igrac 1"))
(def igrac-2 (atom "Igrac 2"))
(def igrac-1-prvi (atom true))


(defn get-row [board row]
  "Vraca red sa indeksom row iz table."
  (nth board row))

(defn get-col [board col]
  "Vraca kolonu sa indeksom col iz table."
  (map #(nth % col) board))

(defn print-board [board]
  "Ispisuje tablu."
  (doseq [row (reverse board)]
    (println row)))

(defn build-board [width height]
  "Pravi tablu dimenzija width x height."
  (vec (repeat height (vec (repeat width 0)))))

(defn init-board []
  "Inicijalizuje tablu standardnih dimenzija 7x6."
  (build-board 7 6))

(defn reset-board! [board]
  "Resetuje tablu na pocetno stanje."
  (reset! board (init-board)))

;(defn pretty-print-board [board]
;  (doseq [row (reverse board)]
;    (println (map #(if (= % 2) "O" (if (= % 1) "X" " ")) row))))

(defn print-board-using [board char1 char2]
  "Ispisuje tablu sa zadatim znakovima."
  (doseq [row (reverse board)]
    (println (map #(if (= % 2) char2 (if (= % 1) char1 " ")) row))))

(defn pretty-print-board [board]
  "Ispisuje tablu sa lepim znakovima."
  (print-board-using board "+" "-"))

(defn col-full? [board col]
  "Proverava da li je kolona puna."
  (not= (nth (get-col board col) 5) 0))

(defn board-full? [board]
  "Proverava da li je cela tabla puna."
  (every? #(col-full? board %) (range 7)))

(defn find-zero [col]
  "Vraca indeks prvog praznog polja u koloni."
  (loop [i 0]
    (if (< i (count col))
      (if (= (nth col i) 0)
        i
        (recur (inc i)))
      6)))

; OCEKUJE VREDNOST< NE ATOM
(defn place-coin [board col player]
  "Ubacuje novcic igraca player u kolonu col."
  (let [row (find-zero (get-col board col))]
    (if (nil? row)
      (throw (Exception. "Kolona je puna"))
      (assoc-in board [row col] player))))

; OCEKUJE ATOM
; VRACA VREDNOST
(defn insert-coin [board col player]
  "Validira potez i vraca novu tablu sa novcicem na odgovarajucem mestu."
  (if (or
        (>= col 7)
        (< col 0))
    (throw (Exception. "Odabrati kolonu izmedju 1 i 7"))
   (if (col-full? board col)
    (throw (Exception. "Kolona je puna"))
    (if (and
          (not= player 1)
          (not= player 2))
      (throw (Exception. "Igrac moze biti samo 1 ili 2"))
      (place-coin board col player)))))

; swap insert coin, pozivati ovu funkciju
;(defn insert-coin! [board col player]
;  "Menja tabelu i unosi novcic u kolonu col za igraca player."
;  (swap! board #(insert-coin % col player)))

;ZAMENA
(defn insert-coin! [board col player]
  "Menja tabelu i unosi novcic u kolonu col za igraca player."
  (reset! board (insert-coin @board col player)))

(defn value-at [board row col]
  "Vraca vrednost polja zadatih kooridnata."
  (try (nth (nth board row) col)
       (catch Exception e -1)))

;(defn value-at [board row col]
;  "Vraca vrednost polja zadatih kooridnata."
;  (try
;    (get-in board [row col])
;    (catch Exception e -1)))

(defn count-left [board row col igrac]
  "Broji koliko novcica igrac ima levo od zadatih koordinata."
  (if (<= igrac 0)
    0
    (loop [i 0]
      (if (not= (value-at board row (- col i 1)) igrac)
        i
        (recur (inc i))))))

(defn count-right [board row col igrac]
  "Broji koliko novcica igrac ima desno od zadatih koordinata."
  (if (<= igrac 0)
    0
    (loop [i 0]
      (if (not= (value-at board row (+ col i 1)) igrac)
        i
        (recur (inc i)))
      )))


(defn count-down [board row col igrac]
  "Broji koliko novcica igrac ima ispod zadatih koordinata."
  (if (<= igrac 0)
    0
    (loop [i 0]
      (if (not= (value-at board (- row i 1) col) igrac)
        i
        (recur (inc i)))
      )))


(defn count-up-right [board row col igrac]
  "Broji koliko novcica igrac ima dijagonalno gore desno od zadatih koordinata."
  (if (<= igrac 0)
    0
    (loop [i 0]
      (if (not= (value-at board (+ row i 1) (+ col i 1)) igrac)
        i
        (recur (inc i)))
      )))

(defn count-up-left [board row col igrac]
  "Broji koliko novcica igrac ima dijagonalno gore levo od zadatih koordinata."
  (if (<= igrac 0)
    0
    (loop [i 0]
      (if (not= (value-at board (+ row i 1) (- col i 1)) igrac)
        i
        (recur (inc i)))
      ))
  )

(defn count-down-right [board row col igrac]
  "Broji koliko novcica igrac ima dijagonalno dole desno od zadatih koordinata."
  (if (<= igrac 0)
    0
    (loop [i 0]
      (if (not= (value-at board (- row i 1) (+ col i 1)) igrac)
        i
        (recur (inc i)))
      )))

(defn count-down-left [board row col igrac]
  "Broji koliko novcica igrac ima dijagonalno dole levo od zadatih koordinata."
  (if (<= igrac 0)
    0
    (loop [i 0]
      (if (not= (value-at board (- row i 1) (- col i 1)) igrac)
        i
        (recur (inc i))))))


(defn check-win [board row col player]
  "Proverava da li je igrac [player] pobedio nakon postavljenog novcica u redu [row] i koloni [col]."
  (if (not= player (value-at board row col))
    false
    (or
      (>= (count-down board row col player) 3)
      (>= (+ (count-left board row col player)
             (count-right board row col player))
          3)
      (>= (+ (count-up-left board row col player)
             (count-down-right board row col player))
          3)
      (>= (+ (count-up-right board row col player)
             (count-down-left board row col player))
          3)))
  )

;vraca true ako jeste i nil ako nije
(defn check-win-global [board player]
  "Proverava da li je igrac [player] pobedio."
  ;proverava da li je igrac player pobedio pozivajuci check-win za svako polje
  (some identity (for [row (range 6) col (range 7)]
                   (check-win board row col player))))

; player je atom
(defn- switch-player!
  [player]
  "Menja igraca."
  (reset! player (- 3 @player)))

;broj ne-nula elemenata u koloni
(defn count-col [board col]
  (count (filter #(not= % 0) (get-col board col))))

(defn- hooray [player]
  "Ispisuje poruku o pobedniku."
  (println "=========================")
  (println "   Pobedio je igrac" player)
  (println "========================="))

(defn- draw []
  "Ispisuje poruku o neresenom rezultatu."
  (println "-------------------------")
  (println "       Nereseno!")
  (println "-------------------------"))

; player je atom, indeks kolone krece od 1
(defn play!
  [board col player]
  "Igra potez. Menja igraca, proverava da li je partija zavrsena. Ispisuje stanje"
  (insert-coin! board (- col 1) @player)
  (if (check-win @board (- (count-col @board (- col 1)) 1) (- col 1) @player)
    (do
      (hooray @player)
      (reset-board! board)
      (switch-player! player))
    (if (board-full? @board)
      (do
        (draw)
        (reset-board! board))
      (switch-player! player)))
  (print-board @board)
  (println "Na potezu je igrac" @player))

