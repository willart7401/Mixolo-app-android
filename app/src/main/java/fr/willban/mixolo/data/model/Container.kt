package fr.willban.mixolo.data.model

data class Container(
    val id: Int? = null,
    var name: String? = null,
    var purging: Boolean = false,
    val totalAmount: Int? = null,
    val remainingAmount: Int? = null
)
