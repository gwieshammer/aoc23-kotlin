import java.lang.RuntimeException
import kotlin.math.max

enum class LR {
    LEFT,
    RIGHT;
    companion object {
        fun byShort(short: Char): LR? {
            return entries.firstOrNull { it.name.startsWith(short) }
        }
    }
}
class Directions(private val directions: String) : Iterator<LR> {
    private var cursor : Int = 0
    override fun hasNext(): Boolean = true
    override fun next(): LR {
        val next = LR.byShort(directions[cursor])!!
        cursor ++
        if (cursor >= directions.length) {
            cursor = 0
        }
        return next
    }
}
class Node(val name: String, val left: String, val right: String) {
    companion object {
        fun fromString(line: String) : Node {
            val lineRegex = """^(\w+)\s*=\s*\((\w+)\s*,\s*(\w+)\s*\)$""".toRegex()
            val matchResult = lineRegex.find(line)!!
            val key = matchResult.groupValues[1]
            val left = matchResult.groupValues[2]
            val right = matchResult.groupValues[3]
            return Node(key, left, right)
        }
    }
}

fun main() {
    fun directionsAndNodeMap(input: List<String>): Pair<Directions?, MutableMap<String, Node>> {
        var directions: Directions? = null
        val nodeMap = mutableMapOf<String, Node>()
        for (line in input) {
            if (line.isBlank()) {
                continue
            }
            if (directions == null) {
                directions = Directions(line)
                continue
            }
            val node = Node.fromString(line)
            nodeMap[node.name] = node
        }
        return Pair(directions, nodeMap)
    }

    fun part1(input: List<String>): Long {
        val pair = directionsAndNodeMap(input)
        val directions: Directions? = pair.first
        val nodeMap = pair.second
        if (directions == null) {
            throw RuntimeException("Missing directions")
        }
        var curNode = "AAA"
        var steps = 0L
        while (curNode != "ZZZ") {
            val direction = directions.next()
            curNode = if (direction == LR.LEFT) {
                nodeMap[curNode]!!.left
            } else {
                nodeMap[curNode]!!.right
            }
            steps ++
        }
        return steps
    }
    fun findLcm(first: Long, second: Long): Long {
        val larger = max(first, second)
        val maxLcm = first * second
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % first == 0L && lcm % second == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }
    fun findLcm(steps: List<Long>): Long {
        var result = steps[0]
        for (i in 1 until steps.size) {
            result = findLcm(result, steps[i])
        }
        return result
    }
    fun part2(input: List<String>): Long {
        val pair = directionsAndNodeMap(input)
        val directions: Directions? = pair.first
        val nodeMap = pair.second
        if (directions == null) {
            throw RuntimeException("Missing directions")
        }
        var curNodes = nodeMap.keys.filter { it.endsWith('A') }
        val stepsNeeded = mutableMapOf<String,Long>()
        var steps = 0
        while (curNodes.isNotEmpty()) {
            val direction = directions.next()
            curNodes = if (direction == LR.LEFT) {
                curNodes.map { nodeMap[it]!!.left }
            } else {
                curNodes.map { nodeMap[it]!!.right }
            }
            steps ++
            val finalized = curNodes.filter { it.endsWith('Z') }
            finalized.forEach { stepsNeeded[it] = steps.toLong() }
            curNodes = curNodes.filter { !it.endsWith('Z') }
        }

        return findLcm(stepsNeeded.values.toList())
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 6L)
    val testInput2 = readInput("Day08_test2")
    check(part2(testInput2) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
