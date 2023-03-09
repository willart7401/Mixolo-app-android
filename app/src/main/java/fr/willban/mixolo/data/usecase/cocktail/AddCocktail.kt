package fr.willban.mixolo.data.usecase.cocktail

import fr.willban.mixolo.data.model.Cocktail
import fr.willban.mixolo.data.repository.CocktailRepository

class AddCocktail {

    private val cocktailRepository = CocktailRepository

    fun invoke(cocktail: Cocktail) {
        cocktailRepository.addCocktails(cocktail)
    }
}
