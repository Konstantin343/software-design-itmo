package ru.kkrukhmalev.mvctodolist.logic

import ru.kkrukhmalev.mvctodolist.model.Todo
import ru.kkrukhmalev.mvctodolist.model.TodoList

object TodoManager {
    fun groupTodosInfoByLists(todoLists : List<TodoList>, todos: List<Todo>): Pair<Map<Int, Int>, Map<Int, Boolean>> {
        val todosByLists = todos.groupBy { it.listId }.toMutableMap()
        for (todoList in todoLists) {
            if (!todosByLists.containsKey(todoList.id)) {
                todosByLists[todoList.id] = listOf()
            }
        }
        val sizes = todosByLists.map { it.key to it.value.size }.toMap()
        val dones = todosByLists.map { it.key to (it.value.all { td -> td.done } && it.value.isNotEmpty()) }.toMap()
        return sizes to dones
    }
}