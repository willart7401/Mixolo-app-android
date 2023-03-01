package fr.willban.mixolo.data.usecase

import fr.willban.mixolo.data.model.Machine
import fr.willban.mixolo.data.repository.MachinesRepository
import kotlinx.coroutines.flow.StateFlow

class GetMachines {

    private val machinesRepository = MachinesRepository

    fun invoke(): StateFlow<List<Machine>> {
        return machinesRepository.get()
    }
}
