package com.thermondo.security

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UsersRepositoryTest {

    private val usersRepository = UsersRepository()

    @Test
    fun `create and valid user success`() {
        usersRepository.createUser("Mickey", "1234")
        assertTrue(usersRepository.isValidUser("Mickey", "1234"))
    }

    @Test
    fun `create and valid user fail`() {
        usersRepository.createUser("Thomas", "1234")
        assertFalse(usersRepository.isValidUser("Jerry", "1234"))
    }

    @Test
    fun `create multiple times same username should fail`() {
        usersRepository.createUser("Bob", "1234")
        assertFailsWith(
            exceptionClass = RuntimeException::class,
            block = { usersRepository.createUser("Bob", "4567") }
        )
    }
}
