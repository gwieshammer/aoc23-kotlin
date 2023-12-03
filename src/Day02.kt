import kotlin.math.max

fun main() {
    val maxItems = mapOf(
        Pair("red", 12),
        Pair("green", 13),
        Pair("blue", 14),
    )
    fun part1(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            var gamePlayOk = true
            val (gameNrString, setsString) = line.split(":")
            val gameNr = """\d+""".toRegex().find(gameNrString)!!.value.toInt()
            val sets = setsString.trim().split(";")
            setLoop@ for (setString in sets) {
                val setPiecesStrings = setString.trim().split(",")
                for (pieceString in setPiecesStrings) {
                    val matchResult = """(\d+)\s(blue|red|green)""".toRegex().find(pieceString.trim())
                    val amount = matchResult!!.groupValues[1].toInt()
                    val color = matchResult.groupValues[2]
                    if (amount > maxItems[color]!!) {
                        gamePlayOk = false
                        break@setLoop
                    }
                }
            }
            if (gamePlayOk) {
                sum += gameNr
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            val minItems = mutableMapOf(
                Pair("red", 0),
                Pair("green", 0),
                Pair("blue", 0),
            )
            val (_, setsString) = line.split(":")
            val sets = setsString.trim().split(";")
            for (setString in sets) {
                val setPiecesStrings = setString.trim().split(",")
                for (pieceString in setPiecesStrings) {
                    val matchResult = """(\d+)\s(blue|red|green)""".toRegex().find(pieceString.trim())
                    val amount = matchResult!!.groupValues[1].toInt()
                    val color = matchResult.groupValues[2]
                    minItems[color] = max(minItems[color]!!, amount)
                }
            }
            sum += minItems["red"]!! * minItems["green"]!! * minItems["blue"]!!
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
