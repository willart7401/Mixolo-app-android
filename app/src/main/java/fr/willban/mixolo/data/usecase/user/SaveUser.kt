package fr.willban.mixolo.data.usecase.user

import fr.willban.mixolo.data.model.User
import fr.willban.mixolo.data.repository.UserRepository

class SaveUser {

    private val userRepository = UserRepository

    fun invoke(user: User) {
        return userRepository.saveUserConnected(user)
    }
}
