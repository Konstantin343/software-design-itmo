package ru.kkrukhmalev.mvctodolist.model

@Suppress("unused")
class Todo {
    var id: Int = 0
    var listId: Int = 0
    var title: String = ""
    var description: String = ""
    var done: Boolean = false

    constructor()

    constructor(id: Int, listId: Int, title: String, description: String, done: Boolean) {
        this.id = id
        this.listId = listId
        this.title = title
        this.description = description
        this.done = done
    }
}
