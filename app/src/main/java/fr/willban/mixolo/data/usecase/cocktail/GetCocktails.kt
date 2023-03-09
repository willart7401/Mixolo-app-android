package fr.willban.mixolo.data.usecase.cocktail

import fr.willban.mixolo.data.model.Cocktail
import fr.willban.mixolo.data.repository.CocktailRepository
import kotlinx.coroutines.flow.StateFlow

class GetCocktails {

    private val cocktailRepository = CocktailRepository

    fun invoke(machineId: String): StateFlow<List<Cocktail>> {
        return cocktailRepository.getCocktails(machineId)
    }
}
