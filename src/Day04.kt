import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

fun main() {
    val lineRegex = """^Card\s+(\d+)\s*:\s*([\d\s]+)\s+\|\s+([\d\s]+)$""".toRegex()
    val lineProcessor = fun (line: String, operateOnLine: (cardNr: Int, correctCount: Int) -> Unit) {
        val matchResult = lineRegex.find(line)!!
        val cardNr = matchResult.groupValues[1].trim().toInt()
        val winningCard = matchResult.groupValues[2].split("""\s+""".toPattern()).map { it.toInt() }
        val testingCard = matchResult.groupValues[3].split("""\s+""".toPattern()).map { it.toInt() }
        val correct = testingCard.filter { winningCard.contains(it) }
        operateOnLine(cardNr, correct.size)
    }
    fun part1(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            lineProcessor(line) {
                _, correctCount ->
                    if (correctCount > 0) {
                        sum += ((2.0).pow( (correctCount - 1).toDouble() )).toInt()
                    }

            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var maxCardNr = 0
        val countMatches = mutableMapOf<Int,Int>()
        val countCards = mutableMapOf<Int,Int>()
        // build maps of count matches and count cards total
        for (line in input) {
            lineProcessor(line) { cardNr, correctCount ->
                countMatches[cardNr] = correctCount
                countCards[cardNr] = 1
                maxCardNr = max(cardNr, maxCardNr)
            }
        }
        // apply count matches to the count of the following n cards
        for (i in 1..maxCardNr) {
            val c = countMatches[i]!!
            if (countMatches[i]!! > 0) {
                for (j in (i+1)..min(i+c, maxCardNr)) {
                    countCards[j] = countCards[j]!! + countCards[i]!!
                }
            }
        }
        return countCards.values.sum()
    }

    // test if implementation meets criteria from the description
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

