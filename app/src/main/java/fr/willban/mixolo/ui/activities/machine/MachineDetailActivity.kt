package fr.willban.mixolo.ui.activities.machine

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.thanosfisherman.wifiutils.WifiUtils
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionErrorCode
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener
import fr.willban.mixolo.R

class MachineDetailActivity : AppCompatActivity() {

    companion object {
        const val ACCESS = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_machine_detail)

        setupNavController()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupNavController() {
        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }

    private fun askPermission(){
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS)
        } else {
            connectToWifi()
        }
    }

    private fun connectToWifi(){
        WifiUtils.withContext(applicationContext).enableWifi()
        WifiUtils.withContext(applicationContext).scanWifi(null).start()
        WifiUtils.withContext(applicationContext)
            .connectWith("MIXOLO WIFI MANAGER", "44:17:93:8a:19:19","")
            .onConnectionResult(object : ConnectionSuccessListener {
                override fun success() {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.4.1")))
                }

                override fun failed(errorCode: ConnectionErrorCode) {
                    Toast.makeText(applicationContext, "Impossible de se connecter à l'appareil", Toast.LENGTH_SHORT).show()
                }
            })
            .start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ACCESS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    connectToWifi()
                } else {
                    Toast.makeText(applicationContext, "Vous devez accepter la permission pour continuer", Toast.LENGTH_LONG).show()
                }
                return
            }
            else -> {}
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.machine_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.wifi_configure -> {
                askPermission()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}