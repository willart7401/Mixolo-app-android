package fr.willban.mixolo.ui.fragments.cocktail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import fr.willban.mixolo.R
import fr.willban.mixolo.data.model.Cocktail
import fr.willban.mixolo.util.prettyPrint

class CocktailAdapter(private val onClickListener: (Cocktail) -> Unit, private val onLongClickListener: (Cocktail) -> Unit) :
    RecyclerView.Adapter<CocktailAdapter.ViewHolder>() {

    private var cocktails: List<Cocktail> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cocktail, parent, false))
    }

    override fun getItemCount(): Int = cocktails.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cocktail = cocktails[position]

        holder.name.text = cocktail.name
        holder.ingredients.text = cocktail.ingredients?.prettyPrint()

        holder.playButton.setOnClickListener {
            onClickListener(cocktail)
        }

        holder.cardView.setOnLongClickListener {
            onLongClickListener(cocktail)
            true
        }
    }

    fun refreshMachines(cocktails: List<Cocktail>) {
        this.cocktails = cocktails
        notifyDataSetChanged()
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val playButton: ImageView = itemView.findViewById(R.id.item_cocktail_play)
        val name: TextView = itemView.findViewById(R.id.item_cocktail_name)
        val ingredients: TextView = itemView.findViewById(R.id.item_cocktail_ingredients)
        val cardView: CardView = itemView.findViewById(R.id.item_cocktail_root)
    }
}