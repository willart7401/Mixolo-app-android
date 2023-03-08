package fr.willban.mixolo.data.usecase.user

import fr.willban.mixolo.data.model.User
import fr.willban.mixolo.data.repository.UserRepository

class GetUserConnected {

    private val getUserConnected = UserRepository

    fun invoke(): User? {
        return getUserConnected.getUserConnected()
    }
}
