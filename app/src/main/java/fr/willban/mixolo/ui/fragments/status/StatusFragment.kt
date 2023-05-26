package fr.willban.mixolo.ui.fragments.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import fr.willban.mixolo.R
import fr.willban.mixolo.util.prettyPrint
import kotlinx.coroutines.launch

class StatusFragment : Fragment() {

    private lateinit var viewModel: StatusViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().title = "En cours"

        viewModel = ViewModelProvider(requireActivity())[StatusViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val statusCocktail = view.findViewById<CardView>(R.id.status_cocktail)
        val statusCocktailName = view.findViewById<TextView>(R.id.status_cocktail_name)
        val statusCocktailIngredients = view.findViewById<TextView>(R.id.status_cocktail_ingredients)
        val statusProgressBar = view.findViewById<ProgressBar>(R.id.status_progress_bar)

        lifecycleScope.launch {
            viewModel.getStatus().collect() { machine ->
                if (machine.cocktail != null) {
                    statusCocktail.isVisible = true
                    statusProgressBar.isVisible = true
                    statusCocktailName.text = machine.cocktail!!.name
                    statusCocktailIngredients.text = machine.cocktail!!.ingredients?.prettyPrint()
                } else {
                    statusCocktail.isVisible = false
                    statusProgressBar.isVisible = false
                }
            }
        }
    }
}
