package levels

class Level1 : Level {
    override val inputFilename = "input.txt"

    override fun run(input: String) {
        println("Running level 1")
        println(input)
    }

}
