/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

object LibVisit {

    fun visitMap(m: MutableMap<String, Any>): Any {
        return visitMapRecurse(m, 0)
    }

    private fun visitMapRecurse(item: Any?, level: Int): Any {
        return when (item) {
            is String -> {
                "$item#"
            }

            is List<*> -> {
                item
                    .map {
                        visitMapRecurse(it, level + 1)
                    }
                    .toList()
            }

            is MutableMap<*, *> -> {
                item
                    .mapKeys {
                        visitMapRecurse(it.key, level + 1)
                    }
                    .mapValues {
                        visitMapRecurse(it.value, level + 1)
                    }
                    .apply {
                        val m: MutableMap<Any, Any> = (this as MutableMap<Any, Any>)
                        m["+x"] = "+y"
                    }
            }

            else -> throw IllegalArgumentException(item.toString())
        }
    }
}
