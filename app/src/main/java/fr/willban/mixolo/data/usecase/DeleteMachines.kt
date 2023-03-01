package fr.willban.mixolo.data.usecase

import fr.willban.mixolo.data.model.Machine
import fr.willban.mixolo.data.repository.MachinesRepository

class DeleteMachines {

    private val machinesRepository = MachinesRepository

    fun invoke(machines: List<Machine>) {
        return machinesRepository.delete(machines)
    }
}
