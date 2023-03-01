package fr.willban.mixolo.ui.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import fr.willban.mixolo.R
import fr.willban.mixolo.TAG
import fr.willban.mixolo.ui.machine.MachineActivity

class SignInActivity : AppCompatActivity() {

    private val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build(), AuthUI.IdpConfig.EmailBuilder().build())
    private val signInLauncher = registerForActivityResult(FirebaseAuthUIActivityResultContract()) { this.onSignInResult(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        startSignInLauncher()
    }

    private fun startSignInLauncher() {
        signInLauncher.launch(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
        )
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        when (result.resultCode) {
            RESULT_OK -> {
                val currentUser = FirebaseAuth.getInstance().currentUser

                if (currentUser != null) {
                    //val user = User(currentUser.displayName, currentUser.email, currentUser.phoneNumber, currentUser.photoUrl.toString())
                } else {
                    Log.e("FirebaseUser", "FirebaseUser is null")
                }

                startActivity(Intent(this, MachineActivity::class.java))
                finish()
            }
            else -> Log.e(TAG, "FAILED CONNECT FIREBASE : ${result.resultCode}")
        }
    }

    private fun logout() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener {
            Toast.makeText(this, "Vous êtes déconnecté !", Toast.LENGTH_LONG).show()
            startSignInLauncher()
        }
    }
}