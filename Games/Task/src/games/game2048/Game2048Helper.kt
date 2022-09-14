package games.game2048

/*
 * This function moves all the non-null elements to the beginning of the list
 * (by removing nulls) and merges equal elements.
 * The parameter 'merge' specifies the way how to merge equal elements:
 * it returns a new element that should be present in the resulting list
 * instead of two merged elements.
 *
 * If the function 'merge("a")' returns "aa",
 * then the function 'moveAndMergeEqual' transforms the input in the following way:
 *   a, a, b -> aa, b
 *   a, null -> a
 *   b, null, a, a -> b, aa
 *   a, a, null, a -> aa, a
 *   a, null, a, a -> aa, a
 *
 * You can find more examples in 'TestGame2048Helper'.
*/
fun <T : Any> List<T?>.moveAndMergeEqual(merge: (T) -> T): List<T> {
    val res = this.filterNotNull().toMutableList()
    val fin = mutableListOf<T>()
    var boom = 0
    for (i in 0 until res.size) {
        val j = i + 1
        if (boom > 0) {
            boom = 0
            continue
        }
        if (j < res.size) {
            val f = res[i]
            val s = res[j]
            if (f == s) {
                fin.add(merge(f))
                boom++
            } else {
                fin.add(f)
            }
        } else if (j == res.size) {
            fin.add(res[i])
        } else {
            continue
        }
    }
    return fin
}

