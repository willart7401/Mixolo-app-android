package fr.willban.mixolo.data.usecase.machine

import fr.willban.mixolo.data.model.LocalMachine
import fr.willban.mixolo.data.repository.MachinesRepository

class EditMachine {

    private val machinesRepository = MachinesRepository

    suspend fun invoke(machine: LocalMachine) {
        return machinesRepository.edit(machine)
    }
}
