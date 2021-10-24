package ru.kkrukhmalev.mvctodolist.dao

import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.support.JdbcDaoSupport
import ru.kkrukhmalev.mvctodolist.model.Todo
import ru.kkrukhmalev.mvctodolist.model.TodoList
import javax.sql.DataSource

class TodoJdbcDao(dataSource: DataSource): TodoDao, JdbcDaoSupport() {
    
    init {
        setDataSource(dataSource)
    }
    
    override fun addTodo(todo: Todo): Int {
        val sql = "INSERT INTO TODOS (listId, title, description, done) VALUES (?, ?, ?, ?)"
        return jdbcTemplate!!.update(sql, todo.listId, todo.title, todo.description, todo.done)
    }

    override fun addTodoList(todoList: TodoList): Int {
        val sql = "INSERT INTO TODOLISTS (title, description) VALUES (?, ?)"
        return jdbcTemplate!!.update(sql, todoList.title, todoList.description)
    }

    override fun getTodoList(id: Int): TodoList {
        val sql = "SELECT * FROM TODOLISTS WHERE id = $id"
        return jdbcTemplate!!.query(sql, BeanPropertyRowMapper(TodoList::class.java)).single()
    }

    override fun getTodoLists(): List<TodoList> {
        val sql = "SELECT * FROM TODOLISTS"
        return jdbcTemplate!!.query(sql, BeanPropertyRowMapper(TodoList::class.java))
    }

    override fun getTodos(): List<Todo> {
        val sql = "SELECT * FROM TODOS"
        return jdbcTemplate!!.query(sql, BeanPropertyRowMapper(Todo::class.java))
    }

    override fun getTodos(listId: Int): List<Todo> {
        val sql = "SELECT * FROM TODOS WHERE listId = $listId"
        return jdbcTemplate!!.query(sql, BeanPropertyRowMapper(Todo::class.java))
    }

    override fun changeTodoStatus(todoId: Int, done: Boolean) {
        val sql = "UPDATE TODOS SET done = ? WHERE id = ?"
        jdbcTemplate!!.update(sql, if (done) 1 else 0, todoId)
    }

    override fun removeTodo(todoId: Int) {
        val sql = "DELETE FROM TODOS WHERE id = ?"
        jdbcTemplate!!.update(sql, todoId)
    }

    override fun removeTodos(listId: Int) {
        val sql = "DELETE FROM TODOS WHERE listId = ?"
        jdbcTemplate!!.update(sql, listId)
    }

    override fun removeList(listId: Int) {
        val sql = "DELETE FROM TODOLISTS WHERE id = ?"
        jdbcTemplate!!.update(sql, listId)
    }
}