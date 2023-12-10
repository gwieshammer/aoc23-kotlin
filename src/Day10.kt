import java.awt.geom.Line2D
import kotlin.math.max

enum class Direction(val canGo: (current: Point, map: PlayMap) -> Boolean, val translate: (current: Point) -> Point, val getOpposite: () -> Direction) {
    NORTH({ current, _ -> current.y > 0 }, { current -> Point(current.x, current.y - 1) }, { SOUTH} ),
    EAST({ current, map -> current.x < map.mapWidth - 1 }, { current -> Point(current.x + 1, current.y)}, { WEST }),
    SOUTH({ current, map -> current.y < map.mapHeight - 1}, { current -> Point(current.x, current.y + 1)}, { NORTH } ),
    WEST({ current, _ -> current.x > 0}, { current -> Point(current.x - 1, current.y) }, { EAST });
}
enum class TileType(
    val char: Char,
    val connects : List<Direction>
) {
    PIPE('|', listOf(Direction.NORTH, Direction.SOUTH)),
    DASH('-', listOf(Direction.EAST, Direction.WEST)),
    L('L', listOf(Direction.NORTH, Direction.EAST)),
    J('J', listOf(Direction.NORTH, Direction.WEST)),
    SEVEN('7', listOf(Direction.SOUTH, Direction.WEST)),
    F('F', listOf(Direction.EAST, Direction.SOUTH)),
    DOT('.', listOf()),
    S('S', listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST))
    ;
    companion object {
        fun fromChar(char: Char) : TileType {
            return entries.firstOrNull { it.char == char }!!
        }
    }
}

data class Point(val x: Int, val y: Int)
data class Line(val start: Point, val end: Point) {
    fun intersectsWith(other: Line) : Boolean {
        return Line2D.linesIntersect(start.x.toDouble(), start.y.toDouble(),
            end.x.toDouble(), end.y.toDouble(),
            other.start.x.toDouble(), other.start.y.toDouble(),
            other.end.x.toDouble(), other.end.y.toDouble()
            )
    }
}
data class Route(
    val positions : MutableList<Point> = mutableListOf(),
    var isLoop : Boolean = false
) {
    fun length() : Int {
        return positions.size
    }
}

class PlayMap(input: List<String>) {
    private val map = mutableMapOf<Point,TileType>()
    val mapWidth : Int
    val mapHeight : Int
    lateinit var start : Point
    init {
        var maxWidth = 0
        mapHeight = input.size
        for (dy in input.indices) {
            maxWidth = max(maxWidth, input[dy].length)
            for (dx in 0 until input[dy].length) {
                val c = input[dy][dx]
                map[Point(dx, dy)] = TileType.fromChar(c)
                if (c == 'S') {
                    start = Point(dx, dy)
                }
            }
        }
        mapWidth = maxWidth
    }
    fun findRoute(route: Route, start: Point, current: Point) : Route? {
        if (route.length() > 0 && current.x == start.x && current.y == start.y) {
            route.isLoop = true
            return route
        }
        val curTile = map[current]!!
        for (direction in Direction.entries) {
            if (curTile.connects.contains(direction) && direction.canGo(current, this)) {
                val position = direction.translate(current)
                val tile = map[position]!!
                if (!route.positions.contains(position) && tile.connects.contains(direction.getOpposite())) {
                    val testRoute = route.copy()
                    testRoute.positions.add(position)
                    val r = findRoute(testRoute, start, position)
                    if (r!!.isLoop) {
                        return r
                    }
                }
            }
        }

        return null
    }
    fun countTilesInLoop(r: Route) : Int {
        var countTilesInside = 0
        val edges = mutableListOf<Line>()
        val points = mutableListOf<Point>()
        points.add(r.positions.last())
        r.positions.forEach { points.add(it) }
        var curPos : Point? = null
        var lastPos : Point? = null
        for (idx in 0 until points.size) {
            val px = points[idx].x
            val py = points[idx].y
            if (curPos == null) {
                curPos = Point(px, py)
            } else if (lastPos == null) {
                lastPos = Point(px, py)
            } else {
                val dx = lastPos.x - curPos.x
                val dy = lastPos.y - curPos.y
                if (lastPos.x + dx == px && lastPos.y + dy == py) {
                    lastPos = Point(px, py)
                } else {
                    edges.add(Line(curPos, lastPos))
                    curPos = lastPos
                    lastPos = Point(px, py)
                }
            }
        }
        edges.add(Line(curPos!!, lastPos!!))

        for (dy in 0 until mapHeight) {
            for (dx in 0 until mapWidth) {
                val curPoint = Point(dx, dy)
                val tile = map[curPoint]
                if (tile == null || tile == TileType.DOT || !r.positions.contains(curPoint)) {
                    var countIntersects = 0
                    for (edge in edges) {
                        if (Line(Point(-100000, dy-1), curPoint).intersectsWith(edge)) {
                            countIntersects ++
                        }
                    }
                    if (countIntersects % 2 == 1) {
                        countTilesInside += 1
                    }
                }
            }
        }
        return countTilesInside
    }
}
fun main() {
    fun part1(input: List<String>): Int {
        val map = PlayMap(input)
        val r = map.findRoute(Route(),map.start, map.start)
        println(map)
        return r!!.length() / 2
    }
    fun part2(input: List<String>): Int {
        val map = PlayMap(input)
        val r = map.findRoute(Route(),map.start, map.start)
        return map.countTilesInLoop(r!!)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 4)
    check(part2(testInput) == 1)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
