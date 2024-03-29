package fr.willban.mixolo.ui.fragments.container

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import com.google.android.material.card.MaterialCardView
import fr.willban.mixolo.R
import fr.willban.mixolo.data.model.Container
import fr.willban.mixolo.util.px
import fr.willban.mixolo.util.wave.MultiWaveHeader
import java.lang.Float.min

class ContainerAdapter(private val onClickListener: (Container) -> Unit, private val onPurgeClickListener: (Container) -> Unit) : RecyclerView.Adapter<ContainerAdapter.ViewHolder>() {

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
        holder.waterWaveView.velocity *= if (containers.size > 3) 0.5f else 1f

        if (container.totalAmount != null && container.remainingAmount != null) {
            holder.waterWaveView.progress = min(container.remainingAmount / container.totalAmount.toFloat(), 1f)
        }

        holder.itemView.setOnClickListener {
            onClickListener(container)
        }

        holder.purge.setOnClickListener {
            onPurgeClickListener(container)
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
        val purge: MaterialCardView = itemView.findViewById(R.id.purge)
    }
}