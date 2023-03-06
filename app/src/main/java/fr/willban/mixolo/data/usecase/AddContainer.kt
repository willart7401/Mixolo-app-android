package fr.willban.mixolo.data.usecase

import fr.willban.mixolo.data.model.Container
import fr.willban.mixolo.data.repository.ContainersRepository

class AddContainer {

    private val containersRepository = ContainersRepository

    fun invoke(container: Container) {
        return containersRepository.add(container)
    }
}
