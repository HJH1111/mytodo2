package com.example.mytodo2.todoapp.service

import com.example.mytodo2.comment.controller.dto.CommentResponse
import com.example.mytodo2.comment.controller.dto.CreateCommentRequest
import com.example.mytodo2.comment.controller.dto.UpdateCommentRequest
import com.example.mytodo2.comment.model.Comment
import com.example.mytodo2.comment.model.toCommentResponse
import com.example.mytodo2.comment.repository.CommentRepository
import com.example.mytodo2.exception.ModelNotFoundException
import com.example.mytodo2.infra.aop.StopWatch
import com.example.mytodo2.todoapp.controller.dto.CreateTodoRequest
import com.example.mytodo2.todoapp.controller.dto.TodoResponse
import com.example.mytodo2.todoapp.controller.dto.UpdateTodoRequest
import com.example.mytodo2.todoapp.model.Todo
import com.example.mytodo2.todoapp.model.toTodoResponse
import com.example.mytodo2.todoapp.repository.TodoRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TodoService(
    val todoRepository: TodoRepository,
    val commentRepository: CommentRepository
) {
    fun getTodoList(): List<TodoResponse> {
        return todoRepository.findAll().map { it.toTodoResponse() }
    }

    @StopWatch
    fun getTodo(todoId: Long): TodoResponse {
        val result = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        return result.toTodoResponse()
    }

    @Transactional
    fun createTodo(request: CreateTodoRequest): TodoResponse {
        return todoRepository.save(
            Todo(
                title = request.title,
                content = request.content,
                writer = request.writer
            )
        ).toTodoResponse()
    }

    @Transactional
    fun updateTodo(todoId: Long, request: UpdateTodoRequest): TodoResponse {
        val result = todoRepository.findByIdOrNull(todoId) ?: throw Exception("Todo")
        if (result == null) throw ModelNotFoundException("Todo", todoId)

        result.title = request.title
        result.content = request.content
        result.writer = request.writer
        return todoRepository.save(result).toTodoResponse()
    }

    @Transactional
    fun deleteTodo(todoId: Long) {
        val result = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        todoRepository.delete(result)
    }

    fun getCommentList(): List<CommentResponse> {
        return commentRepository.findAll().map { it.toCommentResponse() }
    }


    fun getComment(todoId: Long, commentId: Long): CommentResponse {
        val commentGet = commentRepository.findByTodoIdAndId(todoId, commentId)
            ?: throw ModelNotFoundException("Comment", commentId)
        return commentGet.toCommentResponse()
    }

    @Transactional
    fun createComment(todoId: Long, request: CreateCommentRequest): CommentResponse {
        val todoCr = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)

        val comment = Comment(
            writerName = request.writerName,
            commentContent = request.commentContent,
            todo = todoCr
        )
        todoCr.createComment(comment)
        commentRepository.save(comment)
        return comment.toCommentResponse()
    }

    @Transactional
    fun updateComment(
        todoId: Long,
        commentId: Long,
        request: UpdateCommentRequest,
    ): CommentResponse {
        val commentUp = commentRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Comment", commentId)
        val (writerName, commentContent) = request
        commentUp.writerName = writerName
        commentUp.commentContent = commentContent

        return commentRepository.save(commentUp).toCommentResponse()
    }

    @Transactional
    fun deleteComment(
        todoId: Long,
        commentId: Long
    ) {
        val todoDe = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        val commentDe =
            commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException("Comment", commentId)
        commentRepository.deleteById(commentId)
    }

}