package fr.willban.mixolo.data.usecase.machine

import androidx.lifecycle.LiveData
import fr.willban.mixolo.data.model.LocalMachine
import fr.willban.mixolo.data.repository.MachinesRepository
import kotlinx.coroutines.flow.StateFlow

class GetMachines {

    private val machinesRepository = MachinesRepository

    fun invoke(): LiveData<List<LocalMachine>> {
        return machinesRepository.get()
    }
}
