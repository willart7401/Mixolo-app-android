package fr.willban.mixolo.ui.activities.machine

import android.content.Intent
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
import fr.willban.mixolo.ui.activities.detail.MachineDetailActivity
import io.github.g00fy2.quickie.QRResult.*
import io.github.g00fy2.quickie.ScanCustomCode
import io.github.g00fy2.quickie.config.ScannerConfig
import kotlinx.coroutines.launch

class MachinesActivity : AppCompatActivity() {

    private var isDeleteMode: Boolean = false
    private lateinit var recyclerview: RecyclerView
    private lateinit var viewModel: MachinesViewModel
    private lateinit var machinesAdapter: MachinesAdapter
    private lateinit var fabAddMachine: FloatingActionButton
    private lateinit var scanQrCodeLauncher: ActivityResultLauncher<ScannerConfig>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_machines)

        initViews()
        setupViews()
        initQrCodeActivityResult()

        fabAddMachine.setOnClickListener {
            scanQrCodeLauncher.launch(
                ScannerConfig.build {
                    setOverlayStringRes(R.string.scan_your_code_on_your_machine)
                    setShowTorchToggle(true)
                    setShowCloseButton(true)
                }
            )
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

        machinesAdapter = MachinesAdapter(::onMachineClickListener, ::onDeleteModeChanged)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = machinesAdapter
    }

    private fun initQrCodeActivityResult() {
        scanQrCodeLauncher = registerForActivityResult(ScanCustomCode()) { result ->
            when (result) {
                is QRSuccess -> {
                    val machineId = result.content.rawValue.substringAfter("machineId=", "")

                    if (machineId.isNotEmpty()) {
                        showAlertDialogAddMachine(machineId)
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
        val intent = Intent(applicationContext, MachineDetailActivity::class.java)
        intent.putExtra("machineId", machine.id)
        startActivity(intent)
    }

    private fun onDeleteModeChanged(isDeleteMode: Boolean) {
        this.isDeleteMode = isDeleteMode
        supportActionBar?.setDisplayHomeAsUpEnabled(isDeleteMode)
        supportActionBar?.setHomeButtonEnabled(isDeleteMode)
        invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val deleteItem = menu.findItem(R.id.main_menu_delete)
        deleteItem.isVisible = isDeleteMode

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_menu_delete -> {
                showAlertDialogDeleteConfirmation()
                true
            }
            android.R.id.home -> {
                machinesAdapter.exitDeleteMode()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAlertDialogAddMachine(machineId: String) {
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

    private fun showAlertDialogDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(getString(R.string.delete_confirmation))
            .setMessage(getString(R.string.delete_confirmation_machines, machinesAdapter.selectedMachines.size))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.delete(machinesAdapter.selectedMachines)
                machinesAdapter.exitDeleteMode()
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .create()
            .show()
    }
}