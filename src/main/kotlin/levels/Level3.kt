package levels

class Level3 : Level {
    override fun run(@Load("input.txt") input: String) {
        val bits = input
            .split("\n")
            .map { line -> line.toCharArray().map { it == '1' } }

        part(1) {
            val rotated = bits.rotate()

            val gammaBits = rotated.indices.map { i ->
                val b = rotated[i]
                b.groupingBy { it }
                    .eachCount()
                    .maxByOrNull { it.value }
                    ?.key!!
            }

            val gamma = gammaBits.toInt()
            val epsilon = gammaBits.map { !it }
                .toInt()

            gamma * epsilon
        }
    }

    private inline fun <reified T> List<List<T>>.rotate(): Array<Array<T>> {
        val bitsPerColumn = Array(this.first().size) { mutableListOf<T>() }

        this.forEach { row ->
            row.forEachIndexed { i, bit ->
                bitsPerColumn[i].add(bit)
            }
        }

        return bitsPerColumn
            .map { it.toTypedArray() }
            .toTypedArray()
    }

    private fun Iterable<Boolean>.toInt(): Int {
        return this
            .map { if (it) 1 else 0 }
            .reversed()
            .mapIndexed { i, bit -> bit shl i }
            .reduce { acc, bit -> acc or bit }
    }
}
