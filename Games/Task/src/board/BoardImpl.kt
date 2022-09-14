package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

data class MyCells(val i: Int, val j: Int)
val res = hashMapOf<MyCells, Cell>()

open class SquareBoardImpl(override val width: Int): SquareBoard {

    init {
        for (i in 1..width)
            for (j in 1..width)
                res[MyCells(i,j)] = Cell(i,j)
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        if(i < 1 || i > width || j< 1 || j > width)
            return null
        return res.getValue(MyCells(i,j))
    }

    override fun getCell(i: Int, j: Int): Cell {
        val cell = getCellOrNull(i,j)
        if (cell == null)
            throw IllegalArgumentException("Incorrect Values of i and j")
        else
            return cell
    }

    override fun getAllCells(): Collection<Cell> {
        val re = mutableListOf<Cell>()
        for (i in 1..width)
            for(j in 1..width)
                re.add(res.getValue(MyCells(i,j)))
        return re
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val re = mutableListOf<Cell>()
        for (j in jRange)
            if (getCellOrNull(i,j) != null)
                re.add(getCell(i,j))
        return re
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val re = mutableListOf<Cell>()
        for (i in iRange)
            if (getCellOrNull(i,j) != null)
                re.add(getCell(i,j))
        return re
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when(direction) {
            UP -> getCellOrNull(i-1, j)
            DOWN -> getCellOrNull(i+1, j)
            RIGHT -> getCellOrNull(i, j+1)
            LEFT -> getCellOrNull(i, j-1)
        }
    }

}

class GameBoardImpl<T>(override val width: Int): SquareBoardImpl(width), GameBoard<T> {

    private val fin = hashMapOf<MyCells,T?>()

    init {
        for (i in 1..width)
            for (j in 1..width) {
                res[MyCells(i, j)] = Cell(i, j)
                fin[MyCells(i,j)] = null
            }
    }

    override fun get(cell: Cell): T? {
        return fin[MyCells(cell.i,cell.j)]
    }

    override fun set(cell: Cell, value: T?) {
        fin[MyCells(cell.i, cell.j)] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        val re = mutableListOf<Cell?>()
        for ((key,value) in fin) {
            if (predicate(value)) {
                re.add(res[key])
            }
        }
        return re.filterNotNull()
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        for ((key,value) in fin) {
            if (predicate(value))
                return res[key]
        }
        return null
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        for (i in 1..width)
            for (j in 1..width) {
                if (predicate(fin[MyCells(getCell(i,j).i, getCell(i,j).j)]))
                    return true
            }
        return false
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        var fg = 0
        for (i in 1..width)
            for (j in 1..width) {
                if (!predicate(fin[(MyCells(getCell(i,j).i, getCell(i,j).j))]))
                    fg++
            }
        return (fg==0)
    }

}