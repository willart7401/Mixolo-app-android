package fr.willban.mixolo.ui.activities.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import fr.willban.mixolo.R
import fr.willban.mixolo.data.model.User
import fr.willban.mixolo.data.usecase.user.SaveUser
import fr.willban.mixolo.ui.activities.home.MachinesActivity
import fr.willban.mixolo.util.showShortToast

class SignInActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build(), AuthUI.IdpConfig.EmailBuilder().build())
    private val signInLauncher = registerForActivityResult(FirebaseAuthUIActivityResultContract()) { this.onSignInResult(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        if (!getSharedPreferences("MIXOLO", Context.MODE_PRIVATE).getBoolean("isFirstLogin", true)) {
            startSignInLauncher()
        }

        loginButton = findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            startSignInLauncher()
        }
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
                FirebaseAuth.getInstance().currentUser?.let { firebaseUser ->
                    val user = User(
                        id = firebaseUser.uid,
                        email = firebaseUser.email
                    )

                    SaveUser().invoke(applicationContext, user)
                    startActivity(Intent(this, MachinesActivity::class.java))
                    finish()
                } ?: run {
                    applicationContext.showShortToast("Échec de connexion !")
                }
            }
            else -> applicationContext.showShortToast("Échec de connexion !")
        }
    }
}