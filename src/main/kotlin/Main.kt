import levels.Level
import levels.Load
import java.io.File
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

fun main(args: Array<String>) {
    val levelName = args.getOrNull(1) ?: System.getenv("LEVEL") ?: "level5"
    println("Processing $levelName")

    val level = newLevelInstance(levelName)
    val inputContent = loadInputFile(level, levelName)
    level.run(inputContent)
}

private fun loadInputFile(level: Level, levelName: String): String {
    val runMethod = level::class.members.find { it.name == Level::run.name }
    val loadAnnotation = runMethod
        ?.parameters?.first { it.hasAnnotation<Load>() }
        ?.findAnnotation<Load>()
        ?: throw IllegalStateException("No @Load annotation found in")
    val inputPath = "input/$levelName/${loadAnnotation.filename}"
    return File(inputPath).readText().trim()
}

fun newLevelInstance(name: String): Level {
    val className = name.replaceFirstChar { it.uppercase() }
    val levelClass = Class.forName("levels.$className") ?: throw IllegalStateException("Class not found '$className'")
    val instance = levelClass.getDeclaredConstructor().newInstance()
    return instance as Level
}
