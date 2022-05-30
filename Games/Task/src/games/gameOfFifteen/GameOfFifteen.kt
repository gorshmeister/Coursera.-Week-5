package games.gameOfFifteen

import board.Cell
import board.Direction
import board.Direction.*
import board.GameBoard
import board.createGameBoard
import games.game.Game
import games.game2048.*
import games.game2048.moveValuesInRowOrColumn

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        board.addValues(initializer)
    }

    override fun canMove(): Boolean {
        return true
    }

    override fun hasWon(): Boolean {
        val list = (1..15).toList()
        val result = mutableListOf<Int?>()

        board.getAllCells().forEach {
            result += board.get(it)
        }

        result.mapNotNull { it }

        return list == result.mapNotNull { it }
    }

    override fun processMove(direction: Direction) {
        board.moveValues(direction)
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}

/*
 * Add a new value produced by 'initializer' to a specified cell in a board.
 */
fun GameBoard<Int?>.addValues(initializer: GameOfFifteenInitializer) {
    val cells = this.getAllCells()
    val list = initializer.initialPermutation

    val map = mutableMapOf<Cell, Int?>()

    for ((i, cell) in cells.withIndex()) {
        try {
            map += cell to list[i]
        } catch (e: Exception) {
            map += cell to null
        }
    }

    map.forEach { (cell, value) ->
        this[cell] = value
    }
}

fun GameBoard<Int?>.moveValues(direction: Direction) {
    when (direction) {
        LEFT ->
            replaceValues(LEFT.reversed())
        RIGHT ->
            replaceValues(RIGHT.reversed())
        UP ->
            replaceValues(UP.reversed())
        DOWN ->
            replaceValues(DOWN.reversed())
    }
}

fun GameBoard<Int?>.replaceValues(direction: Direction) {
    val cell = this.find { it == null }
    val neighbour = cell?.getNeighbour(direction)

    if (neighbour != null) {
        this[cell] = this[neighbour].also { this[neighbour] = this[cell] }
    }
}




