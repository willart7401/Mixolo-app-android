package fr.willban.mixolo.ui.activities.home

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.willban.mixolo.R
import fr.willban.mixolo.data.model.LocalMachine
import fr.willban.mixolo.ui.activities.machine.MachineDetailActivity
import io.github.g00fy2.quickie.QRResult.*
import io.github.g00fy2.quickie.ScanCustomCode
import io.github.g00fy2.quickie.config.ScannerConfig

class MachinesActivity : AppCompatActivity() {

    private var isSelectMode: Boolean = false
    private lateinit var recyclerview: RecyclerView
    private lateinit var viewModel: MachinesViewModel
    private var localMachines = listOf<LocalMachine>()
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

        viewModel.getMachines().observe(this) { machines ->
            machinesAdapter.refreshMachines(machines)
            localMachines = machines
        }
    }

    private fun initViews() {
        fabAddMachine = findViewById(R.id.fab_machine)
        recyclerview = findViewById(R.id.recyclerview_machine)
    }

    private fun setupViews() {
        this.title = getString(R.string.machines)

        viewModel = ViewModelProvider(this)[MachinesViewModel::class.java]

        machinesAdapter = MachinesAdapter(::onMachineClickListener, ::onSelectModeChanged)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = machinesAdapter
    }

    private fun initQrCodeActivityResult() {
        scanQrCodeLauncher = registerForActivityResult(ScanCustomCode()) { result ->
            when (result) {
                is QRSuccess -> {
                    result.content.rawValue.substringAfter("machineId=", "").takeIf { it.isNotEmpty() }?.let { machineId ->
                        val isMachineAlreadyExist = localMachines.find { it.id == machineId } != null

                        if (isMachineAlreadyExist) {
                            Toast.makeText(applicationContext, "Vous avez déja importé cette machine !", Toast.LENGTH_SHORT).show()
                        } else {
                            showAlertDialogAddMachine(machineId)
                        }
                    } ?: run {
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

    private fun onMachineClickListener(machine: LocalMachine) {
        val intent = Intent(applicationContext, MachineDetailActivity::class.java)
        intent.putExtra("machineId", machine.id)
        startActivity(intent)
    }

    private fun onSelectModeChanged(isDeleteMode: Boolean) {
        this.isSelectMode = isDeleteMode
        supportActionBar?.setDisplayHomeAsUpEnabled(isDeleteMode)
        supportActionBar?.setHomeButtonEnabled(isDeleteMode)
        invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        menu.findItem(R.id.main_menu_delete).isVisible = isSelectMode
        menu.findItem(R.id.main_menu_edit).isVisible = isSelectMode && machinesAdapter.selectedMachines.size == 1

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_menu_delete -> {
                showAlertDialogDeleteConfirmation()
                true
            }
            R.id.main_menu_edit -> {
                machinesAdapter.selectedMachines.firstOrNull()?.let { showAlertDialogEditMachine(it) }
                true
            }
            android.R.id.home -> {
                machinesAdapter.exitSelectMode()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAlertDialogEditMachine(machine: LocalMachine) {
        val view = layoutInflater.inflate(R.layout.dialog_simple_edittext, null)
        val editText: EditText = view.findViewById(R.id.dialog_simple_edittext)
        editText.setText(machine.name)

        AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(getString(R.string.edit_machine_name))
            .setView(view)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                viewModel.editMachine(
                    machine = LocalMachine(id = machine.id, name = editText.text.toString())
                )
                machinesAdapter.exitSelectMode()
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .create()
            .show()
    }

    private fun showAlertDialogAddMachine(machineId: String) {
        val view = layoutInflater.inflate(R.layout.dialog_simple_edittext, null)
        val editText: EditText = view.findViewById(R.id.dialog_simple_edittext)

        AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(getString(R.string.machine_name))
            .setView(view)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                viewModel.addMachine(
                    machine = LocalMachine(id = machineId, name = editText.text.toString())
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
                viewModel.delete(machinesAdapter.selectedMachines.toList())
                machinesAdapter.exitSelectMode()
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .create()
            .show()
    }
}