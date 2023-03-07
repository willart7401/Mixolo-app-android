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
import fr.willban.mixolo.data.model.LocalMachine

class MachinesAdapter(
    private val onClickListener: (LocalMachine) -> Unit,
    private val onSelectModeChanged: (Boolean) -> Unit
) : RecyclerView.Adapter<MachinesAdapter.ViewHolder>() {

    private var isSelectMode = false
    val selectedMachines = mutableListOf<LocalMachine>()
    private var machines: List<LocalMachine> = emptyList()

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
            if (!isSelectMode) {
                onSelectModeChanged(true)
                isSelectMode = true
                selectItem(holder, machine)
                true
            } else {
                false
            }
        }

        holder.itemView.setOnClickListener {
            if (isSelectMode) {
                onSelectModeChanged(true)
                selectItem(holder, machine)
            } else {
                onClickListener(machine)
            }
        }
    }

    private fun selectItem(holder: ViewHolder, machine: LocalMachine) {
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

    fun refreshMachines(machines: List<LocalMachine>) {
        this.machines = machines
        notifyDataSetChanged()
    }

    fun exitSelectMode() {
        onSelectModeChanged(false)
        isSelectMode = false
        selectedMachines.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val iconSelected: ImageView = itemView.findViewById(R.id.selected_item_machine)
        val textView: TextView = itemView.findViewById(R.id.textview_item_machine)
    }
}