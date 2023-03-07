package fr.willban.mixolo.data.usecase.machine

import fr.willban.mixolo.data.model.LocalMachine
import fr.willban.mixolo.data.repository.MachinesRepository

class DeleteMachines {

    private val machinesRepository = MachinesRepository

    suspend fun invoke(machines: List<LocalMachine>) {
        return machinesRepository.delete(machines)
    }
}
