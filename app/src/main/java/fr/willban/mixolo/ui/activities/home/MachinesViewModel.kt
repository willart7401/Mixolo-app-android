package fr.willban.mixolo.ui.activities.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import fr.willban.mixolo.FIREBASE_URL
import fr.willban.mixolo.data.model.Container
import fr.willban.mixolo.data.model.LocalMachine
import fr.willban.mixolo.data.model.RemoteMachine
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

        database.child("machines").child(localMachine.id).get().addOnSuccessListener {
            if (it.value == null) {
                val remoteMachine = RemoteMachine(
                    id = localMachine.id,
                    admins = if (adminEmail != null) listOf(adminEmail) else emptyList(),
                    containers = createContainers(containerNb, containerCapacity),
                    suggestions = emptyList(),
                    historic = emptyList(),
                    cocktail = null,
                    isRunning = false,
                    isPurging = false
                )

                database.child("machines").child(localMachine.id).setValue(remoteMachine)
            }
        }

        viewModelScope.launch {
            addMachineUseCase.invoke(localMachine)
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

    private fun createContainers(containerNb: Int, containerCapacity: Int): List<Container> {
        val list = mutableListOf<Container>()

        for (id in 0 until containerNb) {
            list.add(Container(id, "", containerCapacity, 0))
        }

        return list
    }

    fun saveSelectedMachine(id: String) {
        saveCurrentMachineUseCase.invoke(id)
    }
}