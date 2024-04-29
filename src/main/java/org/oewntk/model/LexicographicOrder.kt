/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.model

/**
 * Lexicographic Order
 */
object LexicographicOrder {

    /**
     * Lower-case first
     */
    val lowerFirst: Comparator<String> = Comparator { s1: String, s2: String ->
        val c = s1.compareTo(s2, ignoreCase = true)
        if (c != 0) {
            return@Comparator c
        }
        -s1.compareTo(s2)
    }

    /**
     * Upper-case first
     */
    val upperFirst: Comparator<String> = Comparator { s1: String, s2: String ->
        val c = s1.compareTo(s2, ignoreCase = true)
        if (c != 0) {
            return@Comparator c
        }
        s1.compareTo(s2)
    }
}
