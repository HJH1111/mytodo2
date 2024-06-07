package com.example.mytodo2.todoapp.repository

import com.example.mytodo2.infra.querydsl.QueryDslSupport
import com.example.mytodo2.todoapp.model.QTodo
import com.example.mytodo2.todoapp.model.Todo
import org.springframework.stereotype.Repository

@Repository
class TodoRepositoryImpl : QueryDslSupport(), CustomTodoRepository {

    private val todo = QTodo.todo

    override fun searchTodoListByTitle(title: String): List<Todo> {
        return queryFactory.selectFrom(todo)
            .where(todo.title.containsIgnoreCase(title))
            .fetch()
    }


}