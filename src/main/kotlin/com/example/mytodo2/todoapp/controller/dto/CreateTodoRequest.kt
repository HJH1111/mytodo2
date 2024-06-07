package com.example.mytodo2.todoapp.controller.dto

data class CreateTodoRequest(
    val title: String,
    val content: String,
    val writer: String,
)
