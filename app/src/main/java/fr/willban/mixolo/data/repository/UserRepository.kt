package fr.willban.mixolo.data.repository

import fr.willban.mixolo.data.model.User


object UserRepository {

    private var userConnected: User? = null

    fun getUserConnected(): User? {
        return userConnected
    }

    fun saveUserConnected(user: User) {
        userConnected = user
    }
}
