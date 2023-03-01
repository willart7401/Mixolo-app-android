package fr.willban.mixolo.ui.machine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import fr.willban.mixolo.R
import fr.willban.mixolo.data.model.Machine

class MachinesAdapter(private val onClickListener: (Machine) -> Unit) : RecyclerView.Adapter<MachinesAdapter.ViewHolder>() {

    private var machines: List<Machine> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_machine, parent, false))
    }

    override fun getItemCount(): Int = machines.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val machine = machines[position]

        holder.textView.text = machine.name

        holder.cardView.setOnClickListener {
            onClickListener(machine)
        }
    }

    fun refreshMachines(machines: List<Machine>) {
        this.machines = machines
        notifyDataSetChanged()
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardview_item_machine)
        val textView: TextView = itemView.findViewById(R.id.textview_item_machine)
    }
}