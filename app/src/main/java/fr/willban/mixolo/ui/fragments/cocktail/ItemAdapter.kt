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
            text.toString().takeIf { it.isNotEmpty() }?.let {
                try {
                    val amount = Integer.parseInt(text.toString())
                    if (tmpIngredients[holder.adapterPosition] != null) {
                        tmpIngredients[holder.adapterPosition]!!.amount = amount
                    } else {
                        tmpIngredients[holder.adapterPosition] = TmpIngredient("", amount)
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
                onFormChanged(verifyFormIsComplete())
            }
        }

        holder.ingredientName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                if (tmpIngredients[holder.adapterPosition] != null) {
                    tmpIngredients[holder.adapterPosition]!!.name = ingredients[pos]
                } else {
                    tmpIngredients[holder.adapterPosition] = TmpIngredient(ingredients[pos], 0)
                }
                onFormChanged(verifyFormIsComplete())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    fun refresh(containers: List<Container> = emptyList(), ingredients: List<String> = emptyList()) {
        this.containers = containers
        this.ingredients = ingredients
        notifyDataSetChanged()
    }

    fun verifyFormIsComplete(): Boolean {
        var isFormComplete = true

        for (ingredient in tmpIngredients.values) {
            if (ingredient.name == "Ingredient" || ingredient.name.isEmpty() || ingredient.amount == 0) {
                isFormComplete = false
            }
        }

        return isFormComplete && tmpIngredients.size == containers.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val ingredientName: Spinner = itemView.findViewById(R.id.dec_ingredient_name_1)
        val ingredientAmount: EditText = itemView.findViewById(R.id.dec_ingredient_amount_1)
    }
}