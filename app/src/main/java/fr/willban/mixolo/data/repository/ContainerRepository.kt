package fr.willban.mixolo.data.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import fr.willban.mixolo.FIREBASE_URL
import fr.willban.mixolo.data.model.Container

object ContainerRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance(FIREBASE_URL).reference

    fun editContainer(machineId: String, container: Container) {
        val childUpdates = mapOf<String, Any>("/machines/$machineId/containers/${container.id}" to container)
        database.updateChildren(childUpdates)
    }

    fun getContainers(machineId: String, onSuccess: (List<Container>) -> Unit) {
        database.child("machines").child(machineId).child("containers").get().addOnSuccessListener {
            val containers = it.getValue(object : GenericTypeIndicator<List<Container>>() {})

            if (containers != null) {
                onSuccess(containers)
            }
        }
    }
}
