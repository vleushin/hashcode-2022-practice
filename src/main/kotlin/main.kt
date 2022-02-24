import java.io.File

fun main(args: Array<String>) {
    args.forEach { filePath ->
        val inputFile = File(filePath)
        val clients = readClients(inputFile)
        println("Clients: $clients")

        val ingredients = mutableSetOf<String>()
        val ingredientLikes = mutableMapOf<String, Int>()
        val ingredientDislikes = mutableMapOf<String, Int>()
        clients.forEach { ingredients.addAll(it.likes + it.dislikes)}
        ingredients.forEach { ingredient ->
            ingredientLikes[ingredient] = clients.filter { client -> client.likes.contains(ingredient) }.size
            ingredientDislikes[ingredient] = clients.filter { client -> client.dislikes.contains(ingredient) }.size
        }
        println("Likes: $ingredientLikes")
        println("Dislikes: $ingredientDislikes")

        // solution 1 -- include all liked and exclude all disliked
        val selectedIngredients1 = ingredientLikes.filter { it.value > 0 }.keys - ingredientDislikes.filter { it.value > 0 }.keys

        // solution 2 -- include only if more likes than dislikes
        val selectedIngredients = ingredients.filter {
            ingredientLikes[it]!! - ingredientDislikes[it]!! > 0 // or >=
        }.toSet()

        // solution 3 -- OR-tools
        // val solution = findSolution(clients, ingredients.toList());

        val score = clients.filter {
            selectedIngredients.containsAll(it.likes)
                    && !it.dislikes.any { ingredient -> selectedIngredients.contains(ingredient) }
        }.size

        println("Selected ingredients: $selectedIngredients")
        println("Score: $score (max: ${clients.size})")

        writeToFile(inputFile.name, Pizza(selectedIngredients))
    }
}
