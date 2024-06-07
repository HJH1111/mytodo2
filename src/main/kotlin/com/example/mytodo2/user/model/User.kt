package com.example.mytodo2.user.model

import com.example.mytodo2.todoapp.model.Todo
import com.example.mytodo2.user.dto.UserResponse
import jakarta.persistence.*



@Entity
@Table(name = "todo_user")
class User(
    @Column(name = "email", nullable = false)
    val email: String,

    @Column(name = "password", nullable = false)
    val password: String,

    @Embedded
    var profile: Profile,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: UserRole,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var todos: MutableList<Todo> = mutableListOf()



) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun createTodo(todo: Todo) {
        todos.add(todo)
    }

    fun deleteTodo(todo: Todo) {
        todos.remove(todo)
    }

}
fun User.toResponse(): UserResponse {
    return UserResponse(
        id = id!!,
        nickname = profile.nickname,
        email = email,
        role = role.name
    )
}