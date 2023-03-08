package fr.willban.mixolo.data.usecase.machine

import fr.willban.mixolo.data.model.LocalMachine
import fr.willban.mixolo.data.repository.MachineRepository

class DeleteMachines {

    private val machinesRepository = MachineRepository

    suspend fun invoke(machines: List<LocalMachine>) {
        return machinesRepository.deleteLocally(machines)
    }
}
