package fr.willban.mixolo.data.repository

import fr.willban.mixolo.data.model.Machine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object MachinesRepository {

    //TODO remoove temp mock
    private var machines = MutableStateFlow(
        listOf(
            Machine("1", "Machine 1", emptyList()),
            Machine("2", "Machine 2", emptyList()),
            Machine("3", "Machine 3", emptyList())
        )
    )

    fun get(): StateFlow<List<Machine>> {
        return machines
    }

    fun add(machine: Machine) {
        machines.value += machine
    }

    fun delete(machines: List<Machine>) {
        this.machines.value -= machines
    }
}
