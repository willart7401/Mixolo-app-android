package fr.willban.mixolo.data.repository

import androidx.lifecycle.LiveData
import fr.willban.mixolo.data.database.MixoloDatabase
import fr.willban.mixolo.data.model.LocalMachine

object MachinesRepository {

    private val machineDao = MixoloDatabase.getDatabase().machineDao()

    fun get(): LiveData<List<LocalMachine>> {
        return machineDao.getAllMachines()
    }

    suspend fun add(machine: LocalMachine) {
        machineDao.addMachine(machine)
    }

    suspend fun delete(machines: List<LocalMachine>) {
        machineDao.deleteMachines(machines)
    }

    suspend fun edit(machine: LocalMachine) {
        machineDao.updateMachines(machine)
    }
}
