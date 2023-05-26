package fr.willban.mixolo.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import fr.willban.mixolo.FIREBASE_URL
import fr.willban.mixolo.data.model.Cocktail
import fr.willban.mixolo.data.model.RemoteMachine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object CocktailRepository {

    private val cocktailList = MutableStateFlow<List<Cocktail>>(emptyList())
    private val databaseMachines: DatabaseReference = FirebaseDatabase.getInstance(FIREBASE_URL).reference.child("machines")

    fun startCocktail(cocktail: Cocktail, machineId: String, onResult: (String) -> Unit) {
        databaseMachines.child(machineId).child("cocktail").get().addOnSuccessListener {
            if (it.value == null) {
                databaseMachines.child(machineId).get().addOnSuccessListener { result ->
                    val machine = result.getValue(RemoteMachine::class.java)

                    if (machine != null) {
                        machine.cocktail = cocktail
                        databaseMachines.child(machineId).setValue(machine)
                        onResult("Cocktail lancé !")
                    } else {
                        onResult("Une erreur est survenur")
                    }
                }
            } else {
                onResult("Un cocktail est déja lancé !")
            }
        }
    }

    fun addCocktails(machineId: String, cocktail: Cocktail) {
        val list = cocktailList.value + cocktail
        databaseMachines.child(machineId).child("suggestions").setValue(list)
        cocktailList.value += cocktail
    }

    fun getCocktails(machineId: String): StateFlow<List<Cocktail>> {
        databaseMachines.child(machineId).child("suggestions").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(result: DataSnapshot) {
                val cocktails = result.getValue(object : GenericTypeIndicator<List<Cocktail>>() {})

                if (cocktails != null) {
                    cocktailList.value = cocktails
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        return cocktailList
    }

    fun delete(machineId: String, cocktail: Cocktail) {
        val list = cocktailList.value - cocktail
        databaseMachines.child(machineId).child("suggestions").setValue(list)
        cocktailList.value -= cocktail
    }
}
