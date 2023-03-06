package fr.willban.mixolo.ui.fragments.capacity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.willban.mixolo.R
import fr.willban.mixolo.data.model.Container

class CapacityAdapter(private val onClickListener: () -> Unit) : RecyclerView.Adapter<CapacityAdapter.ViewHolder>() {

    private var containers: List<Container> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_capacity, parent, false))
    }

    override fun getItemCount(): Int = containers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val container = containers[position]

        holder.textView.text = container.name
    }

    fun refreshMachines(containers: List<Container>) {
        this.containers = containers
        notifyDataSetChanged()
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
//        val capacityLevel: WaterWaveView = itemView.findViewById(R.id.item_capacity_level)
        val textView: TextView = itemView.findViewById(R.id.item_capacity_name)
    }
}