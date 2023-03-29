package fr.willban.mixolo.ui.activities.machine

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.thanosfisherman.wifiutils.WifiUtils
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionErrorCode
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener
import fr.willban.mixolo.R

class MachineDetailActivity : AppCompatActivity() {

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
                connectToWifi()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}