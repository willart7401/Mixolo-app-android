package fr.willban.mixolo.data.model

data class RemoteMachine(
    val id: String? = null,
    val admins: List<String>? = null,
    var containers: List<Container>? = null,
    val suggestions: List<Cocktail>? = null,
    val historic: List<Cocktail>? = null,
    var cocktail: Cocktail? = null,
    val running: Boolean? = null,
    val purging: Boolean? = null
)
