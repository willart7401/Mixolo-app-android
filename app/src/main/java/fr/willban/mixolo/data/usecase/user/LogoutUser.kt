package fr.willban.mixolo.data.usecase.user

import android.content.Context
import fr.willban.mixolo.data.repository.UserRepository

class LogoutUser {

    private val userRepository = UserRepository

    fun invoke(context: Context) {
        return userRepository.logoutUserConnected(context)
    }
}
