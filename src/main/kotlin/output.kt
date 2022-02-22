import java.io.File

fun writeToFile(fileName: String, pizza: Pizza) {
    val resultsFolder = File("results").apply { mkdir() }
    val outputFile = File(resultsFolder, fileName.replace("in", "out")).apply {
        createNewFile()
    }
    outputFile.writeText("${pizza.ingredients.size} ${pizza.ingredients.joinToString(" ")}")
}
