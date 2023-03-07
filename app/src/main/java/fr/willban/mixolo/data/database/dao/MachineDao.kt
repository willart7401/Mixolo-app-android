package fr.willban.mixolo.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fr.willban.mixolo.data.model.LocalMachine

@Dao
interface MachineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMachine(machine: LocalMachine)

    @Query("SELECT * FROM Machine")
    fun getAllMachines(): LiveData<List<LocalMachine>>

    @Query("SELECT * FROM Machine WHERE id LIKE :machineId")
    fun getMachineById(machineId: String): LiveData<LocalMachine?>

    @Update
    suspend fun updateMachines(machines: LocalMachine)

    @Delete
    suspend fun deleteMachines(machine: List<LocalMachine>)
}
