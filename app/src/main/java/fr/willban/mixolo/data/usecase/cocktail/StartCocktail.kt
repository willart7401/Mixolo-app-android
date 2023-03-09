package fr.willban.mixolo.data.usecase.cocktail

import fr.willban.mixolo.data.model.Cocktail
import fr.willban.mixolo.data.repository.CocktailRepository

class StartCocktail {

    private val cocktailRepository = CocktailRepository

    fun invoke(cocktail: Cocktail, machineId: String, onResult: (String) -> Unit) {
        cocktailRepository.startCocktail(cocktail, machineId, onResult)
    }
}
