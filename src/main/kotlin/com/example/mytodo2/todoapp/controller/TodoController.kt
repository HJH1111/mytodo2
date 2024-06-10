package com.example.mytodo2.todoapp.controller

import com.example.mytodo2.todoapp.controller.dto.CreateTodoRequest
import com.example.mytodo2.todoapp.controller.dto.TodoResponse
import com.example.mytodo2.todoapp.controller.dto.UpdateTodoRequest
import com.example.mytodo2.todoapp.service.TodoService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


@RestController
class TodoController(
    val todoService: TodoService
) {

//    @PreAuthorize("hasRole('DOMAIN') or hasRole('GENERAL')")
    @GetMapping("/todos")
    fun getTodoList(): ResponseEntity<List<TodoResponse>> {
        return ResponseEntity.ok(todoService.getTodoList())
    }

//    @PreAuthorize("hasRole('DOMAIN') or hasRole('GENERAL')")
    @GetMapping("/todos/{todoId}")
    fun getTodo(
        @PathVariable("todoId")
        todoId: Long
    ): ResponseEntity<TodoResponse> {
        return ResponseEntity.ok(todoService.getTodo(todoId))
    }

    @PreAuthorize("hasRole('DOMAIN') or hasRole('GENERAL')")
    @PostMapping("/todos")
    fun createTodo(
        @RequestBody request: CreateTodoRequest
    ): ResponseEntity<TodoResponse> {
        return ResponseEntity.ok(todoService.createTodo(request))
    }

    @PreAuthorize("hasRole('DOMAIN') or hasRole('GENERAL')")
    @PutMapping("/todos/{todoId}")
    fun updateTodo(
        @PathVariable todoId: Long,
        @RequestBody updateTodoRequest: UpdateTodoRequest
    ): ResponseEntity<TodoResponse> {
        return ResponseEntity.ok(todoService.updateTodo(todoId, updateTodoRequest))
    }

    @PreAuthorize("hasRole('DOMAIN') or hasRole('GENERAL')")
    @DeleteMapping("/todos/{todoId}")
    fun deleteTodo(@PathVariable todoId: Long): ResponseEntity<Unit> {
        return ResponseEntity.ok(todoService.deleteTodo(todoId))
    }
}