package com.example.mytodo2.user.dto

data class UserResponse (
    val id: Long,
    val email: String,
    val nickname: String,
    val role: String
)