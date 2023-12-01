fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            var a = -1
            var b = -1
            val lastIdx = line.length - 1
            for (i in 0..lastIdx) {
                if (a < 0 && line[i].isDigit()) {
                    a = line[i].digitToInt()
                }
                if (b < 0 && line[lastIdx - i].isDigit()) {
                    b = line[lastIdx - i].digitToInt()
                }
                if (a >= 0 && b >= 0) {
                    break
                }
            }
            sum += a * 10 + b
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        val digitNames = mapOf(Pair("one", 1),
            Pair("two", 2), Pair("three", 3),
            Pair("four", 4), Pair("five", 5),
            Pair("six", 6), Pair("seven", 7),
            Pair("eight", 8), Pair("nine", 9))
        for (line in input) {
            var a : Int = -1
            var b : Int = -1
            val lastIdx = line.length - 1
            for (i in 0..lastIdx) {
                if (a < 0) {
                    if (line[i].isDigit()) {
                        a = line[i].digitToInt()
                    } else {
                        for (entry in digitNames) {
                            if (line.startsWith(entry.key, i)) {
                                a = entry.value
                                break
                            }
                        }
                    }
                }
                if (b < 0) {
                    if (line[lastIdx - i].isDigit()) {
                        b = line[lastIdx - i].digitToInt()
                    } else {
                        for (entry in digitNames) {
                            if (line.startsWith(entry.key, lastIdx - i)) {
                                b = entry.value
                                break
                            }
                        }
                    }
                }
                if (a >= 0 && b >= 0) {
                    break
                }
            }
            sum += a * 10 + b
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
