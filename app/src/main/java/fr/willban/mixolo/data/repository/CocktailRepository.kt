package fr.willban.mixolo.data.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import fr.willban.mixolo.FIREBASE_URL
import fr.willban.mixolo.data.model.Cocktail
import fr.willban.mixolo.data.model.Ingredient
import fr.willban.mixolo.data.model.RemoteMachine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object CocktailRepository {

    private val cocktails = MutableStateFlow<List<Cocktail>>(emptyList())
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

    fun addCocktails(cocktail: Cocktail) {
        cocktails.value += listOf(cocktail)
    }

    fun getCocktails(machineId: String): StateFlow<List<Cocktail>> {
        refreshCocktailsForMachine(machineId)
        return cocktails
    }

    //TODO Modify temp mock
    private fun refreshCocktailsForMachine(machineId: String) {
        cocktails.value = listOf(
            Cocktail(
                1, "Sex on the beach", listOf(
                    Ingredient(1, "Voodka", 5),
                    Ingredient(2, "Jus d'ananas", 5),
                    Ingredient(3, "Jus de cramberry", 15)
                )
            ),
            Cocktail(
                2, "Punch", listOf(
                    Ingredient(1, "Voodka", 5),
                    Ingredient(2, "Jus d'ananas", 10),
                    Ingredient(3, "Jus de cramberry", 10)
                )
            )
        )
    }
}
