package fr.willban.mixolo.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.willban.mixolo.data.database.dao.MachineDao
import fr.willban.mixolo.data.model.LocalMachine

@Database(entities = [LocalMachine::class], version = 1, exportSchema = true)
abstract class MixoloDatabase : RoomDatabase() {

    abstract fun machineDao(): MachineDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: MixoloDatabase

        fun init(context: Context) {
            synchronized(this) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, MixoloDatabase::class.java, "database").build()
            }
        }

        fun getDatabase(): MixoloDatabase {
            return INSTANCE
        }
    }
}
