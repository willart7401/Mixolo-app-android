package fr.willban.mixolo.data.usecase.machine

import androidx.lifecycle.LiveData
import fr.willban.mixolo.data.model.LocalMachine
import fr.willban.mixolo.data.model.RemoteMachine
import fr.willban.mixolo.data.repository.MachineRepository
import kotlinx.coroutines.flow.Flow

class GetMachine {

    private val machinesRepository = MachineRepository

    fun invokeLocally(): LiveData<List<LocalMachine>> {
        return machinesRepository.getAllLocally()
    }

    fun invokeRemotely(machineId: String?): Flow<RemoteMachine> {
        return machinesRepository.getRemotely(machineId)
    }
}
