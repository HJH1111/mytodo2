package com.example.mytodo2.user.exception

data class InvalidCredentialException (
    override val message: String? = "The credential is invalid"
): RuntimeException()