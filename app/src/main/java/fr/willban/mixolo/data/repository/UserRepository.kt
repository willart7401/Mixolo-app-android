package fr.willban.mixolo.data.repository

import android.content.Context
import fr.willban.mixolo.data.model.User


object UserRepository {

    private var userConnected: User? = null

    fun getUserConnected(): User? {
        return userConnected
    }

    fun logoutUserConnected(context: Context) {
        val editor = context.getSharedPreferences("MIXOLO", Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.commit()
        userConnected = null
    }

    fun saveUserConnected(context: Context, user: User) {
        val editor = context.getSharedPreferences("MIXOLO", Context.MODE_PRIVATE).edit()
        editor.putBoolean("isFirstLogin", false)
        editor.apply()
        userConnected = user
    }
}
