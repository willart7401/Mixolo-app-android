package fr.willban.mixolo.data.repository

import com.google.firebase.database.*
import fr.willban.mixolo.FIREBASE_URL
import fr.willban.mixolo.data.model.Container

object ContainerRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance(FIREBASE_URL).reference

    fun editContainer(machineId: String, container: Container) {
        val childUpdates = mapOf<String, Any>("/machines/$machineId/containers/${container.id}" to container)
        database.updateChildren(childUpdates)
    }
}
