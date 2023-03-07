package fr.willban.mixolo.data.repository

import fr.willban.mixolo.data.model.Container
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

object ContainersRepository {

    //TODO remove temp mock
    private var containers = MutableStateFlow(
        mutableListOf(
            Container(1, "Vodka", 75, 65),
            Container(2, "Jus d'orange", 75, 31),
            Container(3, "Jus d'ananas", 75, 3)
        )
    )

    fun get(): StateFlow<List<Container>> {
        return containers
    }

    fun add(container: Container) {
        containers.value += container
    }

    fun delete(container: List<Container>) {
        this.containers.value -= container.toSet()
    }

    fun edit(container: Container) {
        val list = this.containers.value.toMutableList()
        this.containers.value.clear()
        list.indexOfFirst { it.id == container.id }.takeIf { it != -1 }?.let { index ->
            list[index] = list[index].copy(name = container.name)
        }
        this.containers.value = list.toMutableList()
    }
}
