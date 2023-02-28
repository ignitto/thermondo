package com.thermondo.security

import org.slf4j.LoggerFactory

class UsersRepository {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val usersRepo = mutableMapOf<String, String>()


    //TODO: add username and password validation: length, strength, etc.
    fun createUser(username: String, password: String) {
        if (usersRepo.containsKey(username)) {
            val message = "User: $username already exists"
            logger.error(message)
            throw RuntimeException(message) //TODO: create custom exception and handling
        }
        usersRepo[username] = password
        logger.info("User $username has been created")
    }

    fun isValidUser(username: String, password: String): Boolean {
        val userPassword = usersRepo[username]
        if (password == userPassword) {
            logger.debug("User $username is valid")
            return true
        }
        logger.error("User $username is not valid")
        return false
    }
}
