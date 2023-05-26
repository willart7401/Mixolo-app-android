package fr.willban.mixolo.ui.fragments.container

import androidx.lifecycle.ViewModel
import fr.willban.mixolo.data.model.Container
import fr.willban.mixolo.data.model.RemoteMachine
import fr.willban.mixolo.data.usecase.container.EditContainer
import fr.willban.mixolo.data.usecase.machine.GetCurrentMachine
import fr.willban.mixolo.data.usecase.machine.GetMachine
import kotlinx.coroutines.flow.Flow

class ContainerViewModel : ViewModel() {

    private val getMachineUseCase = GetMachine()
    private val editContainerUseCase = EditContainer()
    private val getCurrentMachinesUseCase = GetCurrentMachine()

    fun getContainers(): Flow<RemoteMachine> {
        val machineId = getCurrentMachinesUseCase.invoke()
        return getMachineUseCase.invokeRemotely(machineId)
    }

    fun editContainer(container: Container) {
        getCurrentMachinesUseCase.invoke()?.let { machineId ->
            editContainerUseCase.invoke(machineId, container)
        }
    }

    fun purgeContainer(container: Container) {
        getCurrentMachinesUseCase.invoke()?.let { machineId ->
            editContainerUseCase.purge(machineId, container)
        }
    }
}