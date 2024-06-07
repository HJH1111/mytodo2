package com.example.mytodo2.todoapp.model

import com.example.mytodo2.comment.model.Comment
import com.example.mytodo2.todoapp.controller.dto.TodoResponse
import com.example.mytodo2.user.model.User
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "todo")
class Todo(
    @Column(name = "title")
    var title: String,

    @Column(name = "content")
    var content: String,

    @Column(name = "writer")
    var writer: String,

    @Column(name = "date")
    var date: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "todo", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var comments: MutableList<Comment> = mutableListOf()
    )
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun createComment(comment: Comment) {
        comments.add(comment)
    }
    fun deleteComment(comment: Comment) {
        comments.remove(comment)
    }
}

fun Todo.toTodoResponse(): TodoResponse = TodoResponse (
    id = id!!,
    title = title,
    content = content,
    date = date,
    writer = writer
)