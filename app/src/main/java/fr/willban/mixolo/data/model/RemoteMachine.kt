package fr.willban.mixolo.data.model

data class RemoteMachine(
    val id: String? = null,
    val admins: List<String>? = null,
    var containers: List<Container>? = null,
    val suggestions: List<Cocktail>? = null,
    val historic: List<Cocktail>? = null,
    val cocktail: Cocktail? = null,
    val isRunning: Boolean? = null,
    val isPurging: Boolean? = null
)
