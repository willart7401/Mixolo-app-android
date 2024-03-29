package fr.willban.mixolo.data.usecase.user

import android.content.Context
import fr.willban.mixolo.data.model.User
import fr.willban.mixolo.data.repository.UserRepository

class SaveUser {

    private val userRepository = UserRepository

    fun invoke(context: Context, user: User) {
        return userRepository.saveUserConnected(context, user)
    }
}
