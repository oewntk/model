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
     * @param entrySeparator entry separator
     * @param valueSeparator value separator
     * @param valuePrefix value prefix
     * @param valuePostfix value postfix
     * @return joined string representation of items:
     * ```
     * [key1]={value11,value12,...}
     * [key2]={value21,value22,...}
     *
     * ```
     */
    fun <K, V : Iterable<T>?, T> Map<K, V>?.joinToString(entrySeparator: CharSequence = " ", valueSeparator: CharSequence = ",", valuePrefix: CharSequence = "", valuePostfix: CharSequence = ""): String {
        return this?.entries!!.joinToString(separator = entrySeparator) { "[${it.key}]=${it.value?.joinToString(separator = valueSeparator, prefix = valuePrefix, postfix = valuePostfix) ?: ""}" }
    }
}
