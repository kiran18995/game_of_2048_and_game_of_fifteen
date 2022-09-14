package games.gameOfFifteen

interface GameOfFifteenInitializer {
    /*
     * Even permutation of numbers 1..15
     * used to initialized the first 15 cells on a board.
     * The last cell is empty.
     */
    val initialPermutation: List<Int>
}

@Suppress("UNREACHABLE_CODE")
class RandomGameInitializer : GameOfFifteenInitializer {
    /*
     * Generate a random permutation from 1 to 15.
     * `shuffled()` function might be helpful.
     * If the permutation is not even, make it even (for instance,
     * by swapping two numbers).
     */
    override val initialPermutation by lazy {
        var li: List<Int> = listOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15)
        li = li.shuffled()
        while (true) {
            if (isEven(li))
                return@lazy li
            else
                li = li.shuffled()
        }
        return@lazy li
    }
}

