package fr.willban.mixolo.ui.fragments.capacity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.willban.mixolo.R
import fr.willban.mixolo.data.model.Container

class CapacityFragment : Fragment() {

    private lateinit var adapter: CapacityAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CapacityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[CapacityViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_capacity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(view)
    }

    private fun initRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerview_capacity)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = CapacityAdapter(::onClickListener)
        recyclerView.adapter = adapter
        adapter.refreshMachines(listOf(Container(1, "Vodka", 2, 2)))
    }

    private fun onClickListener() {

    }
}

