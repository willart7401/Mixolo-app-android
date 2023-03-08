package fr.willban.mixolo.data.usecase.machine

import fr.willban.mixolo.data.model.LocalMachine
import fr.willban.mixolo.data.repository.MachineRepository

class EditMachine {

    private val machinesRepository = MachineRepository

    suspend fun invoke(machine: LocalMachine) {
        return machinesRepository.editLocally(machine)
    }
}
