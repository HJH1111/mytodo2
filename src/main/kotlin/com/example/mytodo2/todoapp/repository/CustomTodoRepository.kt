package com.example.mytodo2.todoapp.repository

import com.example.mytodo2.todoapp.model.Todo

interface CustomTodoRepository {

    fun searchTodoListByTitle(title: String): List<Todo>
}