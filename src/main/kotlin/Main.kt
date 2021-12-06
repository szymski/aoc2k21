import levels.Level
import java.io.File

fun main(args: Array<String>) {
    val levelName = args.getOrNull(1) ?: System.getenv("LEVEL") ?: "level1"
    println("Processing $levelName")

    val level = newLevelInstance(levelName)
    val inputPath = "input/$levelName/${level.inputFilename}"
    val inputContent = File(inputPath).readText()
    level.run(inputContent)
}

fun newLevelInstance(name: String): Level {
    val className = name.replaceFirstChar { it.toUpperCase() }
    val levelClass = Class.forName("levels.$className") ?: throw IllegalStateException("Class not found '$className'")
    val instance = levelClass.getDeclaredConstructor().newInstance()
    return instance as Level
}
