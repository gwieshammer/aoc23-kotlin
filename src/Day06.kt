import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {

    fun getOneRaceWinningAccelerationTimes(totalRaceTime: Long, currentRecordDistance: Long) :Long {
        val x1 = (totalRaceTime - sqrt(0.0 + totalRaceTime * totalRaceTime - 4 * currentRecordDistance)) / 2
        val x2 = (totalRaceTime + sqrt(0.0 + totalRaceTime * totalRaceTime - 4 * currentRecordDistance)) / 2
        var minimumAccelerationTime = ceil(x1)
        if (minimumAccelerationTime == x1) {
            minimumAccelerationTime += 1
        }
        var maximumAccelerationTime = floor(x2)
        if (maximumAccelerationTime == x2) {
            maximumAccelerationTime -= 1
        }
        return (maximumAccelerationTime.toLong() - minimumAccelerationTime.toLong() + 1)
    }

    fun calculate(
        times: List<Long>,
        distances: List<Long>,
    ): Long {
        var winNr = 1L
        for (race in times.indices) {
            val t = times[race]
            val d = distances[race]
            winNr *= getOneRaceWinningAccelerationTimes(t, d)
        }
        return winNr
    }

    fun part1(input: List<String>): Long {
        val times = input[0].split(":")[1].trim().split("""\s+""".toRegex()).map { it.toLong() }
        val distances = input[1].split(":")[1].trim().split("""\s+""".toRegex()).map { it.toLong() }
        return calculate(times, distances)
    }

    fun part2(input: List<String>): Long {
        val time = input[0].split(":")[1].replace(" ", "").toLong()
        val distance = input[1].split(":")[1].replace(" ", "").toLong()
        return getOneRaceWinningAccelerationTimes(time, distance)
    }

    // test if implementation meets criteria from the description
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

