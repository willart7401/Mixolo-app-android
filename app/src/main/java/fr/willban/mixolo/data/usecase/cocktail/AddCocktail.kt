package fr.willban.mixolo.data.usecase.cocktail

import fr.willban.mixolo.data.model.Cocktail
import fr.willban.mixolo.data.repository.CocktailRepository

class AddCocktail {

    private val cocktailRepository = CocktailRepository

    fun invoke(machineId: String, cocktail: Cocktail) {
        cocktailRepository.addCocktails(machineId, cocktail)
    }
}
