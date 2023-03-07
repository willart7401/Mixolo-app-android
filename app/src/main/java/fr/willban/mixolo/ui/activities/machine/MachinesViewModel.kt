package fr.willban.mixolo.ui.activities.machine

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.willban.mixolo.data.model.LocalMachine
import fr.willban.mixolo.data.usecase.AddMachine
import fr.willban.mixolo.data.usecase.DeleteMachines
import fr.willban.mixolo.data.usecase.EditMachine
import fr.willban.mixolo.data.usecase.GetMachines
import kotlinx.coroutines.launch

class MachinesViewModel : ViewModel() {

    private val addMachineUseCase = AddMachine()
    private val getMachinesUseCase = GetMachines()
    private val editMachinesUseCase = EditMachine()
    private val deleteMachinesUseCase = DeleteMachines()

    fun addMachine(machine: LocalMachine) {
        viewModelScope.launch {
            addMachineUseCase.invoke(machine)
        }
    }

    fun editMachine(machine: LocalMachine) {
        viewModelScope.launch {
            editMachinesUseCase.invoke(machine)
        }
    }

    fun getMachines(): LiveData<List<LocalMachine>> {
        return getMachinesUseCase.invoke()
    }

    fun delete(selectedMachines: List<LocalMachine>) {
        viewModelScope.launch {
            deleteMachinesUseCase.invoke(selectedMachines)
        }
    }
}
