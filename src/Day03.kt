import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        var lineNr = 0
        for (line in input) {
            val matchResult = """\d+""".toRegex().findAll(line)
            matchResult.forEach {
                if (checkSymbol(input, lineNr, it.range.first, it.range.last)) {
                    sum += it.value.toInt()
                }
            }
            lineNr ++
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        val starsAndParts = mutableMapOf<String,MutableList<Int>>()
        for ((lineNr, line) in input.withIndex()) {
            val matchResult = """\d+""".toRegex().findAll(line)
            matchResult.forEach {match ->
                val listOfIntRanges = getStars(input, lineNr, match.range.first, match.range.last)
                listOfIntRanges.forEach { starPosition ->
                    if (!starsAndParts.containsKey(starPosition)) {
                        starsAndParts[starPosition] = mutableListOf<Int>()
                    }
                    starsAndParts[starPosition]!!.add(match.value.toInt())
                }
            }
        }
        starsAndParts.values.forEach {
            if (it.size == 2) {
                sum += it[0] * it[1]
            }
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

fun checkSymbol(input: List<String>, lineNr: Int, sx: Int, ex: Int) : Boolean {
    var foundSymbol = false;
    lineLoop@ for (y in IntRange(max(0, lineNr - 1), min(input.size - 1, lineNr + 1))) {
        for (x in IntRange(max(0, sx - 1), min(input[y].length - 1, ex + 1))) {
            val c = input[y][x]
            if (!c.isDigit() && c != '.') {
                foundSymbol = true;
                break@lineLoop
            }
        }
    }
    return foundSymbol
}

fun getStars(input: List<String>, lineNr: Int, sx: Int, ex: Int) : List<String> {
    val foundPositions = mutableListOf<String>()
    for (y in IntRange(max(0, lineNr - 1), min(input.size - 1, lineNr + 1))) {
        for (x in IntRange(max(0, sx - 1), min(input[y].length - 1, ex + 1))) {
            val c = input[y][x]
            if (c == '*') {
                foundPositions.add("${x},${y}")
            }
        }
    }
    return foundPositions
}