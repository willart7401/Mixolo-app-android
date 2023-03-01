package fr.willban.mixolo.ui.machine

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.willban.mixolo.R
import fr.willban.mixolo.data.model.Machine
import io.github.g00fy2.quickie.QRResult.*
import io.github.g00fy2.quickie.ScanQRCode
import kotlinx.coroutines.launch

class MachineActivity : AppCompatActivity() {

    private lateinit var recyclerview: RecyclerView
    private lateinit var viewModel: MachinesViewModel
    private lateinit var machinesAdapter: MachinesAdapter
    private lateinit var fabAddMachine: FloatingActionButton
    private lateinit var scanQrCodeLauncher: ActivityResultLauncher<Nothing?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_machine)

        initViews()
        setupViews()
        initQrCodeActivityResult()

        fabAddMachine.setOnClickListener {
            scanQrCodeLauncher.launch(null)
        }

        lifecycleScope.launch {
            viewModel.getMachines().collect { machines ->
                machinesAdapter.refreshMachines(machines)
            }
        }
    }

    private fun initViews() {
        fabAddMachine = findViewById(R.id.fab_machine)
        recyclerview = findViewById(R.id.recyclerview_machine)
    }

    private fun setupViews() {
        this.title = getString(R.string.machines)

        viewModel = ViewModelProvider(this)[MachinesViewModel::class.java]

        machinesAdapter = MachinesAdapter(::onMachineClickListener)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = machinesAdapter
    }

    private fun initQrCodeActivityResult() {
        scanQrCodeLauncher = registerForActivityResult(ScanQRCode()) { result ->
            when (result) {
                is QRSuccess -> {
                    val machineId = result.content.rawValue.substringAfter("machineId=", "")

                    if (machineId.isNotEmpty()) {
                        showAlertDialogButtonClicked(machineId)
                    } else {
                        Toast.makeText(applicationContext, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                    }
                }
                is QRMissingPermission -> {
                    Toast.makeText(applicationContext, "Autorisation caméra manquante", Toast.LENGTH_SHORT).show()
                }
                is QRError -> {
                    Toast.makeText(applicationContext, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                    result.exception.printStackTrace()
                }
                else -> {}
            }
        }
    }

    private fun onMachineClickListener(machine: Machine) {
        //TODO on machine click
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_menu_delete -> {
                //TODO on delete click
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAlertDialogButtonClicked(machineId: String) {
        val view = layoutInflater.inflate(R.layout.dialog_add_machine, null)
        val editText: EditText = view.findViewById(R.id.dialog_add_machine_editText)

        AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(getString(R.string.machine_name))
            .setView(view)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                viewModel.addMachine(
                    machine = Machine(id = machineId, name = editText.text.toString(), admins = emptyList())
                )
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .create()
            .show()
    }
}