import kotlin.math.min

class ElfMap(
    val destinationStart: Long,
    val sourceStart: Long,
    private val length: Long
) {
    fun applies(source: Long): Boolean {
        return source >= sourceStart && source < sourceStart + length
    }
    fun map(source: Long): Long {
        return source + (destinationStart - sourceStart)
    }
    fun sourceIntersects(other: LongRange) : Boolean {
        return sourceStart <= other.last && sourceStart + length - 1 >= other.first
    }
    fun getSourceEnd() : Long {
        return sourceStart + length - 1L
    }
}

fun List<ElfMap>.lookup(source: Long): Long {
    for (m in this) {
        if (m.applies(source)) {
            return m.map(source)
        }
    }
    return source
}

fun main() {


    fun transformSingleRange(inputRange: LongRange, map: List<ElfMap>): MutableList<LongRange> {
        val r = mutableListOf<LongRange>()
        var run = inputRange.first
        val runTo = inputRange.last
        val mapsToGo = map
            .filter { it.sourceIntersects(inputRange) }
            .sortedBy { it.sourceStart }
        if (mapsToGo.isEmpty()) {
            r.add(inputRange)
            return r
        }
        var mapIdx = 0

        while (run < runTo && mapIdx < mapsToGo.size) {
            if (run < mapsToGo[mapIdx].sourceStart) {
                // add input unchanged up to start of map
                r.add(LongRange(run, mapsToGo[mapIdx].sourceStart - 1))
                run = mapsToGo[mapIdx].sourceStart
                continue
            }
            if (run >= mapsToGo[mapIdx].sourceStart) {
                val curMap = mapsToGo[mapIdx]
                var upTo : Long
                if (runTo <= curMap.getSourceEnd()) {
                    upTo = runTo
                } else {
                    upTo = mapsToGo[mapIdx].getSourceEnd()
                    mapIdx ++
                }
                val destinationStart = curMap.destinationStart + (run - curMap.sourceStart)
                val destinationEnd = curMap.destinationStart + (upTo - curMap.sourceStart)
                r.add(LongRange(
                    destinationStart,
                    destinationEnd
                ))
                run = upTo
                continue
            }
        }
        return r
    }

    fun transform(inputRanges: MutableList<LongRange>, map: List<ElfMap>) : MutableList<LongRange> {
        val r = mutableListOf<LongRange>()
        inputRanges.forEach { inputRange -> r.addAll(transformSingleRange(inputRange, map)) }
        return r
    }

    fun getElfMaps(
        input: List<String>
    ): Pair<MutableList<List<ElfMap>>, List<Long>> {
        var seeds : List<Long> = listOf()
        val maps: MutableList<List<ElfMap>> = mutableListOf()
        var r: MutableList<ElfMap>? = null
        for (line in input) {
            if (line.startsWith("seeds: ")) {
                seeds = input.first().split(":")[1].split(" ").map { it.trim() }.filter { it.isNotEmpty() }
                    .map { it.toLong() }
            } else if (line.trim().isBlank()) {
                // no op
            } else if (line[0].isLetter()) {
                // category
                if (r !== null) {
                    maps.add(r)
                }
                r = mutableListOf()
            } else if (line[0].isDigit()) {
                // map destination range start, the source range start, and the range length
                val v = line.split(" ").map { it.trim() }.filter { it.isNotEmpty() }.map { it.toLong() }
                r!!.add(ElfMap(v[0], v[1], v[2]))
            }
        }
        if (r !== null) {
            maps.add(r)
        }
        return Pair(maps, seeds)
    }

    fun part1(input: List<String>): Long {
        val mapsAndSeeds = getElfMaps(input)
        val maps: MutableList<List<ElfMap>> = mapsAndSeeds.first
        val seeds : List<Long> = mapsAndSeeds.second
        var minMap : Long? = null
        for (seed in seeds) {
            var v = seed
            for (map in maps) {
                v = map.lookup(v)
            }
            minMap = if (minMap == null) { v } else { min(minMap, v) }
        }
        return minMap!!
    }

    fun part2(input: List<String>): Long {
        val mapsAndSeeds = getElfMaps(input)
        val maps: MutableList<List<ElfMap>> = mapsAndSeeds.first
        val seedNumbers : List<Long> = mapsAndSeeds.second
        val seedNrIter = seedNumbers.listIterator()
        var runRanges = mutableListOf<LongRange>()
        while (seedNrIter.hasNext()) {
            val from = seedNrIter.next()
            val to = from + seedNrIter.next() - 1
            runRanges.add(LongRange(from, to))
        }

        maps.forEach {
            runRanges = transform(runRanges, it)
        }

        return runRanges.minOf { it.first }
    }

    // test if implementation meets criteria from the description
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

