package levels

class Level2 : Level {
    data class Command(val type: String, val value: Int)

    override fun run(@Load("input.txt") input: String) {
        val commands = input
            .lines()
            .map { it.split(" ") }
            .map { Command(it[0], it[1].toInt()) }

        part(1) {
            val pos = object {
                var horizontal = 0;
                var depth = 0;
            }

            commands.forEach { command ->
                with(command) {
                    when (type) {
                        "forward" -> pos.horizontal += value
                        "up" -> pos.depth -= value
                        "down" -> pos.depth += value
                    }
                }
            }

            pos.horizontal * pos.depth
        }

        part(2) {
            val pos = object {
                var horizontal = 0;
                var depth = 0;
                var aim = 0;
            }

            commands.forEach { command ->
                with(command) {
                    when (type) {
                        "forward" -> {
                            pos.horizontal += value;
                            pos.depth += pos.aim * value;
                        }
                        "up" -> pos.aim -= value
                        "down" -> pos.aim += value
                    }
                }
            }

            pos.horizontal * pos.depth
        }
    }
}
