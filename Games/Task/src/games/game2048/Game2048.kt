package games.game2048

import board.Cell
import board.Direction
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

    override fun canMove() = board.any { it == null }

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
    val ini = initializer.nextValue(this)
    if (ini != null)
        this[ini.first] = ini.second
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
    val values = mutableListOf<Int?>()
    for(c in rowOrColumn) {
        values.add(this[c])
    }
    val fin = values.moveAndMergeEqual { it * 2 }
    if (values.size == fin.size) {
        return false
    }
    for ((i, c) in rowOrColumn.withIndex()) {
        if (i < fin.size)
            this[c] = fin[i]
        else
            this[c] = null
    }
    return true
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" to the specified direction
 * following the rules of the 2048 game .
 * Use the 'moveValuesInRowOrColumn' function above.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValues(direction: Direction): Boolean {
    val width = this.width
    val allCell = this.getAllCells()
    val origList = mutableListOf<Int>()
    for (ac in allCell) {
        val t = this[ac]
        if (t == null)
            origList.add(0)
        else
            origList.add(t)
    }
    when(direction) {
        Direction.UP -> for (j in width downTo 1){ moveValuesInRowOrColumn(this.getColumn(1..width, j).toList()) }
        Direction.DOWN -> for (j in 1..width){ moveValuesInRowOrColumn(this.getColumn(width downTo 1, j).toList())}
        Direction.RIGHT -> for (i in width downTo 1){ moveValuesInRowOrColumn(this.getRow(i, width downTo 1).toList())}
        Direction.LEFT -> for (i in 1..width){ moveValuesInRowOrColumn(this.getRow(i, 1..width).toList())}
    }
    val newList = mutableListOf<Int>()
    for (ac in allCell) {
        val t = this[ac]
        if (t == null)
            newList.add(0)
        else
            newList.add(t)
    }
    if (origList.size == newList.size) {
        var fg = 0
        for (i in 0 until origList.size) {
            if (origList[i] != newList[i]) {
                fg++
            }
        }
        return (fg != 0)
    }
    return true
}