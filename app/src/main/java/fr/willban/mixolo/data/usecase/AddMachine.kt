package fr.willban.mixolo.data.usecase

import fr.willban.mixolo.data.model.LocalMachine
import fr.willban.mixolo.data.repository.MachinesRepository

class AddMachine {

    private val machinesRepository = MachinesRepository

    suspend fun invoke(machine: LocalMachine) {
        return machinesRepository.add(machine)
    }
}
