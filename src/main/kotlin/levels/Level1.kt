package levels

class Level1 : Level {
    override fun run(@Load("input.txt") input: String) {
        val depths = input.split("\n").map(String::toInt)

        part(1) {
            depths
                .zipWithNext { a, b -> b > a }
                .count { it }
        }

        part(2) {
            depths
                .windowed(3) { it.sum() }
                .zipWithNext { a, b -> b > a }
                .count { it }
        }
    }
}
