package ru.kkrukhmalev.mvctodolist.dao

import ru.kkrukhmalev.mvctodolist.model.Todo
import ru.kkrukhmalev.mvctodolist.model.TodoList
import java.util.concurrent.atomic.AtomicInteger

class TodoInMemoryDao : TodoDao {
    private val lastTodoId = AtomicInteger(0)
    private val lastTodoListId = AtomicInteger(0)
    private val todoLists = mutableListOf<TodoList>()
    private val todos = mutableListOf<Todo>()

    override fun addTodo(todo: Todo): Int {
        todo.id = lastTodoId.incrementAndGet()
        todos.add(todo)
        return todo.id
    }

    override fun addTodoList(todoList: TodoList): Int {
        todoList.id = lastTodoListId.incrementAndGet()
        todoLists.add(todoList)
        return todoList.id
    }

    override fun getTodoList(id: Int): TodoList {
        return todoLists.single { it.id == id }
    }

    override fun getTodoLists() = todoLists.toList()
    
    override fun getTodos() = todos.toList()

    override fun getTodos(listId: Int) = todos.filter { it.listId == listId }.toList()

    override fun changeTodoStatus(todoId: Int, done: Boolean) {
        todos.single { it.id == todoId }.done = done
    }

    override fun removeTodo(todoId: Int) {
        todos.removeIf { it.id == todoId }
    }

    override fun removeTodos(listId: Int) {
        todos.removeIf { it.listId == listId }
    }

    override fun removeList(listId: Int) {
        todoLists.removeIf { it.id == listId }
    }

}