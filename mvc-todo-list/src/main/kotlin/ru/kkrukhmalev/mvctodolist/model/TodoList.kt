package ru.kkrukhmalev.mvctodolist.model

@Suppress("unused")
class TodoList {
    var id: Int = 0
    var title: String = ""
    var description: String = ""
    
    constructor()

    constructor(id: Int, title: String, description: String) {
        this.description = description
        this.id = id
        this.title = title
    }
}