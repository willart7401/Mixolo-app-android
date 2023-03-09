package fr.willban.mixolo.ui.activities.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.willban.mixolo.R
import fr.willban.mixolo.data.model.LocalMachine
import fr.willban.mixolo.data.model.RemoteMachine
import fr.willban.mixolo.ui.activities.machine.MachineDetailActivity
import fr.willban.mixolo.ui.activities.qrcodes.QrCodesManagerActivity
import fr.willban.mixolo.ui.activities.signin.SignInActivity
import fr.willban.mixolo.util.findParameterValue
import fr.willban.mixolo.util.showShortToast
import fr.willban.mixolo.util.toInt
import fr.willban.mixolo.util.tryToInt
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
                    val url = result.content.rawValue
                    val machineId = url.findParameterValue("machineId")
                    val containerNbString = url.findParameterValue("containers")
                    val containerCapacityString = url.findParameterValue("capacity")

                    when {
                        machineId.isNullOrEmpty() || containerNbString.isNullOrEmpty() || containerCapacityString.isNullOrEmpty() -> {
                            applicationContext.showShortToast("Machine inconnue")
                        }
                        localMachines.find { it.id == machineId } != null -> {
                            applicationContext.showShortToast("Vous avez déja importé cette machine")
                        }
                        else -> {
                            containerNbString.tryToInt { containerNb ->
                                containerCapacityString.tryToInt { containerCapacity ->
                                    showAlertDialogAddMachine(machineId, containerNb, containerCapacity)
                                }
                            }
                        }
                    }
                }
                is QRMissingPermission -> {
                    applicationContext.showShortToast("Autorisation caméra manquante")
                }
                is QRError -> {
                    applicationContext.showShortToast("AUne erreur est survenue")
                    result.exception.printStackTrace()
                }
                else -> {}
            }
        }
    }

    private fun onMachineClickListener(machine: LocalMachine) {
        viewModel.saveSelectedMachine(machine.id)
        startActivity(Intent(applicationContext, MachineDetailActivity::class.java))
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
        menu.findItem(R.id.main_menu_admin).isVisible = viewModel.isUserConnectedIsAdmin()

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
            R.id.main_menu_admin -> {
                machinesAdapter.exitSelectMode()
                startActivity(Intent(applicationContext, QrCodesManagerActivity::class.java))
                true
            }
            R.id.main_menu_logout -> {
                machinesAdapter.exitSelectMode()
                showAlertDialogLogoutConfirmation()
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

        val alertDialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(getString(R.string.edit_machine_name))
            .setView(view)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                viewModel.editMachine(
                    machine = LocalMachine(id = machine.id, name = editText.text.toString())
                )
                machinesAdapter.exitSelectMode()
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                machinesAdapter.exitSelectMode()
            }
            .create()

        editText.doOnTextChanged { text, _, _, _ ->
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = text.toString().isNotEmpty()
        }

        alertDialog.apply {
            show()
            getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = false
        }
    }

    private fun showAlertDialogAddMachine(machineId: String, containerNb: Int, containerCapacity: Int) {
        val view = layoutInflater.inflate(R.layout.dialog_simple_edittext, null)
        val editText: EditText = view.findViewById(R.id.dialog_simple_edittext)
        editText.setText("")

        val alertDialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(getString(R.string.machine_name))
            .setView(view)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                viewModel.addMachine(LocalMachine(id = machineId, name = editText.text.toString()), containerNb, containerCapacity)
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .create()

        editText.doOnTextChanged { text, _, _, _ ->
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = text.toString().isNotEmpty()
        }

        alertDialog.apply {
            show()
            getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = false
        }
    }

    private fun showAlertDialogDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setCancelable(true)
            .setTitle(getString(R.string.delete_confirmation))
            .setMessage(getString(R.string.delete_confirmation_machines, machinesAdapter.selectedMachines.size))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.delete(machinesAdapter.selectedMachines.toList())
                machinesAdapter.exitSelectMode()
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                machinesAdapter.exitSelectMode()
            }
            .create()
            .show()
    }

    private fun showAlertDialogLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setCancelable(true)
            .setTitle(getString(R.string.logout_confirmation))
            .setMessage(getString(R.string.logout_confirmation_message))
            .setPositiveButton(getString(R.string.logout)) { _, _ ->
                logout()
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .create()
            .show()
    }

    private fun logout() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener {
            viewModel.logout(applicationContext)
            applicationContext.showShortToast("Vous êtes déconnecté !")
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }
}