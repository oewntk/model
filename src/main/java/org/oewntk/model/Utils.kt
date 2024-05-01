/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

object Utils {

    // Set

    /**
     * Array to set
     *
     * @param objects array of objects
     * @param T       type of objects
     * @return set of objects
     */
    fun <T> toSet(objects: Array<T>?): Set<T> {
        if (objects == null) {
            return setOf()
        }
        return mutableSetOf(*objects)
    }

    // Name for extractor

    private const val DUMMY_UPPER = "CASE"

    private const val DUMMY_SATELLITE = 's'

    private val dummyLex = Lex(DUMMY_UPPER, DUMMY_SATELLITE.toString(), null)

    /**
     * Name a word extractor (by applying dummy data)
     *
     * @param wordExtractor word extractor
     * @return name
     */
    fun toWordExtractorString(wordExtractor: (Lex) -> String): String {
        return if (wordExtractor.invoke(dummyLex) == DUMMY_UPPER) "cs" else "lc"
    }

    /**
     * Name a pos/type extractor (by applying dummy data)
     *
     * @param posTypeExtractor word extractor
     * @return name
     */
    fun toPosTypeExtractorString(posTypeExtractor: (Lex) -> Char): String {
        return if (posTypeExtractor.invoke(dummyLex) == DUMMY_SATELLITE) "t" else "p"
    }
}
