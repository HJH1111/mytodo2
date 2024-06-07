package com.example.mytodo2.comment.model

import com.example.mytodo2.comment.controller.dto.CommentResponse
import com.example.mytodo2.todoapp.model.Todo
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "comment")
class Comment(
    @Column(name = "comment_content")
    var commentContent: String,

    @Column(name = "writer_name")
    var writerName: String,

    @Column(name = "date")
    var date: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    var todo: Todo
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

fun Comment.toCommentResponse(): CommentResponse = CommentResponse(
    id = id!!,
    commentContent = commentContent,
    writerName = writerName,
    date = date,
)