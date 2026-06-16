package org.oewntk.model

import java.util.HashSet

/**
 * Distinct or do
 * Keeps first, removes duplicates
 * Calls block when a duplicate is found
 *
 * @param onDuplicate block called when a duplicate is found
 */
inline fun <T> Iterable<T>.distinctOrDo(onDuplicate: (T) -> Unit): List<T> {
    val visited = HashSet<T>()
    return this.filter { element ->
        val isUnique = visited.add(element)
        if (!isUnique) {
            onDuplicate(element)
        }
        isUnique
    }
}
