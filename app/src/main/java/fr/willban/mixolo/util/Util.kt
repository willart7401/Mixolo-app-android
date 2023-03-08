package fr.willban.mixolo.util

import android.content.Context
import android.content.res.Resources
import android.widget.Toast
import fr.willban.mixolo.data.model.Container
import fr.willban.mixolo.data.model.Ingredient

val Int.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun List<Ingredient>.prettyPrint(): String {
    return this.joinToString(", ") { it.name.lowercase() }.replaceFirstChar(Char::titlecase)
}

fun String.findParameterValue(parameterName: String): String? {
    return this.split('&').map {
        val parts = it.split('=')
        val name = parts.firstOrNull() ?: ""
        val value = parts.drop(1).firstOrNull() ?: ""
        Pair(name, value)
    }.firstOrNull { it.first == parameterName }?.second
}

fun Context.showShortToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun String.tryToInt(onSuccess: (Int) -> Unit) {
    this.takeIf { it.isNotEmpty() }?.let {
        try {
            val number = Integer.parseInt(this)
            onSuccess(number)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}

fun String.toInt(): Int {
    return if (this.isNotEmpty()) {
        try {
            Integer.parseInt(this)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            0
        }
    } else 0
}