package levels

data class Cell(val number: Int, var marked: Boolean = false) {
    override fun toString(): String {
        return if (marked) "[$number]" else " $number "
    }
}

typealias Board = Array<Array<Cell>>

fun Board.toBoardString() =
    this.joinToString("\n") { row ->
        row.joinToString(",") { it.toString().padStart(4) }
    }

class Level4 : Level {
    override fun run(@Load("input.txt") input: String) {
        val lines = input.split("\n")
        val toDraw = lines
            .first()
            .split(",")
            .map(String::toInt)

        val boards: Array<Board> = lines
            .drop(1)
            .chunked(6)
            .map { it.drop(1) }
            .map {
                it
                    .map { row ->
                        row
                            .trim()
                            .split(Regex("\\s+"))
                            .map(String::toInt)
                            .map(::Cell)
                            .toTypedArray()
                    }
                    .toTypedArray()
            }
            .toTypedArray()

        val winnerBoardIds = hashSetOf<Int>()
        val winnerBoardScores = mutableListOf<Int>()

        toDraw.forEach { number ->
            boards.forEachIndexed { boardIndex, board ->
                val cells = board.flatten()
                cells.filter { it.number == number }
                    .forEach { it.marked = true }

                if(!winnerBoardIds.contains(boardIndex) && board.hasWon()) {
                    winnerBoardIds.add(boardIndex)
                    val unmarkedSum = cells.filter { !it.marked }
                        .sumOf { it.number }
                    val score = unmarkedSum * number
                    winnerBoardScores.add(score)
                }
            }
        }

        part(1) {
            winnerBoardScores.first()
        }

        part(1) {
            winnerBoardScores.last()
        }
    }

    fun Board.hasWon(): Boolean {
        for (y in 0 until this.size) {
            val row = this[y]
            if (row.all { it.marked }) {
                return true
            }
        }

        for (x in 0 until this.first().size) {
            var ok = true
            for (y in 0 until this.size) {
                val cell = this[y][x]
                if (!cell.marked) {
                    ok = false
                }
            }

            if(ok) {
                return true
            }
        }

        return false
    }
}
