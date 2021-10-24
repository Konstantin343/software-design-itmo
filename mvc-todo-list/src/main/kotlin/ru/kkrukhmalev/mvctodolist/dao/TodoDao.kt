package ru.kkrukhmalev.mvctodolist.dao

import ru.kkrukhmalev.mvctodolist.model.Todo
import ru.kkrukhmalev.mvctodolist.model.TodoList

interface TodoDao {
    fun addTodo(todo: Todo): Int

    fun addTodoList(todoList: TodoList): Int
    
    fun getTodoList(id: Int): TodoList

    fun getTodoLists(): List<TodoList>

    fun getTodos(): List<Todo>
    
    fun getTodos(listId: Int): List<Todo>

    fun changeTodoStatus(todoId: Int, done: Boolean)

    fun removeTodo(todoId: Int)
    
    fun removeTodos(listId: Int)

    fun removeList(listId: Int)
}