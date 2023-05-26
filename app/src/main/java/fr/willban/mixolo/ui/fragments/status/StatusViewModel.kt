package fr.willban.mixolo.ui.fragments.status

import androidx.lifecycle.ViewModel
import fr.willban.mixolo.data.model.RemoteMachine
import fr.willban.mixolo.data.usecase.machine.GetCurrentMachine
import fr.willban.mixolo.data.usecase.machine.GetMachine
import kotlinx.coroutines.flow.Flow

class StatusViewModel : ViewModel() {

    private val getMachineUseCase = GetMachine()
    private val getCurrentMachinesUseCase = GetCurrentMachine()

    fun getStatus(): Flow<RemoteMachine> {
        val machineId = getCurrentMachinesUseCase.invoke()
        return getMachineUseCase.invokeRemotely(machineId)
    }
}