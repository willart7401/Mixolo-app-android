package fr.willban.mixolo.ui.fragments.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.willban.mixolo.R
import fr.willban.mixolo.data.model.Container
import kotlinx.coroutines.launch

class ContainerFragment : Fragment() {

    private lateinit var adapter: ContainerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ContainerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ContainerViewModel::class.java]

        requireActivity().title = "Cocktails"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_capacity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(view)

        lifecycleScope.launch {
            viewModel.getContainers().collect { machine ->
                val containerSize = machine.containers?.size ?: 0
                machine.containers?.let { adapter.refreshMachines(it) }
                recyclerView.layoutManager = if (containerSize > 3) GridLayoutManager(requireContext(), 2) else LinearLayoutManager(requireContext())
            }
        }
    }

    private fun initRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerview_capacity)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ContainerAdapter(::onClickListener, ::onPurgeClickListener)
        recyclerView.adapter = adapter
    }

    private fun onClickListener(container: Container) {
        showAlertDialogEditContainer(container)
    }

    private fun onPurgeClickListener(container: Container) {
        showAlertDialogPurgeContainer(container)
    }

    private fun showAlertDialogEditContainer(container: Container) {
        val view = layoutInflater.inflate(R.layout.dialog_simple_edittext, null)
        val editText: EditText = view.findViewById(R.id.dialog_simple_edittext)
        editText.setText(container.name)

        AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setTitle(getString(R.string.container_name))
            .setView(view)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                container.name = editText.text.toString()
                viewModel.editContainer(container)
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .create()
            .show()
    }

    private fun showAlertDialogPurgeContainer(container: Container) {
        AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setTitle("Purger le container")
            .setMessage("Voulez vous purger le container ${container.name} ?")
            .setPositiveButton("Oui") { _, _ ->
                viewModel.purgeContainer(container)
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .create()
            .show()
    }
}