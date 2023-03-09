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
import fr.willban.mixolo.data.model.Ingredient
import fr.willban.mixolo.util.DividerItemDecorator
import fr.willban.mixolo.util.showShortToast
import kotlinx.coroutines.launch

class CocktailFragment : Fragment() {

    private var cocktailList = emptyList<Cocktail>()
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

        lifecycleScope.launch {
            viewModel.getCocktails()?.collect { cocktails ->
                adapter.refreshMachines(cocktails)
                cocktailList = cocktails
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
        viewModel.startCocktail(cocktail) { msg ->
            requireContext().showShortToast(msg)
        }
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

        viewModel.getContainersAndIngredients { containers, ingredients ->
            adapter.refresh(containers, ingredients)
        }

        alertDialog = AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setTitle(getString(R.string.container_name))
            .setView(view)
            .setPositiveButton(getString(R.string.add)) { _, _ ->
                createCocktail(cocktailName.text.toString(), adapter.tmpIngredients)
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

    private fun createCocktail(cocktailName: String, tmpIngredients: HashMap<Int, TmpIngredient>) {
        val cocktail = Cocktail(
            id = cocktailList.size + 1,
            name = cocktailName,
            ingredients = tmpIngredients.entries.filter { (_, value) -> value.amount != 0 || value.name != "Ingredient" }
                .map { Ingredient(it.key, it.value.name, it.value.amount) }
        )

        viewModel.addCocktail(cocktail)
    }
}