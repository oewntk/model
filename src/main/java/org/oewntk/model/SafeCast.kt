package org.oewntk.model

/**
 * Avoid warning
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> safeCast(value: Any): T {
    return value as T
}

/**
 * Avoid warning
 */
@Suppress("UNCHECKED_CAST")
fun <T> safeNullableCast(value: Any?): T? {
    return value as T
}
