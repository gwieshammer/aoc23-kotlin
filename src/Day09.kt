
class ValueHistory(val values: List<Long>) {
    var diffs : ValueHistory? = null
    init {
        if (values.filter { it != 0L }.size > 0) {
            val d = mutableListOf<Long>()
            var prev: Long? = null
            for (v in values) {
                if (prev != null) {
                    d.add(v - prev)
                }
                prev = v
            }
            diffs = ValueHistory(d)
        }
    }
    fun predictNext() : Long {
        if (values.none { it != 0L }) {
            return 0L
        }
        return values.last() + diffs!!.predictNext()
    }
    fun predictPrevious() : Long {
        if (values.none { it != 0L }) {
            return 0L
        }
        return values[0] - diffs!!.predictPrevious()
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        var result = 0L
        for (line in input) {
            val v = ValueHistory(line.split(" ").map { it.trim().toLong() })
            result += v.predictNext()
        }
        return result
    }
    fun part2(input: List<String>): Long {
        var result = 0L
        for (line in input) {
            val v = ValueHistory(line.split(" ").map { it.trim().toLong() })
            result += v.predictPrevious()
        }
        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114L)
    check(part2(testInput) == 2L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
