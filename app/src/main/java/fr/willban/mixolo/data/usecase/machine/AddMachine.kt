package fr.willban.mixolo.data.usecase.machine

import fr.willban.mixolo.data.model.LocalMachine
import fr.willban.mixolo.data.repository.MachineRepository

class AddMachine {

    private val machinesRepository = MachineRepository

    suspend fun invokeLocally(machine: LocalMachine) {
        return machinesRepository.addLocally(machine)
    }

    fun invokeRemotely(machine: LocalMachine, containerNb: Int, containerCapacity: Int, adminEmail: String?) {
        return machinesRepository.addRemotely(machine, containerNb, containerCapacity, adminEmail)
    }
}
