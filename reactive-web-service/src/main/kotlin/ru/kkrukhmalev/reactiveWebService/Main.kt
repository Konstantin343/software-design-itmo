package ru.kkrukhmalev.reactiveWebService

fun main() =
    ProductsWebService(
        8080,
        "localhost",
        27017,
        "products-db"
    ).start()
