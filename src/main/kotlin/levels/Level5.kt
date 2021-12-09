package levels

import kotlin.math.absoluteValue
import kotlin.math.sign

data class Vector(var x: Int, var y: Int) : Comparable<Vector> {
    override fun compareTo(other: Vector) = x.compareTo(other.x)
    operator fun rangeTo(other: Vector) = StraightVectorRange(this, other)
}

class Level5 : Level {
    override fun run(@Load("input.txt") input: String) {
        val commands = input
            .lines()
            .asSequence()
            .flatMap { it.split(" -> ") }
            .flatMap { it.split(",") }
            .map(String::toInt)
            .chunked(2) { Vector(it[0], it[1]) }
            .chunked(2) { it[0] to it[1] }

        val points = hashMapOf<Vector, Int>()

        part(1) {
            commands
                .filter { (start, end) -> isStraight(start, end) }
                .flatMap { (start, end) -> (start..end) }
                .groupingBy { it }
                .eachCount()
                .filter { it.value > 1 }
                .count()

        }

        part(2) {
            commands
                .flatMap { (start, end) ->
                    if (isStraight(start, end))
                        (start..end).asSequence()
                    else
                        pointsOfDiagonal(start, end)
                }
                .groupingBy { it }
                .eachCount()
                .filter { it.value > 1 }
                .count()
        }
    }
}

fun pointsOfDiagonal(from: Vector, to: Vector): Sequence<Vector> {
    val xFac = (to.x - from.x).sign
    val yFac = (to.y - from.y).sign

    val dist = (to.x - from.x).absoluteValue

    return (0..dist)
        .asSequence()
        .map { Vector(from.x + it * xFac, from.y + it * yFac) }
}


fun isStraight(a: Vector, b: Vector): Boolean = a.x == b.x || a.y == b.y

class StraightVectorRange(override val start: Vector, override val endInclusive: Vector) :
    ClosedRange<Vector>, Iterable<Vector> {
    init {
        require(isStraight(start, endInclusive)) { "The vector range $start -> $endInclusive must create a straight line" }
    }

    override fun iterator(): Iterator<Vector> {
        val xChanges = start.x != endInclusive.x

        val range =
            if (xChanges)
                if (start.x > endInclusive.x) endInclusive.x..start.x else start.x..endInclusive.x
            else
                if (start.y > endInclusive.y) endInclusive.y..start.y else start.y..endInclusive.y

        val buildVector =
            if (xChanges)
                { x: Int -> Vector(x, start.y) }
            else
                { y: Int -> Vector(start.x, y) }

        val componentIter = range.iterator()

        return object : Iterator<Vector> {
            override fun hasNext() = componentIter.hasNext()
            override fun next() = buildVector(componentIter.next())
        }
    }
}
