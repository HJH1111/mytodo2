package com.example.mytodo2.user.dto

data class LoginRequest(
    val email: String,
    val password: String,
    val role: String
)