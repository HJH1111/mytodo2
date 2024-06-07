package com.example.mytodo2.user.repository


import com.example.mytodo2.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {

    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): User?
}