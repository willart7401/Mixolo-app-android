package fr.willban.mixolo.data.usecase

import fr.willban.mixolo.data.model.Machine
import fr.willban.mixolo.data.repository.MachinesRepository

class AddMachine {

    private val machinesRepository = MachinesRepository

    fun invoke(machine: Machine) {
        return machinesRepository.add(machine)
    }
}
