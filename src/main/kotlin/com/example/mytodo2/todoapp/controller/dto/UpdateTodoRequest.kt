package com.example.mytodo2.todoapp.controller.dto

data class UpdateTodoRequest (
    val title: String,
    val content: String,
    val writer: String,
)