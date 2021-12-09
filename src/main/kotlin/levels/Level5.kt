package levels

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

        part(1) {
            val points = hashMapOf<Vector, Int>()

            val toConsider = commands.filter { it.isStraight() }

            toConsider.forEach { (from, to) ->
                val range = from..to
                range.forEach { point ->
                    points[point] = points.getOrDefault(point, 0) + 1
                }
            }

            points.values.count() { it > 1 }
        }
    }
}

fun Pair<Vector, Vector>.isStraight(): Boolean =
    this.first.x == this.second.x || this.first.y == this.second.y

class StraightVectorRange(override val start: Vector, override val endInclusive: Vector) :
    ClosedRange<Vector>, Iterable<Vector> {
    init {
        require((start to endInclusive).isStraight()) { "The vector range $start -> $endInclusive must create a straight line" }
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
