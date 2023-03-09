package fr.willban.mixolo.data.usecase.container

import fr.willban.mixolo.data.model.Container
import fr.willban.mixolo.data.repository.ContainerRepository

class GetContainers {

    private val containerRepository = ContainerRepository

    fun invoke(machineId: String, onSuccess: (List<Container>) -> Unit) {
        containerRepository.getContainers(machineId, onSuccess)
    }
}
