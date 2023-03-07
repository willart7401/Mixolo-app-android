package fr.willban.mixolo.ui.fragments.cocktail

import androidx.lifecycle.ViewModel
import fr.willban.mixolo.data.model.Cocktail
import fr.willban.mixolo.data.model.Ingredient

class CocktailViewModel : ViewModel() {

    //TODO replace by API Cocktails
    fun getCocktails(): List<Cocktail> {
        return listOf(
            Cocktail(1, "Sex on the beach", listOf(
                Ingredient(1, "Voodka", 5),
                Ingredient(2, "Jus d'ananas", 25),
                Ingredient(3, "Jus de cramberry", 35)
            )),
            Cocktail(2, "Sex on the beach", listOf(
                Ingredient(1, "Voodka", 5),
                Ingredient(2, "Jus d'ananas", 25),
                Ingredient(3, "Jus de cramberry", 35)
            )),
            Cocktail(3, "Sex on the beach", listOf(
                Ingredient(1, "Voodka", 5),
                Ingredient(2, "Jus d'ananas", 25),
                Ingredient(3, "Jus de cramberry", 35)
            )),
            Cocktail(4, "Sex on the beach", listOf(
                Ingredient(1, "Voodka", 5),
                Ingredient(2, "Jus d'ananas", 25),
                Ingredient(3, "Jus de cramberry", 35)
            )),
            Cocktail(5, "Sex on the beach", listOf(
                Ingredient(1, "Voodka", 5),
                Ingredient(2, "Jus d'ananas", 25),
                Ingredient(3, "Jus de cramberry", 35)
            ))
        )
    }
}