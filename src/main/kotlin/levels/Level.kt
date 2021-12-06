package levels

interface Level {
    val inputFilename: String

    fun run(input: String)
}
