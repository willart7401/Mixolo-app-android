package fr.willban.mixolo.util

import fr.willban.mixolo.data.model.Cocktail
import fr.willban.mixolo.data.model.Ingredient

object CocktailList {

    private final val cocktails = listOf(
        Cocktail(
            1, "Sex on the beach", listOf(
                Ingredient(1, "Vodka", 5),
                Ingredient(2, "Jus d'ananas", 5),
                Ingredient(3, "Jus de cramberry", 15)
            )
        ),
    )

    fun defaultCocktailList(){
        //return cocktails
    }
}