/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */
package org.oewntk.model

/**
 * Formatter to joined representation of items
 */
object Formatter {

    /**
     * Join array of items
     *
     * @param T   type of item
     * @param items array of items of type T
     * @param delim delimiter
     * @return joined string representation of items
     */
    fun <T> join(items: Array<T>?, delim: String?): String {
        if (items == null) {
            return ""
        }
        val sb = StringBuilder()
        var first = true
        for (item in items) {
            if (first) {
                first = false
            } else {
                sb.append(delim)
            }
            sb.append(item.toString())
        }
        return sb.toString()
    }

    /**
     * Join iteration of items
     *
     * @param T   type of item
     * @param items array of items of type T
     * @param delim delimiter
     * @return joined string representation of items
     */
    fun <T> join(items: Iterable<T>?, delim: String?): String {
        if (items == null) {
            return ""
        }
        val sb = StringBuilder()
        var first = true
        for (item in items) {
            if (first) {
                first = false
            } else {
                sb.append(delim)
            }
            sb.append(item.toString())
        }
        return sb.toString()
    }

    /**
     * Join items in multimap
     *
     * @param K   type of key
     * @param V   type of value
     * @param T   type of item
     * @param map   map of lists of items of type V mapped by K
     * @param delim delimiter
     * @return joined string representation of items
     */
    fun <K, V : Iterable<T>?, T> join(map: Map<K, V>?, delim: String?): String {
        if (map == null) {
            return ""
        }
        val sb = StringBuilder()
        var first = true
        for ((key, v) in map) {
            if (first) {
                first = false
            } else {
                sb.append(delim)
            }
            val k = key.toString()
            sb.append('[')
            sb.append(k)
            sb.append(']')
            sb.append('=')
            sb.append(join(v, ","))
        }
        return sb.toString()
    }

    /**
     * Join array of integers
     *
     * @param items  array of integers
     * @param delim  delimiter
     * @param format format
     * @return joined string representation of each item
     */
    fun join(items: IntArray?, delim: Char, format: String?): String {
        if (items == null) {
            return ""
        }
        val sb = StringBuilder()
        var first = true
        for (item in items) {
            if (first) {
                first = false
            } else {
                sb.append(delim)
            }
            sb.append(String.format(format!!, item))
        }
        return sb.toString()
    }
}
