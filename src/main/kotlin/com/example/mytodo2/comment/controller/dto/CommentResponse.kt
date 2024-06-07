package com.example.mytodo2.comment.controller.dto

import java.time.LocalDateTime

data class CommentResponse(
    val id: Long,
    val commentContent: String,
    val writerName: String,
    val date: LocalDateTime
)
