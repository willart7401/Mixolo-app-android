package fr.willban.mixolo.ui.fragments.cocktail

import androidx.lifecycle.ViewModel
import fr.willban.mixolo.data.model.Cocktail
import fr.willban.mixolo.data.model.Container
import fr.willban.mixolo.data.usecase.cocktail.AddCocktail
import fr.willban.mixolo.data.usecase.cocktail.DeleteCocktail
import fr.willban.mixolo.data.usecase.cocktail.GetCocktails
import fr.willban.mixolo.data.usecase.cocktail.StartCocktail
import fr.willban.mixolo.data.usecase.container.GetContainers
import fr.willban.mixolo.data.usecase.machine.GetCurrentMachine
import kotlinx.coroutines.flow.StateFlow

class CocktailViewModel : ViewModel() {

    private val startCocktailUseCase = StartCocktail()
    private val getCocktailsUseCase = GetCocktails()
    private val addCocktailUseCase = AddCocktail()
    private val deleteCocktailUseCase = DeleteCocktail()
    private val getCurrentMachinesUseCase = GetCurrentMachine()
    private val getContainersUseCase = GetContainers()

    fun getCocktails(): StateFlow<List<Cocktail>>? {
        val machineId = getCurrentMachinesUseCase.invoke()
        return machineId?.let { getCocktailsUseCase.invoke(it) }
    }

    fun startCocktail(cocktail: Cocktail, onResult: (String) -> Unit) {
        getCurrentMachinesUseCase.invoke()?.let { machineId ->
            startCocktailUseCase.invoke(cocktail, machineId, onResult)
        }
    }

    fun addCocktail(cocktail: Cocktail) {
        val machineId = getCurrentMachinesUseCase.invoke()
        machineId?.let { addCocktailUseCase.invoke(it, cocktail) }
    }

    fun getContainersAndIngredients(onSuccess: (List<Container>, List<String>) -> Unit) {
        getCurrentMachinesUseCase.invoke()?.let { machineId ->
            getContainersUseCase.invoke(machineId) { containers ->
                val ingredients = listOf("Ingredient") + containers.map { it.name ?: "" }.filter { it.isNotEmpty() }.distinct()
                onSuccess(containers, ingredients)
            }
        }
    }

    fun delete(cocktail: Cocktail) {
        val machineId = getCurrentMachinesUseCase.invoke()
        machineId?.let { deleteCocktailUseCase.invoke(it, cocktail) }
    }
}