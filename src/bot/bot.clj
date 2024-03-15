(ns bot.bot
  (:require [board.board :refer :all :as board]))

(defn enemy-of [player]
  "Vraca protivnika prosledjenog igraca"
  (- 3 player))
(defn occupied? [board row col]
  "Proverava da li je bilo koji igrac postavio novcic na zadatu poziciju"
(not= (board/value-at board row col) 0))

(defn unoccupied? [board row col]
  "Proverava da li je zadata pozicija prazna"
  (= (board/value-at board row col) 0))
(defn occupied-by? [board row col player]
  "Proverava da li je zadati igrac postavio novcic na zadatu poziciju"
(= (board/value-at board row col) player))
(defn opponent-occupied? [board row col player]
  "Proverava da li je protivnik zadatog igraca postavio novcic na zadatu poziciju"
  (occupied-by? board row col (enemy-of player)))

; proverava to polje i tri polja udesno
(defn check-possible-win-horizontal [board row col player]
  "Proverava da li je protivnik zauzeo zadato mesto ili neko od tri mesta udesno"
      (if (> col 3)
        false
        (not (or
        (opponent-occupied? board row col player)
        (opponent-occupied? board row (+ col 1) player)
        (opponent-occupied? board row (+ col 2) player)
        (opponent-occupied? board row (+ col 3) player)))))

(defn check-possible-win-vertical [board row col player]
      "Proverava da li je protivnik zauzeo zadato mesto ili neko od tri mesta iznad"
      (if (> row 2)
        false
        (not (or
        (opponent-occupied? board row col player)
        (opponent-occupied? board (+ row 1) col player)
        (opponent-occupied? board (+ row 2) col player)
        (opponent-occupied? board (+ row 3) col player)))))

(defn check-possible-win-main-diagonal [board row col player]
      "Proverava da li je protivnik zauzeo zadato mesto ili neko od tri mesta u dijagonali gore desno"
      (if (or (> row 2) (> col 3))
        false
        (not (or
        (opponent-occupied? board row col player)
        (opponent-occupied? board (+ row 1) (+ col 1) player)
        (opponent-occupied? board (+ row 2) (+ col 2) player)
        (opponent-occupied? board (+ row 3) (+ col 3) player)))))

(defn check-possible-win-secondary-diagonal [board row col player]
      "Proverava da li je protivnik zauzeo zadato mesto ili neko od tri mesta u dijagonali gore levo"
      (if (or (< row 3) (> col 3))
        false
        (not (or
        (opponent-occupied? board row col player)
        (opponent-occupied? board (- row 1) (+ col 1) player)
        (opponent-occupied? board (- row 2) (+ col 2) player)
        (opponent-occupied? board (- row 3) (+ col 3) player)))))


(defn count-possible-wins-horizontal [board player]
  "Broji na koliko nacina igrac moze da pobedi horizontalno"
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
  "Broji na koliko nacina igrac moze da pobedi vertikalno"
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
  "Broji na koliko nacina igrac moze da pobedi po glavnoj dijagonali"
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
  "Broji na koliko nacina igrac moze da pobedi po sporednoj dijagonali"
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
  "Broji na koliko nacina igrac moze da pobedi"
  (+ (count-possible-wins-horizontal board player)
     (count-possible-wins-vertical board player)
     (count-possible-wins-main-diagonal board player)
     (count-possible-wins-secondary-diagonal board player)))

;(defn count-possible-wins [board player]
;  (let [rez (+ (count-possible-wins-horizontal board player)
;               (count-possible-wins-vertical board player)
;               (count-possible-wins-main-diagonal board player)
;               (count-possible-wins-secondary-diagonal board player))]
;    (println "Possible wins player " player " : " rez)
;    rez))

(defn check-twos-in-a-row-horizontal [board row col player]
  "Proverava da li igrac ima dva novcica u nizu horizontalno"
  (if (> col 3)
    false
    (or
      (and
        (occupied-by? board row col player)
        (occupied-by? board row (+ col 1) player)
        (unoccupied? board row (+ col 2))
        (unoccupied? board row (+ col 3)))
      (and
        (unoccupied? board row col)
        (occupied-by? board row (+ col 1) player)
        (occupied-by? board row (+ col 2) player)
        (unoccupied? board row (+ col 3)))
      (and
        (unoccupied? board row col)
        (unoccupied? board row (inc col))
        (occupied-by? board row (+ col 2) player)
        (occupied-by? board row (+ col 3) player)))))

(defn check-twos-in-a-row-vertical [board row col player]
  "Proverava da li igrac ima dva novcica u nizu vertikalno"
  (if (> row 2)
    false
    (or
      (and
        (occupied-by? board row col player)
        (occupied-by? board (+ row 1) col player)
        (unoccupied? board (+ row 2) col)
        (unoccupied? board (+ row 3) col))
      (and
        (unoccupied? board row col)
        (occupied-by? board (+ row 1) col player)
        (occupied-by? board (+ row 2) col player)
        (unoccupied? board (+ row 3) col))
      (and
        (unoccupied? board row col)
        (unoccupied? board (+ row 1) col)
        (occupied-by? board (+ row 2) col player)
        (occupied-by? board (+ row 3) col player)))))

(defn check-twos-in-a-row-main-diagonal [board row col player]
  "Proverava da li igrac ima dva novcica u nizu po glavnoj dijagonali"
  (if (or (> row 2) (> col 3))
    false
    (or
      (and
        (occupied-by? board row col player)
        (occupied-by? board (+ row 1) (+ col 1) player)
        (unoccupied? board (+ row 2) (+ col 2))
        (unoccupied? board (+ row 3) (+ col 3)))
      (and
        (unoccupied? board row col)
        (occupied-by? board (+ row 1) (+ col 1) player)
        (occupied-by? board (+ row 2) (+ col 2) player)
        (unoccupied? board (+ row 3) (+ col 3)))
      (and
        (unoccupied? board row col)
        (unoccupied? board (+ row 1) (+ col 1))
        (occupied-by? board (+ row 2) (+ col 2) player)
        (occupied-by? board (+ row 3) (+ col 3) player)))))

(defn check-twos-in-a-row-secondary-diagonal [board row col player]
  "Proverava da li igrac ima dva novcica u nizu po sporednoj dijagonali"
  (if (or (< row 3) (> col 3))
    false
    (or
      (and
        (occupied-by? board row col player)
        (occupied-by? board (- row 1) (+ col 1) player)
        (unoccupied? board (- row 2) (+ col 2))
        (unoccupied? board (- row 3) (+ col 3)))
      (and
        (unoccupied? board row col)
        (occupied-by? board (- row 1) (+ col 1) player)
        (occupied-by? board (- row 2) (+ col 2) player)
        (unoccupied? board (- row 3) (+ col 3)))
      (and
        (unoccupied? board row col)
        (unoccupied? board (- row 1) (+ col 1))
        (occupied-by? board (- row 2) (+ col 2) player)
        (occupied-by? board (- row 3) (+ col 3) player)))))
(defn count-uninterrupted-twos-in-a-row-horizontal [board player]
  "Broji na koliko mesta igrac ima dva novcica u nizu horizontalno"
  (loop [i 0
         j 0
         count 0]
    (if (> j 3)
      (recur (inc i) 0 count)
      (if (>= i 6)
        count
        (if (check-twos-in-a-row-horizontal board i j player)
          (recur i (inc j) (inc count))
          (recur i (inc j) count))))))

(defn count-uninterrupted-twos-in-a-row-vertical [board player]
  "Broji na koliko mesta igrac ima dva novcica u nizu vertikalno"
  (loop [i 0
         j 0
         count 0]
    (if (> i 3)
      (recur 0 (inc j) count)
      (if (>= j 7)
        count
        (if (check-twos-in-a-row-vertical board i j player)
          (recur (inc i) j (inc count))
          (recur (inc i) j count))))))

(defn count-uninterrupted-twos-in-a-row-main-diagonal [board player]
  "Broji na koliko mesta igrac ima dva novcica u nizu po glavnoj dijagonali"
  (loop [i 0
         j 0
         count 0]
    (if (> j 3)
      (recur (inc i) 0 count)
      (if (>= i 3)
        count
        (if (check-twos-in-a-row-main-diagonal board i j player)
          (recur i (inc j) (inc count))
          (recur i (inc j) count))))))

(defn count-uninterrupted-twos-in-a-row-secondary-diagonal [board player]
  "Broji na koliko mesta igrac ima dva novcica u nizu po sporednoj dijagonali"
  (loop [i 3
         j 0
         count 0]
    (if (> j 3)
      (recur (inc i) 0 count)
      (if (>= i 6)
        count
        (if (check-twos-in-a-row-secondary-diagonal board i j player)
          (recur i (inc j) (inc count))
          (recur i (inc j) count))))))

(defn count-uninterrupted-twos-in-a-row [board player]
  "Broji na koliko mesta igrac ima dva novcica u nizu, i da oni imaju potencijal da spoje 4 u nizu"
  (+ (count-uninterrupted-twos-in-a-row-horizontal board player)
     (count-uninterrupted-twos-in-a-row-vertical board player)
     (count-uninterrupted-twos-in-a-row-main-diagonal board player)
     (count-uninterrupted-twos-in-a-row-secondary-diagonal board player)))

;(defn count-uninterrupted-twos-in-a-row [board player]
;  (let [rez (+ (count-uninterrupted-twos-in-a-row-horizontal board player)
;     (count-uninterrupted-twos-in-a-row-vertical board player)
;     (count-uninterrupted-twos-in-a-row-main-diagonal board player)
;     (count-uninterrupted-twos-in-a-row-secondary-diagonal board player))]
;    (println "Uninterrupted twos player " player " : " rez)
;    rez))

(defn check-threes-in-a-row-horizontal [board row col player]
  "Proverava da li igrac ima tri novcica horizontalno, koji imaju sansu da spoje 4 u nizu."
  (if (> col 3)
    false
    (if (or
          (opponent-occupied? board row col player)
          (opponent-occupied? board row (+ col 1) player)
          (opponent-occupied? board row (+ col 2) player)
          (opponent-occupied? board row (+ col 3) player))
      false
      (let [num-of-coins (atom 0)]
        (if (occupied-by? board row col player)
          (swap! num-of-coins inc))
        (if (occupied-by? board row (+ col 1) player)
          (swap! num-of-coins inc))
        (if (occupied-by? board row (+ col 2) player)
          (swap! num-of-coins inc))
        (if (occupied-by? board row (+ col 3) player)
          (swap! num-of-coins inc))
        (= @num-of-coins 3)))))

(defn check-threes-in-a-row-vertical [board row col player]
  "Proverava da li igrac ima tri novcica vertikalno, koji imaju sansu da spoje 4 u nizu."
  (if (> row 2)
    false
    (if (or
          (opponent-occupied? board row col player)
          (opponent-occupied? board (+ row 1) col player)
          (opponent-occupied? board (+ row 2) col player)
          (opponent-occupied? board (+ row 3) col player))
      false
      (let [num-of-coins (atom 0)]
        (if (occupied-by? board row col player)
          (swap! num-of-coins inc))
        (if (occupied-by? board (+ row 1) col player)
          (swap! num-of-coins inc))
        (if (occupied-by? board (+ row 2) col player)
          (swap! num-of-coins inc))
        (if (occupied-by? board (+ row 3) col player)
          (swap! num-of-coins inc))
        (= @num-of-coins 3)))))

(defn check-threes-in-a-row-main-diagonal [board row col player]
  "Proverava da li igrac ima tri novcica po glavnoj dijagonali, koji imaju sansu da spoje 4 u nizu."
  (if (or (> row 2) (> col 3))
    false
    (if (or
          (opponent-occupied? board row col player)
          (opponent-occupied? board (+ row 1) (+ col 1) player)
          (opponent-occupied? board (+ row 2) (+ col 2) player)
          (opponent-occupied? board (+ row 3) (+ col 3) player))
      false
      (let [num-of-coins (atom 0)]
        (if (occupied-by? board row col player)
          (swap! num-of-coins inc))
        (if (occupied-by? board (+ row 1) (+ col 1) player)
          (swap! num-of-coins inc))
        (if (occupied-by? board (+ row 2) (+ col 2) player)
          (swap! num-of-coins inc))
        (if (occupied-by? board (+ row 3) (+ col 3) player)
          (swap! num-of-coins inc))
        (= @num-of-coins 3)))))

(defn check-threes-in-a-row-secondary-diagonal [board row col player]
  "Proverava da li igrac ima tri novcica po sporednoj dijagonali, koji imaju sansu da spoje 4 u nizu."
  (if (or (< row 3) (> col 3))
    false
    (if (or
          (opponent-occupied? board row col player)
          (opponent-occupied? board (- row 1) (+ col 1) player)
          (opponent-occupied? board (- row 2) (+ col 2) player)
          (opponent-occupied? board (- row 3) (+ col 3) player))
      false
      (let [num-of-coins (atom 0)]
        (if (occupied-by? board row col player)
          (swap! num-of-coins inc))
        (if (occupied-by? board (- row 1) (+ col 1) player)
          (swap! num-of-coins inc))
        (if (occupied-by? board (- row 2) (+ col 2) player)
          (swap! num-of-coins inc))
        (if (occupied-by? board (- row 3) (+ col 3) player)
          (swap! num-of-coins inc))
        (= @num-of-coins 3)))))

(defn count-threes-in-a-row-horizontal [board player]
  "Broji na koliko mesta igrac ima tri novcica u nizu horizontalno, koji imaju sansu da spoje 4 u nizu."
  (loop [i 0
         j 0
         count 0]
    (if (> j 3)
      (recur (inc i) 0 count)
      (if (>= i 6)
        count
        (if (check-threes-in-a-row-horizontal board i j player)
          (recur i (inc j) (inc count))
          (recur i (inc j) count))))))

(defn count-threes-in-a-row-vertical [board player]
  "Broji na koliko mesta igrac ima tri novcica u nizu vertikalno, koji imaju sansu da spoje 4 u nizu."
  (loop [i 0
         j 0
         count 0]
    (if (> i 3)
      (recur 0 (inc j) count)
      (if (>= j 7)
        count
        (if (check-threes-in-a-row-vertical board i j player)
          (recur (inc i) j (inc count))
          (recur (inc i) j count))))))

(defn count-threes-in-a-row-main-diagonal [board player]
  "Broji na koliko mesta igrac ima tri novcica po glavnoj dijagonali, koji imaju sansu da spoje 4 u nizu."
  (loop [i 0
         j 0
         count 0]
    (if (> j 3)
      (recur (inc i) 0 count)
      (if (>= i 3)
        count
        (if (check-threes-in-a-row-main-diagonal board i j player)
          (recur i (inc j) (inc count))
          (recur i (inc j) count))))))

(defn count-threes-in-a-row-secondary-diagonal [board player]
  "Broji na koliko mesta igrac ima tri novcica po sporednoj dijagonali, koji imaju sansu da spoje 4 u nizu."
  (loop [i 3
         j 0
         count 0]
    (if (> j 3)
      (recur (inc i) 0 count)
      (if (>= i 6)
        count
        (if (check-threes-in-a-row-secondary-diagonal board i j player)
          (recur i (inc j) (inc count))
          (recur i (inc j) count))))))

(defn count-threes-in-a-row [board player]
  "Broji na koliko mesta igrac ima tri novcica, koji imaju sansu da spoje 4 u nizu."
  (+ (count-threes-in-a-row-horizontal board player)
     (count-threes-in-a-row-vertical board player)
     (count-threes-in-a-row-main-diagonal board player)
     (count-threes-in-a-row-secondary-diagonal board player)))

(defn evaluate-position [board]
  "Vraca vrednost pozicije na tabli. Negativan broj predstavlja poziciju koja vise odgovara igracu 2, pozitivan igracu 1."
  (cond
    (board/check-win-global board 2) -10000
    (board/check-win-global board 1) 10000
    (board/board-full? board) 0
    :else (+ (* 1 (- (count-possible-wins board 1) (count-possible-wins board 2)))
             (* 2 (- (count-uninterrupted-twos-in-a-row board 1) (count-uninterrupted-twos-in-a-row board 2)))
             (* 5 (- (count-threes-in-a-row board 1) (count-threes-in-a-row board 2))))))


; OCEKUJE ATOM
(defn hypothetical-move [board column player]
  "Vraca tablu nakon sto je igrac player odigrao potez u koloni column."
  ;(println "Proveren hipoteticki potez " column " za igraca " player) ;LOG
  (board/insert-coin board column player))

(defn my-max [board]
  "Vraca najbolji potez za igraca 1 u datoj poziciji."
  ;(println "My-max") ;LOG
  (loop [i 0
         max -10000
         move -1]
    (if (> i 6)
      (if (= max -10000)
        (throw (Exception. "Nema mogucih poteza"))
        {:evaluation max :move move})

      (if (col-full? board i)
        (recur (inc i) max move)
        (let [vrednost (evaluate-position (hypothetical-move board i 1))]
          ;(println "Za potez: " i  " je dobijena vrednost: " vrednost ", a max je " max) ;LOG
          (if (> vrednost max)
            (recur (inc i) vrednost i)
            (recur (inc i) max move)))))))

(defn my-min [board]
  "Vraca najbolji potez za igraca 2 u datoj poziciji."
  ;(println "My-min") ;LOG
  (loop [i 0
         min 10000
         move -1]
    (if (> i 6)
      (if (= min 10000)
        (throw (Exception. "Nema mogucih poteza"))
        {:evaluation min :move move})

      (if (col-full? board i)
        (recur (inc i) min move)
        (let [vrednost (evaluate-position (hypothetical-move board i 2))]
        ;(println "Za potez: " i  " je dobijena vrednost: " vrednost ", a min je " min "za potez" move) ;LOG
          (if (< vrednost min)
          (recur (inc i) vrednost i)
          (recur (inc i) min move)))))))


(defn my-minimax [board depth player]
  "Vraca najbolji potez i evaluaciju tog poteza igraca player sa prosledjenom dubinom proveravanja"
  (if (board/check-win-global board 1)
    10000
    (if (board/check-win-global board 2)
      -10000
      (if (board/board-full? board)
        0
        (if (<= depth 1)
          (if (= player 1)
          (my-max board)
          (my-min board))
        (if (= player 1)


          ;max funkcija
          (loop [i 0
                 max -10000
                 move -1]
            ;kraj petlje
            ;(println "Iteracija: " i " max: " max) ;LOG
            (if (> i 6)
              (if (= max -10000)
                (throw (Exception. "Nema mogucih poteza"))
                {:evaluation max :move move})

              (let [result (try
                             (my-minimax
                               (hypothetical-move board i player)
                               (dec depth) (enemy-of player))
                             (catch Exception ignored
                               {:evaluation max :move move}))]
                (if (> (:evaluation result) max)
                  (recur (inc i) (:evaluation result) i)
                  (recur (inc i) max move)))))


          ;min funkcija
          (loop [i 0
                 min 10000
                 move -1]
            ;(println "Iteracija: " i " min: " min) ;LOG
            (if (> i 6)
              (if (= min 10000)
                (throw (Exception. "Nema mogucih poteza"))
                {:evaluation min :move move})
              (let [result (try
                             (my-minimax
                               (hypothetical-move board i player)
                               (dec depth)
                               (enemy-of player))
                             (catch Exception ignored
                               ;(println "Exception" (.getMessage ignored)) ;LOG
                               {:evaluation min :move move}))]
                ;(println "Nakon " i " poteza " player " ima evaluaciju " (:evaluation result) " i min je " min) ;LOG
                (if (< (:evaluation result) min)
                  (recur (inc i) (:evaluation result) i)
                  (recur (inc i) min move)))))))))))


(defn get-best-move [board depth player]
  "Vraca kolonu koju bi trebalo sledeci igrac da odigra"
  (get (my-minimax board depth player) :move))

(defn let-bot-play! [board depth player]
  "Poziva funkciju play! sa najboljim potezom za drugog igraca"
  (if (= @player 1)
    (throw (Exception. "Bot ne moze igrati za igraca 1")))
  (let [move (inc (get-best-move @board depth @player))]
    (println "Bot igra kolonu : " move)
    (play! board move player)))

(defn autoplay! [board move depth player]
  "Odigrava dati potez za igraca 1 i automatski odigrava najbolji moguci potez za igraca 2"
  (if (= @player 2)
    (throw (Exception. "Autoplay moze samo za igraca 1")))
  (play! board move player)
  (println "Bot razmislja...")
  (if (< depth 6) (Thread/sleep 2000))
  (let-bot-play! board depth player))

