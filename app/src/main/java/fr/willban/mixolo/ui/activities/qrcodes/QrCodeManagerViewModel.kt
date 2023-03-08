package fr.willban.mixolo.ui.activities.qrcodes

import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import fr.willban.mixolo.FIREBASE_URL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import java.text.FieldPosition

class QrCodeManagerViewModel : ViewModel() {

    private var qrcodes = MutableStateFlow<List<String>?>(null)
    private var database: DatabaseReference = FirebaseDatabase.getInstance(FIREBASE_URL).reference

    fun getQrCodes(): Flow<List<String>> {
        database.child("qrcodes").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                qrcodes.value = snapshot.getValue(object : GenericTypeIndicator<List<String>>() {})
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        return qrcodes.filterNotNull()
    }

    fun addQrCode(url: String) {
        database.child("qrcodes").setValue((qrcodes.value ?: emptyList()) + listOf(url))
    }

    fun removeQrCode(pos: Int) {
        qrcodes.value?.let {
            val list = it.toMutableList()
            list.removeAt(pos)
            database.child("qrcodes").setValue(list.toList())
            if (list.isEmpty()) qrcodes.value = emptyList()
        }
    }
}