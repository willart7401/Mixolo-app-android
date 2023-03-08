package fr.willban.mixolo.data.usecase.container

import fr.willban.mixolo.data.model.Container
import fr.willban.mixolo.data.repository.ContainerRepository

class EditContainer {

    private val containerRepository = ContainerRepository

    fun invoke(machineId: String, container: Container) {
        containerRepository.editContainer(machineId, container)
    }
}
