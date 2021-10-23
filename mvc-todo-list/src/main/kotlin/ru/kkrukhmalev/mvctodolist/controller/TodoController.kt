package ru.kkrukhmalev.mvctodolist.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import ru.kkrukhmalev.mvctodolist.dao.TodoDao
import ru.kkrukhmalev.mvctodolist.model.Todo
import ru.kkrukhmalev.mvctodolist.model.TodoList

@Controller
class TodoController(private var todoDao: TodoDao) {

    @GetMapping("/")
    fun index() = "redirect:/todo-lists"

    @GetMapping("/todo-lists")
    fun todoLists(map: ModelMap): String {
        val todoLists = todoDao.getTodoLists()
        val todos = todoLists.associate { it.id to todoDao.getTodos(it.id) }
        map.addAttribute("todoLists", todoLists)
        map.addAttribute("sizes", todos.map { it.key to it.value.size }.toMap())
        map.addAttribute("dones",
            todos.map { it.key to (it.value.all { td -> td.done } && it.value.isNotEmpty()) }.toMap())
        map.addAttribute("todoList", TodoList())
        return "index"
    }

    @GetMapping("/todo-list")
    fun todoList(@RequestParam listId: Int, map: ModelMap): String {
        map.addAttribute("todos", todoDao.getTodos(listId))
        map.addAttribute("todoList", todoDao.getTodoList(listId))
        map.addAttribute("todo", Todo())
        return "todo-list"
    }

    @PostMapping("/add-todo-list")
    fun addTodoList(@ModelAttribute("todoList") todoList: TodoList): String {
        todoDao.addTodoList(todoList)
        return "redirect:/todo-lists"
    }

    @PostMapping("/add-todo")
    fun addTodo(@ModelAttribute("todo") todo: Todo): String {
        todoDao.addTodo(todo)
        return "redirect:/todo-list?listId=${todo.listId}"
    }

    @PostMapping("/delete-todo-list")
    fun deleteTodoList(@ModelAttribute("id") id: Int): String {
        todoDao.removeList(id)
        return "redirect:/todo-lists"
    }

    @PostMapping("/delete-todo")
    fun deleteTodo(@ModelAttribute("id") id: Int, @ModelAttribute("listId") listId: Int): String {
        todoDao.removeTodo(id)
        return "redirect:/todo-list?listId=${listId}"
    }

    @PostMapping("/todo-done")
    fun todoDone(
        @ModelAttribute("listId") listId: Int,
        @ModelAttribute("id") id: Int,
        @ModelAttribute("done") done: Boolean,
    ): String {
        todoDao.changeTodoStatus(id, done)
        return "redirect:/todo-list?listId=${listId}"
    }
}