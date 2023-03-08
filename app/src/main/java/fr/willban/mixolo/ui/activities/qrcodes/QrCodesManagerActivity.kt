package fr.willban.mixolo.ui.activities.qrcodes

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import fr.willban.mixolo.R
import fr.willban.mixolo.util.findParameterValue
import io.github.g0dkar.qrcode.QRCode
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

const val BASE_URL = "https://play.google.com/store/apps/details?id=fr.willban.mixolo"

class QrCodesManagerActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var addMachineButton: Button
    private lateinit var editTextContainer: EditText
    private lateinit var editTextCapacity: EditText
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_codes_manager)

        val viewModel = ViewModelProvider(this)[QrCodeManagerViewModel::class.java]

        listView = findViewById(R.id.listview_qrcodes)
        editTextContainer = findViewById(R.id.qrcodes_container)
        editTextCapacity = findViewById(R.id.qrcodes_capacity)
        addMachineButton = findViewById(R.id.qrcodes_add_machine)

        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        listView.adapter = adapter

        lifecycleScope.launch {
            viewModel.getQrCodes().collect { list ->
                val beautifulList = list.map {
                    "${it.findParameterValue("machineId")} : " +
                            "${it.findParameterValue("containers")} x " +
                            "${it.findParameterValue("capacity")} cl"
                }

                adapter.clear()
                adapter.addAll(beautifulList)
                adapter.notifyDataSetChanged()
            }
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            adapter.getItem(position)?.let { url ->
                val file = File(applicationContext.cacheDir, "${System.currentTimeMillis()}.png")

                FileOutputStream(file).use {
                    QRCode(url).render().writeImage(it)
                }

                val imageUri = FileProvider.getUriForFile(applicationContext, "${applicationContext.packageName}.fileprovider", file)

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(imageUri, "image/png")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                startActivity(intent)
            }
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->
            viewModel.removeQrCode(position)
            return@setOnItemLongClickListener true
        }

        addMachineButton.setOnClickListener {
            val machineId = System.currentTimeMillis()
            val container = editTextContainer.text
            val capacity = editTextCapacity.text

            if (container.isNotEmpty() && capacity.isNotEmpty()) {
                viewModel.addQrCode("$BASE_URL&machineId=$machineId&containers=$container&capacity=$capacity")
                editTextCapacity.setText("")
                editTextContainer.setText("")
            }
        }
    }
}