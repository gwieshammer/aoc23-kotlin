import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun getGalaxyDescriptors(input: List<String>): Triple<MutableList<Pair<Int, Int>>, List<Int>, List<Int>> {
        val galaxies = mutableListOf<Pair<Int, Int>>()
        var minx = Int.MAX_VALUE
        var miny = Int.MAX_VALUE
        var maxx = 0
        var maxy = 0
        for ((idy, line) in input.withIndex()) {
            for ((idx, char) in line.toCharArray().withIndex()) {
                if (char == '#') {
                    galaxies.add(Pair(idx, idy))
                    minx = min(minx, idx)
                    miny = min(miny, idy)
                    maxx = max(maxx, idx)
                    maxy = max(maxy, idy)
                }
            }
        }
        val emptyCols = IntRange(minx, maxx)
            .filter { i -> !galaxies.map { it.first }.contains(i) }
        val emptyRows = IntRange(miny, maxy)
            .filter { i -> !galaxies.map { it.second }.contains(i) }
        return Triple(galaxies, emptyCols, emptyRows)
    }

    fun part1(input: List<String>): Long {
        var sum = 0L
        val (galaxies, emptyCols, emptyRows) = getGalaxyDescriptors(input)
        val galaxyIndices = galaxies.indices
        for (a in galaxyIndices.first until galaxyIndices.last) {
            for (b in a + 1 .. galaxyIndices.last) {
                val ax = galaxies[a].first
                val bx = galaxies[b].first
                val dx = abs(ax - bx) + emptyCols.count { IntRange(min(ax, bx), max(ax, bx)).contains(it) }
                val ay = galaxies[a].second
                val by = galaxies[b].second
                val dy = abs(ay - by) + emptyRows.count { IntRange(min(ay, by), max(ay, by)).contains(it) }
                sum += (dx + dy)
            }
        }
        return sum
    }
    fun part2(input: List<String>): Long {
        var sum = 0L
        val (galaxies, emptyCols, emptyRows) = getGalaxyDescriptors(input)
        val galaxyIndices = galaxies.indices
        for (a in galaxyIndices.first until galaxyIndices.last) {
            for (b in a + 1 .. galaxyIndices.last) {
                val ax = galaxies[a].first.toLong()
                val bx = galaxies[b].first.toLong()
                val dx = abs(ax - bx) + 999999L * emptyCols.count { LongRange(min(ax, bx), max(ax, bx)).contains(it) }
                val ay = galaxies[a].second.toLong()
                val by = galaxies[b].second.toLong()
                val dy = abs(ay - by) + 999999L * emptyRows.count { LongRange(min(ay, by), max(ay, by)).contains(it) }
                sum += (dx + dy)
            }
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374L)
    //check(part2(testInput) == 1030L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
