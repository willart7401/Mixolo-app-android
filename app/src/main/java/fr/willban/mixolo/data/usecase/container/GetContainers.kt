package fr.willban.mixolo.data.usecase.container

import fr.willban.mixolo.data.model.Container
import fr.willban.mixolo.data.repository.ContainersRepository
import kotlinx.coroutines.flow.StateFlow

class GetContainers {

    private val containersRepository = ContainersRepository

    fun invoke(): StateFlow<List<Container>> {
        return containersRepository.get()
    }
}
