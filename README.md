# _Connect 4_

![img.png](../resources/connect-four-bot/connect4irl.png)

What you see here is an implementation of a popular board game called *Connect 4*. 
The game is implemented in Clojure, including game logic & rules, and a simple AI to play against. 

## Game rules

Connect-4 is a two-player game, where each player takes turns dropping a coin into a 7-column, 6-row vertically suspended grid.
By "vertically suspended" I mean that the coins fall straight down, occupying the next available space within the column.

In the beggining, the grid is empty, and it might look something like this:

```
        1 2 3 4 5 6 7
      +---------------+
    6 | . . . . . . . |
    5 | . . . . . . . |
    4 | . . . . . . . |
    3 | . . . . . . . |
    2 | . . . . . . . |
    1 | . . . . . . . |
      +---------------+
```

The game starts with player number 1, and the players take turns dropping their coins into the grid.
After player 1 plays the 3rd column, and player 2 plays the 4th column, the grid would look like this:

```
        1 2 3 4 5 6 7
      +---------------+
    6 | . . . . . . . |
    5 | . . . . . . . |
    4 | . . . . . . . |
    3 | . . . . . . . |
    2 | . . . . . . . |
    1 | . . 1 2 . . . |
      +---------------+
```

If, for example, player 1 decides to play the 4th column, the grid would look like this:

```
        1 2 3 4 5 6 7
      +---------------+
    6 | . . . . . . . |
    5 | . . . . . . . |
    4 | . . . . . . . |
    3 | . . . . . . . |
    2 | . . . 1 . . . |
    1 | . . 1 2 . . . |
      +---------------+
```

The point here being the fact that a player cannot remove a coin from the grid, and the coins are always placed at the top of the column (unless the column is full, in which case the move is illegal).


***The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own coins (like the name may suggest)***.

Example of a player 1 winning diagonally may look like this:
    
    ```
            1 2 3 4 5 6 7
          +---------------+
        6 | . . . . . . . |
        5 | . . . . . . . |
        4 | . 1 . . . . . |
        3 | . 1 1 . . . . |
        2 | 1 2 2 1 2 . . |
        1 | 2 2 1 2 1 . . |
          +---------------+
    ```

*Alternatively, a game can end in a draw, if the grid is full and no player has won.*
Example of a draw may look like this:

    ```
            1 2 3 4 5 6 7
          +---------------+
        6 | 1 2 1 2 1 1 2 |
        5 | 2 1 1 1 2 1 2 |
        4 | 1 2 2 2 1 2 1 |
        3 | 2 1 2 1 1 2 2 |
        2 | 1 1 1 2 2 2 1 |
        1 | 2 1 2 1 2 1 2 |
          +---------------+
    ```
In such case, the game over and the board is reset.

## How to play

The text below will provide a demonstration of how to play the game in terminal

### Getting ready

In order to start the game in terminal (or REPL), all it takes is defining a board and player's turn.

```clojure
(def board (atom (init-board)))
```
and
```clojure
(def player (atom 1))
```
*You can change 1 to 2 if you wish for player 2 to start.*

### Two-player mode

Playing moves is done with a single function, where only the column number is required.
    
```clojure
(play! board column player)
```
Note that board and player are atoms, and the column is a number from 1 to 7.
So, an example of a move would be:

```clojure
(play! board 4 player)
```

The _play!_ function will automatically update the board and switch the player's turn, so there is no need to update anything other than column for the next player's turn.
It will also check if the move is legal, if the game is over, and print the board afterwards.

### Co-op mode

Playing against the AI is done in a similar manner, but with slight modifications:

```clojure
(autoplay! board column depth player)
```

The _autoplay!_ function behaves similarly to _play!_, but it requires an additional argument - the depth of the AI's search tree. It basically dictates how many moves ahead it will consider before finsing the best solution in the min-max algorithm.

If this confuses you, just think of it as a difficulty setting. The higher the number, the harder the AI will be to beat.

Again, note that board and player are atoms, the column is a number from 1 to 7, and **recommended depth is 3 to 6**. (using higher depth will take up significantly more time)

### Utility functions

There are also some utility functions that can help you out:
```clojure
; PRINTING THE BOARD
(print-board @board) ;this will print the current state of the board
(pretty-print-board @board) ;this will print the current the board using "+" and "-"
(print-board-using @board char1 char2) ;this will print the board, but with custom characters

; RESETING THE BOARD
(reset-board! board) ;this will reset the board to its initial state
```

### Winning

Upon winning, the game will print a cute message and reset the board.
![img_1.png](../resources/connect-four-bot/winning_message.png)

## License

~~Copyright © 2024 Novica Tri...~~ Just kidding please abuse it as you wish.


