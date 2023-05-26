package fr.willban.mixolo.data.repository

import androidx.lifecycle.LiveData
import com.google.firebase.database.*
import fr.willban.mixolo.FIREBASE_URL
import fr.willban.mixolo.data.database.MixoloDatabase
import fr.willban.mixolo.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

object MachineRepository {

    private var currentMachineId: String? = null
    private val machineDao = MixoloDatabase.getDatabase().machineDao()
    private val machineStateFlow = MutableStateFlow<RemoteMachine?>(null)
    private val database: DatabaseReference = FirebaseDatabase.getInstance(FIREBASE_URL).reference

    fun getAllLocally(): LiveData<List<LocalMachine>> {
        return machineDao.getAllMachines()
    }

    fun getRemotely(machineId: String?): Flow<RemoteMachine> {
        refreshMachineStateFlow(machineId)
        return machineStateFlow.filterNotNull()
    }

    private fun refreshMachineStateFlow(machineId: String?) {
        if (machineId != null) {
            database.child("machines").child(machineId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val machine = dataSnapshot.getValue(RemoteMachine::class.java)

                    if (machine != null) {
                        machineStateFlow.value = machine
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

            database.child("machines").child(machineId).child("containers").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val containers = dataSnapshot.getValue(object : GenericTypeIndicator<List<Container>>() {})

                    if (containers != null) {
                        machineStateFlow.value?.containers = machineStateFlow.value?.containers?.toMutableList()?.apply {
                            addAll(containers)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }

    fun saveCurrentMachineId(currentMachineId: String) {
        this.currentMachineId = currentMachineId
    }

    fun getCurrentMachineId(): String? {
        return currentMachineId
    }

    suspend fun addLocally(machine: LocalMachine) {
        machineDao.addMachine(machine)
    }

    fun addRemotely(localMachine: LocalMachine, containerNb: Int, containerCapacity: Int, adminEmail: String?) {
        database.child("machines").child(localMachine.id).get().addOnSuccessListener {
            if (it.value == null) {
                val remoteMachine = RemoteMachine(
                    id = localMachine.id,
                    admins = if (adminEmail != null) listOf(adminEmail) else emptyList(),
                    containers = createContainers(containerNb, containerCapacity),
                    suggestions = listOf(),
                    historic = listOf(),
                    cocktail = null,
                    running = false,
                    purging = false
                )

                database.child("machines").child(localMachine.id).setValue(remoteMachine)
            }
        }
    }

    suspend fun deleteLocally(machines: List<LocalMachine>) {
        machineDao.deleteMachines(machines)
    }

    suspend fun editLocally(machine: LocalMachine) {
        machineDao.updateMachines(machine)
    }

    private fun createContainers(containerNb: Int, containerCapacity: Int): List<Container> {
        val list = mutableListOf<Container>()

        for (id in 0 until containerNb) {
            list.add(Container(id, "", false,  containerCapacity, 0))
        }

        return list
    }
}
