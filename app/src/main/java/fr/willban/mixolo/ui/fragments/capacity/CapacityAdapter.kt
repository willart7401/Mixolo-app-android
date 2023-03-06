package fr.willban.mixolo.ui.fragments.capacity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import fr.willban.mixolo.R
import fr.willban.mixolo.data.model.Container
import fr.willban.mixolo.util.px
import fr.willban.mixolo.util.wave.MultiWaveHeader

class CapacityAdapter(private val onClickListener: (Container) -> Unit) : RecyclerView.Adapter<CapacityAdapter.ViewHolder>() {

    private var containers: List<Container> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_capacity, parent, false)
        view.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, (parent.measuredHeight - 16.px) / 3)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = containers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val container = containers[position]

        holder.containerId.text = container.id.toString()
        holder.textView.text = container.name
        holder.waterWaveView.setWaves("0,0,1,1,-26")
        holder.waterWaveView.scaleY = -1f
        holder.waterWaveView.progress = container.remainingAmount / container.totalAmount.toFloat()
        holder.waterWaveView.velocity = if (containers.size > 3) 4f else 8f

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
        val containerId: TextView = itemView.findViewById(R.id.item_capacity_id)
        val textView: TextView = itemView.findViewById(R.id.item_capacity_name)
    }
}