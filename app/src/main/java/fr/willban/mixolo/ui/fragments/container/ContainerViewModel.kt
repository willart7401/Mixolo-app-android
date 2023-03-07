package fr.willban.mixolo.ui.fragments.container

import androidx.lifecycle.ViewModel
import fr.willban.mixolo.data.model.Container
import fr.willban.mixolo.data.usecase.container.EditContainer
import fr.willban.mixolo.data.usecase.container.GetContainers
import kotlinx.coroutines.flow.StateFlow

class ContainerViewModel : ViewModel() {

    private val getContainersUseCase = GetContainers()
    private val editContainerUseCase = EditContainer()

    fun getContainers(): StateFlow<List<Container>> {
        return getContainersUseCase.invoke()
    }

    fun editContainer(container: Container) {
        editContainerUseCase.invoke(container)
    }
}