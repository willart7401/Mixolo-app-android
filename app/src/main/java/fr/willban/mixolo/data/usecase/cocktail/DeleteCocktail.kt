package fr.willban.mixolo.data.usecase.cocktail

import fr.willban.mixolo.data.model.Cocktail
import fr.willban.mixolo.data.repository.CocktailRepository

class DeleteCocktail {

    private val cocktailRepository = CocktailRepository

    fun invoke(machineId: String, cocktail: Cocktail) {
        cocktailRepository.delete(machineId, cocktail)
    }
}
