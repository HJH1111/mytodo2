package com.example.mytodo2.comment.controller

import com.example.mytodo2.comment.controller.dto.CommentResponse
import com.example.mytodo2.comment.controller.dto.CreateCommentRequest
import com.example.mytodo2.comment.controller.dto.UpdateCommentRequest
import com.example.mytodo2.todoapp.service.TodoService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RequestMapping("/todos/{todoId}/comments")
@RestController
class CommentController(
    val todoService: TodoService
) {

    @PreAuthorize("hasRole('DOMAIN') or hasRole('GENERAL')")
    @GetMapping()
    fun getCommentList(
    ): ResponseEntity<List<CommentResponse>> {
        return ResponseEntity.ok(todoService.getCommentList())
    }

    @PreAuthorize("hasRole('DOMAIN') or hasRole('GENERAL')")
    @GetMapping("/{commentId}")
    fun getComment(
        @PathVariable todoId: Long,
        @PathVariable commentId: Long
    ): ResponseEntity<CommentResponse> {
        return ResponseEntity.ok(todoService.getComment(todoId, commentId))
    }

    @PreAuthorize("hasRole('DOMAIN') or hasRole('GENERAL')")
    @PostMapping()
    fun createComment(
        @PathVariable todoId: Long,
        @RequestBody createCommentRequest: CreateCommentRequest
    ): ResponseEntity<CommentResponse> {
        val commentCr: CommentResponse = todoService.createComment(todoId, createCommentRequest)
        return ResponseEntity.ok(commentCr)
    }

    @PreAuthorize("hasRole('DOMAIN') or hasRole('GENERAL')")
    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable todoId: Long,
        @PathVariable commentId: Long,
        @RequestBody  updateCommentRequest: UpdateCommentRequest
    ): ResponseEntity<CommentResponse> {
        val commentUp: CommentResponse = todoService.updateComment(todoId, commentId, updateCommentRequest)
        return ResponseEntity.ok(commentUp)
    }

    @PreAuthorize("hasRole('DOMAIN') or hasRole('GENERAL')")
    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable todoId: Long,
        @PathVariable commentId: Long
    ): ResponseEntity<Unit> {
       return ResponseEntity.ok(
           todoService.deleteComment(todoId, commentId)
       )
    }
}