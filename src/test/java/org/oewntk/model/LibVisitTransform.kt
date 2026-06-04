/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

object LibVisitTransform {

    fun visit(m: Any): Any {
        return visitRecurse(m, 0)
    }

    private fun visitRecurse(item: Any?, level: Int): Any {
        return when (item) {
            is String -> {
                "$item#"
            }

            is List<*> -> {
                item
                    .map { visitRecurse(it, level + 1) }
                    .toList()
            }

            is MutableMap<*, *> -> {
                item
                    .mapKeys {
                        visitRecurse(it.key, level + 1)
                    }
                    .mapValues {
                        visitRecurse(it.value, level + 1)
                    }
                    .apply {
                        val m: MutableMap<Any, Any> = (this as MutableMap<Any, Any>)
                        m["+x"] = "+y"
                    }
            }

            else -> throw IllegalArgumentException("$item is of type ${item?.let { it::class.java.name }}")
        }
    }
}
