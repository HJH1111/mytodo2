package com.example.mytodo2.user.service

import com.example.mytodo2.exception.ModelNotFoundException
import com.example.mytodo2.infra.security.jwt.JwtPlugin
import com.example.mytodo2.user.dto.*
import com.example.mytodo2.user.exception.InvalidCredentialException
import com.example.mytodo2.user.model.Profile
import com.example.mytodo2.user.model.User
import com.example.mytodo2.user.model.UserRole
import com.example.mytodo2.user.model.toResponse
import com.example.mytodo2.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin
) {

    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email) ?: throw ModelNotFoundException("User", null)

        if (user.role.name != request.role || !passwordEncoder.matches(request.password, user.password)) {
            throw InvalidCredentialException()
        }

        return LoginResponse(
            accessToken = jwtPlugin.generateAccessToken(
                subject = user.id.toString(),
                email = user.email,
                role = user.role.name
            )
        )
    }

    fun signUp(request: SignUpRequest): UserResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalStateException("Email is already in use")
        }

        return userRepository.save(
            User(
                email = request.email,
                password = passwordEncoder.encode(request.password),
                profile = Profile(
                    nickname = request.nickname
                ),
                role = when (request.role) {
                    "ADMIN" -> UserRole.ADMIN
                    "GENERAL" -> UserRole.GENERAL
                    else -> throw IllegalArgumentException("Invalid role")
                }
            )
        ).toResponse()
    }

    fun updateUserProfile(userId: Long, updateUserProfileRequest: UpdateUserProfileRequest): UserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)
        user.profile = Profile(
            nickname = updateUserProfileRequest.nickname
        )

        return userRepository.save(user).toResponse()
    }

}