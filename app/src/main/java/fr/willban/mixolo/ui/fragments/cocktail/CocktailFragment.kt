package fr.willban.mixolo.ui.fragments.cocktail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.willban.mixolo.R
import fr.willban.mixolo.data.model.Cocktail
import fr.willban.mixolo.data.model.Container
import fr.willban.mixolo.util.DividerItemDecorator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CocktailFragment : Fragment() {

    private lateinit var adapter: CocktailAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CocktailViewModel
    private lateinit var fabCocktail: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[CocktailViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cocktail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)

        fabCocktail.setOnClickListener {
            showAddCocktailPopUp()
        }

        //TODO replace by API Cocktails
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getCocktails().also { cocktails ->
                adapter.refreshMachines(cocktails)
            }
        }
    }

    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerview_cocktails)
        fabCocktail = view.findViewById(R.id.fab_cocktails)

        ResourcesCompat.getDrawable(resources, R.drawable.line_separator, requireContext().theme)?.let { dividerDrawable ->
            val itemDecoration = DividerItemDecorator(dividerDrawable)
            recyclerView.addItemDecoration(itemDecoration)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = CocktailAdapter(::startCocktail)
        recyclerView.adapter = adapter
    }

    private fun startCocktail(cocktail: Cocktail) {
        //TODO start cocktail
    }

    private fun showAddCocktailPopUp() {
        var isRecyclerViewCompleted = false
        var isEditTextNameCompleted = false
        var alertDialog: AlertDialog? = null
        val view = layoutInflater.inflate(R.layout.dialog_edit_cocktail, null)
        val cocktailName: EditText = view.findViewById(R.id.dec_cocktail_name)
        val recyclerView: RecyclerView = view.findViewById(R.id.dec_recyclerview)

        cocktailName.doOnTextChanged { text, _, _, _ ->
            isEditTextNameCompleted = text.toString().isNotEmpty()
            togglePositiveButton(alertDialog, isEditTextNameCompleted && isRecyclerViewCompleted)
        }

        val adapter = ItemAdapter(requireContext()) { isComplete ->
            isRecyclerViewCompleted = isComplete
            togglePositiveButton(alertDialog, isEditTextNameCompleted && isRecyclerViewCompleted)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        //TODO remove temp mock
        adapter.refresh(
            listOf(
                Container(1, "", 0, 0),
                Container(1, "", 0, 0),
                Container(1, "", 0, 0)
            ),
            listOf("Ingredient", "Vodka", "Tequila", "Jus d'orange", "Jus pomme")
        )

        alertDialog = AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setTitle(getString(R.string.container_name))
            .setView(view)
            .setPositiveButton(getString(R.string.add)) { _, _ ->
                createCocktail(adapter.tmpIngredients)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .create()

        alertDialog.apply {
            getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = false
            show()
        }
    }

    private fun togglePositiveButton(alertDialog: AlertDialog?, isCompleted: Boolean) {
        alertDialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = isCompleted
    }

    //TODO create cocktail
    private fun createCocktail(tmpIngredients: HashMap<Int, TmpIngredient>) {
        for (ingredient in tmpIngredients) {
            Log.e("WIWI", "Ingrédient ${ingredient.key} : ${ingredient.value.name} ${ingredient.value.amount} cl")
        }
    }
}