package fr.willban.mixolo.data.model

import java.util.Date

data class Historic(
    val id: Int,
    val cocktail: Cocktail,
    val date: Date
)
