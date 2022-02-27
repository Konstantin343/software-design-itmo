package ru.kkrukhmalev.reactiveWebService.validator

import org.bson.Document

class BodyValidator(private val fields: Map<String, Regex?>) {
    fun validate(document: Document): String? {
        if (document.keys.size != fields.size || !document.keys.containsAll(fields.keys)) {
            return "Wrong fields: [${document.keys.joinToString()}], expected: [${fields.keys.joinToString()}]"
        }
        for ((field, regex) in fields) {
            if (regex != null && !regex.matches(document[field].toString())) {
                return "Wrong value '${document[field]}' for field '$field', expected to match: '$regex'"
            }
        }
        return null
    }
}