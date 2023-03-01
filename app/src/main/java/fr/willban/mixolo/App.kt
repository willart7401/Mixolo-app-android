package fr.willban.mixolo

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

const val TAG = "DEBUG-Mixolo"

class App : Application() {

    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate() {
        analytics = Firebase.analytics
        super.onCreate()
    }
}
