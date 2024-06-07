package com.example.mytodo2.comment.controller.dto

data class CreateCommentRequest(
    val commentContent: String,
    val writerName: String
)