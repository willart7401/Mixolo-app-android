package fr.willban.mixolo.ui.fragments.cocktail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import fr.willban.mixolo.R
import fr.willban.mixolo.data.model.Container
import fr.willban.mixolo.util.toInt

data class TmpIngredient(
    var name: String,
    var amount: Int
)

class ItemAdapter(private val context: Context, private val onFormChanged: (Boolean) -> Unit) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private var ingredients: List<String> = emptyList()
    private var containers: List<Container> = emptyList()
    var tmpIngredients = HashMap<Int, TmpIngredient>()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_dialog_edit_cocktail, parent, false))
    }

    override fun getItemCount(): Int = containers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ingredientName.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, ingredients)

        holder.ingredientAmount.doOnTextChanged { text, _, _, _ ->
            text.toString().toInt().also { amount ->
                saveTmpIngredient(position = position, amount = amount)
            }
        }

        holder.ingredientName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                saveTmpIngredient(position = holder.adapterPosition, name = ingredients[pos])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    fun refresh(containers: List<Container> = emptyList(), ingredients: List<String> = emptyList()) {
        this.containers = containers
        this.ingredients = ingredients
        notifyDataSetChanged()
    }

    private fun verifyFormIsComplete(): Boolean {
        var isFormComplete = true

        for (ingredient in tmpIngredients.values) {
            if (ingredient.name == "Ingredient" || ingredient.name.isEmpty() || ingredient.amount == 0) {
                isFormComplete = false
            }
        }

        return isFormComplete && tmpIngredients.size == containers.size
    }

    private fun saveTmpIngredient(position: Int, name: String = "", amount: Int = 0) {
        when {
            tmpIngredients[position] != null && name.isEmpty() -> {
                tmpIngredients[position]!!.amount = amount
            }
            tmpIngredients[position] != null && amount == 0 -> {
                tmpIngredients[position]!!.name = name
            }
            else -> tmpIngredients[position] = TmpIngredient(name, amount)
        }
        onFormChanged(verifyFormIsComplete())
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val ingredientName: Spinner = itemView.findViewById(R.id.dec_ingredient_name_1)
        val ingredientAmount: EditText = itemView.findViewById(R.id.dec_ingredient_amount_1)
    }
}