package fr.willban.mixolo.ui.activities.machine

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.willban.mixolo.R
import fr.willban.mixolo.data.model.Machine

class MachinesAdapter(
    private val onClickListener: (Machine) -> Unit,
    private val onDeleteModeChanged: (Boolean) -> Unit
) : RecyclerView.Adapter<MachinesAdapter.ViewHolder>() {

    private var isDeleteMode = false
    val selectedMachines = mutableListOf<Machine>()
    private var machines: List<Machine> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_machine, parent, false))
    }

    override fun getItemCount(): Int = machines.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val machine = machines[position]

        holder.textView.text = machine.name

        if (selectedMachines.contains(machine)) {
            holder.itemView.alpha = 0.75f
            holder.iconSelected.visibility = VISIBLE
        } else {
            holder.iconSelected.visibility = GONE
            holder.itemView.alpha = 1.0f
        }

        holder.itemView.setOnLongClickListener {
            if (!isDeleteMode) {
                onDeleteModeChanged(true)
                isDeleteMode = true
                selectItem(holder, machine)
                true
            } else {
                false
            }
        }

        holder.itemView.setOnClickListener {
            if (isDeleteMode) {
                selectItem(holder, machine)
            } else {
                onClickListener(machine)
            }
        }
    }

    private fun selectItem(holder: ViewHolder, machine: Machine) {
        if (selectedMachines.contains(machine)) {
            selectedMachines.remove(machine)
            holder.iconSelected.visibility = GONE
            holder.itemView.alpha = 1.0f
        } else {
            selectedMachines.add(machine)
            holder.iconSelected.visibility = VISIBLE
            holder.itemView.alpha = 0.75f
        }
    }

    fun refreshMachines(machines: List<Machine>) {
        this.machines = machines
        notifyDataSetChanged()
    }

    fun exitDeleteMode() {
        onDeleteModeChanged(false)
        isDeleteMode = false
        selectedMachines.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val iconSelected: ImageView = itemView.findViewById(R.id.selected_item_machine)
        val textView: TextView = itemView.findViewById(R.id.textview_item_machine)
    }
}