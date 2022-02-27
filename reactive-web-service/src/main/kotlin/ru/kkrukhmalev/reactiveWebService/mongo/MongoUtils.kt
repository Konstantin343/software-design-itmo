package ru.kkrukhmalev.reactiveWebService.mongo

import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.MongoCollection
import com.mongodb.rx.client.MongoDatabase
import org.bson.Document

fun mongoDb(host: String, port: Int, dbName: String, setUp: MongoDatabase.() -> Unit = {}): MongoDatabase =
    MongoClients.create("mongodb://$host:$port").getDatabase(dbName).apply(setUp)

fun MongoDatabase.collection(name: String, build: MongoCollection<Document>.() -> Unit = {}) {
    createCollection(name)
    getCollection(name).build()
}

fun MongoCollection<Document>.unique(vararg fields: String) {
    createIndex(
        Indexes.ascending(*fields),
        IndexOptions().unique(true)
    ).subscribe {
        println(it)
    }
}