package fr.willban.mixolo.data.model

data class Cocktail(
    val id: Int,
    val name: String,
    val ingredients: List<Ingredient>
)
