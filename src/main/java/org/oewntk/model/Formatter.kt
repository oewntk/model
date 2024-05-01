/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

/**
 * Formatter to joined representation of items
 */
object Formatter {

    /**
     * Join items in multimap
     *
     * @param K  type of key
     * @param V  type of value
     * @param T  type of item
     * @param separator separator
     * @return joined string representation of items:
     * ```
     * [key1]={value11,value12,...}separator
     * [key2]={value21,value22,...}separator
     *
     * ```
     */
    fun <K, V : Iterable<T>?, T> Map<K, V>?.joinToString(separator: String): String {
        return this?.entries!!.joinToString(separator) { "[${it.key}]=${it.value?.joinToString(separator = ",", prefix = "{", postfix = "}") ?: ""}" }
    }
}
