package fr.willban.mixolo.ui.activities.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import fr.willban.mixolo.FIREBASE_URL
import fr.willban.mixolo.data.model.LocalMachine
import fr.willban.mixolo.data.usecase.machine.*
import fr.willban.mixolo.data.usecase.user.GetUserConnected
import kotlinx.coroutines.launch

class MachinesViewModel : ViewModel() {

    private val addMachineUseCase = AddMachine()
    private val getMachineUseCase = GetMachine()
    private val editMachinesUseCaseLocally = EditMachine()
    private val deleteMachinesUseCase = DeleteMachines()
    private val getUserConnectedUseCase = GetUserConnected()
    private val saveCurrentMachineUseCase = SaveCurrentMachine()

    private var database: DatabaseReference = FirebaseDatabase.getInstance(FIREBASE_URL).reference

    fun addMachine(localMachine: LocalMachine, containerNb: Int, containerCapacity: Int) {
        val adminEmail = getUserConnectedUseCase.invoke()?.email

        addMachineUseCase.invokeRemotely(localMachine, containerNb, containerCapacity, adminEmail)

        viewModelScope.launch {
            addMachineUseCase.invokeLocally(localMachine)
        }
    }

    fun editMachine(machine: LocalMachine) {
        viewModelScope.launch {
            editMachinesUseCaseLocally.invoke(machine)
        }
    }

    fun getMachines(): LiveData<List<LocalMachine>> {
        return getMachineUseCase.invokeLocally()
    }

    fun delete(selectedMachines: List<LocalMachine>) {
        viewModelScope.launch {
            deleteMachinesUseCase.invoke(selectedMachines)
        }
    }

    fun saveSelectedMachine(id: String) {
        saveCurrentMachineUseCase.invoke(id)
    }

    fun isUserConnectedIsAdmin(): Boolean {
        return getUserConnectedUseCase.invoke()?.email == "william.germain74@gmail.com"
    }
}