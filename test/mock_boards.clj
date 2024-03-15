(ns mock-boards
  (:require [board.board :refer :all]))

(defn- rev-vec [v]
  (into [] (reverse v)))
(defn get-board-1 []
  (rev-vec
    [[0 0 0 0 0 0 0]
     [0 0 0 1 0 0 0]
     [0 1 2 1 0 0 0]
     [0 2 1 2 0 0 0]
     [0 1 2 1 1 0 0]
     [2 1 1 1 2 0 0]]))

(defn get-board-2 []
  (rev-vec
    [[0 0 0 0 0 0 0]
     [0 0 0 2 1 0 0]
     [0 1 2 1 2 1 0]
     [1 2 2 2 1 1 2]
     [1 1 2 1 2 2 1]
     [2 1 1 1 2 1 1]]))

(defn get-board-3 []
  (rev-vec
    [[0 0 0 0 0 0 0]
    [0 0 0 0 0 0 0]
    [0 0 0 0 1 0 0]
    [0 0 1 2 1 0 0]
    [0 1 2 1 2 0 0]
    [1 2 1 2 1 0 0]]))

(defn get-board-4  []
  (rev-vec
    [[0 0 0 0 0 0 0]
     [0 0 0 1 0 0 0]
     [0 0 2 1 0 0 0]
     [0 2 1 2 0 0 0]
     [0 1 2 2 1 0 0]
     [1 2 1 2 1 2 0]]))

(defn get-full-board []
  (rev-vec
    [[1 2 1 2 1 2 1]
     [2 1 2 1 2 1 2]
     [1 2 1 2 1 2 1]
     [2 1 2 1 2 1 2]
     [1 2 1 2 1 2 1]
     [2 1 2 1 2 1 2]]))
