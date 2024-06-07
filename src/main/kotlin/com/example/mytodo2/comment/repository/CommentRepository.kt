package com.example.mytodo2.comment.repository

import com.example.mytodo2.comment.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository: JpaRepository<Comment, Long>  {
    fun findByTodoIdAndId(todoId: Long, commentId: Long): Comment?
}