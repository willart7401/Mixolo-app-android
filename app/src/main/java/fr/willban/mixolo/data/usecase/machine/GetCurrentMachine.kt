package fr.willban.mixolo.data.usecase.machine

import fr.willban.mixolo.data.repository.MachineRepository

class GetCurrentMachine {

    private val machinesRepository = MachineRepository

    fun invoke(): String? {
        return machinesRepository.getCurrentMachineId()
    }
}
