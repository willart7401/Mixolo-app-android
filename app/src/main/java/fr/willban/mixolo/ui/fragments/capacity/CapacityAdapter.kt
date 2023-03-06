package fr.willban.mixolo.ui.fragments.capacity

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.willban.mixolo.R
import fr.willban.mixolo.data.model.Container
import fr.willban.mixolo.util.wave.MultiWaveHeader

class CapacityAdapter(private val onClickListener: (Container) -> Unit) : RecyclerView.Adapter<CapacityAdapter.ViewHolder>() {

    private var containers: List<Container> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_capacity, parent, false))
    }

    override fun getItemCount(): Int = containers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val container = containers[position]

        holder.textView.text = container.name
        holder.waterWaveView.setWaves("0,0,1,1,-26")
        holder.waterWaveView.progress = container.remainingAmount / container.totalAmount.toFloat()
        Log.e("test", "${container.remainingAmount / container.totalAmount.toFloat()}")

        holder.itemView.setOnClickListener {
            onClickListener(container)
        }
    }

    fun refreshMachines(containers: List<Container>) {
        this.containers = containers
        notifyDataSetChanged()
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val waterWaveView: MultiWaveHeader = itemView.findViewById(R.id.water_wave_view)
        val textView: TextView = itemView.findViewById(R.id.item_capacity_name)
    }
}