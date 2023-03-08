package fr.willban.mixolo.data.usecase.machine

import fr.willban.mixolo.data.repository.MachineRepository

class SaveCurrentMachine {

    private val machinesRepository = MachineRepository

    fun invoke(machineId: String) {
        machinesRepository.saveCurrentMachineId(machineId)
    }
}
