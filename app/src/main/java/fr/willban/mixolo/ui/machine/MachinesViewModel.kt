package fr.willban.mixolo.ui.machine

import androidx.lifecycle.ViewModel
import fr.willban.mixolo.data.model.Machine
import fr.willban.mixolo.data.usecase.AddMachine
import fr.willban.mixolo.data.usecase.GetMachines
import kotlinx.coroutines.flow.StateFlow

class MachinesViewModel : ViewModel() {

    private val addMachineUseCase = AddMachine()
    private val getMachinesUseCase = GetMachines()

    fun addMachine(machine: Machine){
        addMachineUseCase.invoke(machine)
    }

    fun getMachines(): StateFlow<List<Machine>> {
        return getMachinesUseCase.invoke()
    }
}
