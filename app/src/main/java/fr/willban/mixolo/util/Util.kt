package fr.willban.mixolo.util

import android.content.res.Resources
import fr.willban.mixolo.data.model.Ingredient

val Int.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun List<Ingredient>.prettyPrint():String {
    return this.joinToString(", ") { it.name.lowercase() }.replaceFirstChar(Char::titlecase)
}