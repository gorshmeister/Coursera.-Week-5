package games.game2048

import board.Cell
import board.Direction
import board.Direction.*
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Your task is to implement the game 2048 https://en.wikipedia.org/wiki/2048_(video_game).
 * Implement the utility methods below.
 *
 * After implementing it you can try to play the game running 'PlayGame2048'.
 */
fun newGame2048(initializer: Game2048Initializer<Int> = RandomGame2048Initializer): Game =
    Game2048(initializer)

class Game2048(private val initializer: Game2048Initializer<Int>) : Game {
    private val board = createGameBoard<Int?>(4)


    override fun initialize() {
        repeat(2) {
            board.addNewValue(initializer)
        }
    }

    override fun canMove(): Boolean = board.any { it == null }

    override fun hasWon() = board.any { it == 2048 }

    override fun processMove(direction: Direction) {
        if (board.moveValues(direction)) {
            board.addNewValue(initializer)
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}

/*
 * Add a new value produced by 'initializer' to a specified cell in a board.
 */
fun GameBoard<Int?>.addNewValue(initializer: Game2048Initializer<Int>) {
    val pair: Pair<Cell, Int>? = initializer.nextValue(this)

    if (pair != null) {
        this[pair.first] = pair.second
    }
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" in a specified rowOrColumn only.
 * Use the helper function 'moveAndMergeEqual' (in Game2048Helper.kt).
 * The values should be moved to the beginning of the row (or column),
 * in the same manner as in the function 'moveAndMergeEqual'.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValuesInRowOrColumn(rowOrColumn: List<Cell>): Boolean {
    val listOfValues = mutableListOf<Int?>()
    rowOrColumn.forEach { cell ->
        listOfValues += this[cell]
    }

    val newValues = listOfValues.moveAndMergeEqual { it + it }

    if (newValues.isEmpty()) {
        return false
    } else if (listOfValues != newValues) {
        rowOrColumn.forEach { cell ->
            this[cell] = null
        }

        val map = mutableMapOf<Cell, Int?>()
        for ((i, cell) in rowOrColumn.withIndex()) {
            try {
                map += cell to newValues[i]
            } catch (e: Exception) {
                break
            }
        }

        map.forEach { (cell, value) ->
            this[cell] = value
        }

        return true
    } else
        return false


}

/*
 * Update the values stored in a board,
 * so that the values were "moved" to the specified direction
 * following the rules of the 2048 game .
 * Use the 'moveValuesInRowOrColumn' function above.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValues(direction: Direction): Boolean {
    var flag: Boolean
    var count = 0
    when (direction) {
        LEFT ->
            for (i in 1..4) {
                val row = this.getRow(i, 1..4)
                flag = this.moveValuesInRowOrColumn(row)
                if (flag)
                    count++
            }
        RIGHT ->
            for (i in 4 downTo 1) {
                val row = this.getRow(i, 4 downTo 1)
                flag = this.moveValuesInRowOrColumn(row)
                if (flag)
                    count++
            }
        UP ->
            for (i in 1..4) {
                val column = this.getColumn(1..4, i)
                flag = this.moveValuesInRowOrColumn(column)
                if (flag)
                    count++
            }
        DOWN ->
            for (i in 4 downTo 1) {
                val column = this.getColumn(4 downTo 1, i)
                flag = this.moveValuesInRowOrColumn(column)
                if (flag)
                    count++
            }

    }

    return count > 0

}