package fr.willban.mixolo.ui.fragments.capacity

import androidx.lifecycle.ViewModel
import fr.willban.mixolo.data.model.Container
import fr.willban.mixolo.data.usecase.AddContainer
import fr.willban.mixolo.data.usecase.EditContainer
import fr.willban.mixolo.data.usecase.GetContainers
import kotlinx.coroutines.flow.StateFlow

class CapacityViewModel : ViewModel() {

    private val getContainersUseCase = GetContainers()
    private val addContainerUseCase = AddContainer()
    private val editContainerUseCase = EditContainer()

    fun getContainers(): StateFlow<List<Container>> {
        return getContainersUseCase.invoke()
    }

    fun addContainer(container: Container) {
        return addContainerUseCase.invoke(container)
    }

    fun editContainer(container: Container) {
        editContainerUseCase.invoke(container)
    }
}