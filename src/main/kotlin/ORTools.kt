import com.google.ortools.Loader
import kotlin.jvm.JvmStatic
import com.google.ortools.linearsolver.MPSolver
import com.google.ortools.linearsolver.MPSolver.ResultStatus
import com.google.ortools.linearsolver.MPVariable

fun findSolution(client: List<Client>, ingredients: List<String>) {
    Loader.loadNativeLibraries()
    val solver = MPSolver.createSolver("SCIP")
    if (solver == null) {
        println("Could not create solver SCIP")
        return
    }

    val infinity = Double.POSITIVE_INFINITY
    val ingredientVars = List(ingredients.size) { i ->
        solver.makeNumVar(0.0, 1.0, "${ingredients[i]}")
    }
    println("Number of variables = ${solver.numVariables()}")

    client.forEachIndexed { i, client ->
        val constraint = solver.makeConstraint(client.likes.size.toDouble(), infinity, "client${i}")
        ingredientVars.forEach { ingredientVar ->
            val ingredient = ingredientVar.name()
            if (client.likes.contains(ingredient)) {
                constraint.setCoefficient(ingredientVar, 1.5)
            } else if (client.dislikes.contains(ingredient)) {
                constraint.setCoefficient(ingredientVar, -0.5)
            } else {
                constraint.setCoefficient(ingredientVar, 0.0)
            }
        }
    }
    println("Number of constraints = " + solver.numConstraints())

    // [START objective]
    val objective = solver.objective()
    ingredientVars.forEach {
        objective.setCoefficient(it, 1.0)
    }
    objective.setMaximization()

    val resultStatus = solver.solve()
    solver.constraints()
    // [START print_solution]
    if (resultStatus == ResultStatus.OPTIMAL) {
        println("Solution:")
        println("Objective value = " + objective.value())
        ingredientVars.forEach {
            println("${it.name()} = ${it.solutionValue()}")
        }
    } else {
        System.err.println("The problem does not have an optimal solution!")
    }
    // [END print_solution]

    // [START advanced]
    println("\nAdvanced usage:")
    println("Problem solved in " + solver.wallTime() + " milliseconds")
    println("Problem solved in " + solver.iterations() + " iterations")
    println("Problem solved in " + solver.nodes() + " branch-and-bound nodes")
    // [END advanced]
}
