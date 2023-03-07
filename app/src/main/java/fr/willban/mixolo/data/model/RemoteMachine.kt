package fr.willban.mixolo.data.model

data class RemoteMachine(
    val id: String,
    val name: String,
    val admins: List<String>,
    val containers: List<Container>,
)
