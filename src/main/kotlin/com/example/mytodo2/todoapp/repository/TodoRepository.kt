package com.example.mytodo2.todoapp.repository

import com.example.mytodo2.todoapp.model.Todo
import org.springframework.data.jpa.repository.JpaRepository

interface TodoRepository: JpaRepository<Todo, Long>, CustomTodoRepository  {
}