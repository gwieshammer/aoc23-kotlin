
val Cards = listOf("A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2")
class HandsAndBids(
    val hands: String,
    val bid: Long
) : Comparable<HandsAndBids> {
    val countCardsEach : Map<Char,Int>
    init {
        countCardsEach = mutableMapOf()
        hands.chars().forEach {
            c ->
            countCardsEach.putIfAbsent(c.toChar(), 0 )
            countCardsEach[c.toChar()] = countCardsEach[c.toChar()]!! + 1
        }
    }
    val getKindFuns = listOf<(HandsAndBids) -> Boolean>(
        { h -> h.isFiveOfAKind() },
        { h -> h.isFourOfAKind() },
        { h -> h.isFullHouseKind() },
        { h -> h.isThreeOfAKind() },
        { h -> h.isTwoPairKind() },
        { h -> h.isOnePairKind() },
        { h -> h.isHighCardKind() },
    )
    // neg if less than other
    override fun compareTo(other: HandsAndBids): Int {
        var thisKind = -1
        var otherKind = -1
        for ((index, kindFun) in getKindFuns.withIndex()) {
            if (kindFun(this)) {
                thisKind = index
                break
            }
        }
        for ((index, kindFun) in getKindFuns.withIndex()) {
            if (kindFun(other)) {
                otherKind = index
                break
            }
        }
        return if (thisKind > otherKind) {
            -1
        } else if (thisKind < otherKind) {
            1
        } else {
            compareFirstLetter(hands, other.hands)
        }
    }
    fun compareFirstLetter(me: String, other: String) : Int {
        if (Cards.indexOf(me[0].toString()) > Cards.indexOf(other[0].toString())) {
            return -1
        } else if (Cards.indexOf(me[0].toString()) < Cards.indexOf(other[0].toString())) {
            return 1
        } else if (me.length > 1) {
            return compareFirstLetter(me.substring(1), other.substring(1))
        }
        return 0
    }
    fun isFiveOfAKind() : Boolean {
        return countCardsEach.values.filter { it == 5 }.count() == 1
    }
    fun isFourOfAKind() : Boolean {
        return countCardsEach.values.filter { it == 4 }.count() == 1
    }
    fun isFullHouseKind() : Boolean {
        return countCardsEach.values.filter { it == 3 }.count() == 1
                && countCardsEach.values.filter { it == 2 }.count() == 1
    }
    fun isThreeOfAKind() : Boolean {
        return countCardsEach.values.filter { it == 3 }.count() == 1
                && countCardsEach.values.filter { it == 2 }.count() == 0
    }
    fun isTwoPairKind() : Boolean {
        return countCardsEach.values.filter { it == 2 }.count() == 2
    }
    fun isOnePairKind() : Boolean {
        return countCardsEach.values.filter { it == 2 }.count() == 1
    }
    fun isHighCardKind() : Boolean {
        return countCardsEach.values.filter { it == 1 }.count() == 5
    }
}
val CardsWithJoker = listOf("A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2", "J")
class HandsAndBids2(
    val hands: String,
    val bid: Long
) : Comparable<HandsAndBids2> {
    val countCardsEach : Map<Char,Int>
    val notJokerCardsEach : Map<Char,Int>
    val jokerCount : Int
    init {
        countCardsEach = mutableMapOf()
        hands.chars().forEach {
                c ->
            countCardsEach.putIfAbsent(c.toChar(), 0 )
            countCardsEach[c.toChar()] = countCardsEach[c.toChar()]!! + 1
        }
        jokerCount = if (countCardsEach.containsKey('J')) { countCardsEach['J']!! } else { 0 }
        notJokerCardsEach = countCardsEach.filter { it.key != 'J' }
    }
    val getKindFuns = listOf<(HandsAndBids2) -> Boolean>(
        { h -> h.isFiveOfAKind() },
        { h -> h.isFourOfAKind() },
        { h -> h.isFullHouseKind() },
        { h -> h.isThreeOfAKind() },
        { h -> h.isTwoPairKind() },
        { h -> h.isOnePairKind() },
        { h -> h.isHighCardKind() },
    )
    // neg if less than other
    override fun compareTo(other: HandsAndBids2): Int {
        var thisKind = -1
        var otherKind = -1
        for ((index, kindFun) in getKindFuns.withIndex()) {
            if (kindFun(this)) {
                thisKind = index
                break
            }
        }
        for ((index, kindFun) in getKindFuns.withIndex()) {
            if (kindFun(other)) {
                otherKind = index
                break
            }
        }
        return if (thisKind > otherKind) {
            -1
        } else if (thisKind < otherKind) {
            1
        } else {
            compareFirstLetter(hands, other.hands)
        }
    }
    fun compareFirstLetter(me: String, other: String) : Int {
        if (CardsWithJoker.indexOf(me[0].toString()) > CardsWithJoker.indexOf(other[0].toString())) {
            return -1
        } else if (CardsWithJoker.indexOf(me[0].toString()) < CardsWithJoker.indexOf(other[0].toString())) {
            return 1
        } else if (me.length > 1) {
            return compareFirstLetter(me.substring(1), other.substring(1))
        }
        return 0
    }
    fun isFiveOfAKind() : Boolean {
        return notJokerCardsEach.values.filter { it == 5 }.count() == 1
                || (notJokerCardsEach.values.filter { it == 4 }.count() == 1 && jokerCount == 1)
                || (notJokerCardsEach.values.filter { it == 3 }.count() == 1 && jokerCount == 2)
                || (notJokerCardsEach.values.filter { it == 2 }.count() == 1 && jokerCount == 3)
                || jokerCount >= 4
    }
    fun isFourOfAKind() : Boolean {
        return notJokerCardsEach.values.filter { it == 4 }.count() == 1
                || (notJokerCardsEach.values.filter { it == 3 }.count() == 1 && jokerCount == 1)
                || (notJokerCardsEach.values.filter { it == 2 }.count() == 1 && jokerCount == 2)
                || jokerCount >= 3
    }
    fun isFullHouseKind() : Boolean {
        return (notJokerCardsEach.values.filter { it == 3 }.count() == 1
                && notJokerCardsEach.values.filter { it == 2 }.count() == 1)
                || (notJokerCardsEach.values.filter { it == 2 }.count() == 2 && jokerCount == 1)
    }
    fun isThreeOfAKind() : Boolean {
        return notJokerCardsEach.values.filter { it == 3 }.count() == 1
                || (notJokerCardsEach.values.filter { it == 2 }.count() == 1 && jokerCount == 1)
                || jokerCount >= 2
    }
    fun isTwoPairKind() : Boolean {
        return notJokerCardsEach.values.filter { it == 2 }.count() == 2
                || (notJokerCardsEach.values.filter { it == 2 }.count() == 1 && jokerCount == 1)
    }
    fun isOnePairKind() : Boolean {
        return countCardsEach.values.filter { it == 2 }.count() == 1
                || jokerCount >= 1
    }
    fun isHighCardKind() : Boolean {
        return countCardsEach.values.filter { it == 1 }.count() == 5
    }
}
fun main() {

    fun getInput(input: List<String>): List<HandsAndBids> {
        val handsAndBids = mutableListOf<HandsAndBids>()
        for (line in input) {
            val parts = line.split(" ").map { it.trim() }
            handsAndBids.add(HandsAndBids(parts[0], parts[1].toLong()))
        }
        return handsAndBids
    }
    fun part1(input: List<String>): Long {
        val handsAndBids = getInput(input)
        return handsAndBids.sorted().withIndex().sumOf { it -> (it.index + 1) * it.value.bid }
    }
    fun getInput2(input: List<String>): List<HandsAndBids2> {
        val handsAndBids = mutableListOf<HandsAndBids2>()
        for (line in input) {
            val parts = line.split(" ").map { it.trim() }
            handsAndBids.add(HandsAndBids2(parts[0], parts[1].toLong()))
        }
        return handsAndBids
    }
    fun part2(input: List<String>): Long {
        val handsAndBids = getInput2(input)
        return handsAndBids.sorted().withIndex().sumOf { it -> (it.index + 1) * it.value.bid }
    }

    // test if implementation meets criteria from the description
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440L)
    check(part2(testInput) == 5905L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println() // 254 461 044 too high
}

