package com.example.mytodo2.todoapp.controller.dto

import java.time.LocalDateTime

data class TodoResponse(
    val id: Long,
    val title: String,
    val content: String,
    val writer: String,
    val date: LocalDateTime,
)
