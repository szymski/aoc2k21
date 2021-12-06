package levels

interface Level {
    fun run(input: String)

    fun <T, R> T.part(number: Int, block: (T) -> R) =
        println("Part $number: ${block(this)}")
}

annotation class Load(val filename: String)
