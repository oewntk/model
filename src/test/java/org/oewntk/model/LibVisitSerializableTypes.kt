/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

object LibVisitSerializableTypes {

    fun visit(m: Any): Any {
        return visitRecurse(m, 0)
    }

    private fun visitRecurse(item: Any?, level: Int): Any {
        return when (item) {
            is String -> {
                "string"
            }

            is Char -> {
                "char"
            }

            is List<*> -> {
                item
                    .map { visitRecurse(it, level + 1) }
                    .toList()
            }

            is Array<*> -> {
                item
                    .map { visitRecurse(it, level + 1) }
                    .toList()
            }

            is Set<*> -> {
                item
                    .map { visitRecurse(it, level + 1) }
                    .toList()
            }

            is Map<*, *> -> {
                item
                    .mapKeys {
                        if (it.key is String) it.key else throw IllegalArgumentException("Key it should be string")
                    }
                    .mapValues {
                        visitRecurse(it.value, level + 1)
                    }
            }

            else -> throw IllegalArgumentException("$item is of type ${item?.let { it::class.java.name }}")
        }
    }
}
