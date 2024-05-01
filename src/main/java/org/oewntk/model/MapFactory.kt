/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

/**
 * Map factory
 */
object MapFactory {

    private const val LOG_DUPLICATE_VALUES = false

    /**
     * Make map
     *
     * @param things           elements
     * @param groupingFunction map element to key
     * @param K                type of key
     * @param V                type of element
     * @return elements mapped by key
     */
    fun <K : Comparable<K>, V> map(
        things: Collection<V>,
        groupingFunction: (V) -> K,
    ): Map<K, V> {
        return map(things, groupingFunction, keepMerging())
    }

    fun <K : Comparable<K>, V> mapAlt(
        things: Collection<V>,
        groupingFunction: (V) -> K,
    ): Map<K, V> {
        return things
            .associateBy { groupingFunction(it) }
            .toSortedMap(naturalOrder())
    }

    /**
     * Make map
     *
     * @param things           elements
     * @param groupingFunction map element to key
     * @param mergingFunction  merging function for existing and replacement elements
     * @param K                type of key
     * @param V                type of element
     * @return elements mapped by key
     */
    fun <K : Comparable<K>, V> map(
        things: Collection<V>,
        groupingFunction: (V) -> K,
        mergingFunction: (V, V) -> V,
    ): Map<K, V> {
        return things
            .groupBy(groupingFunction)
            .mapValues { (_, values) -> values.reduce(mergingFunction) }
            .toSortedMap(naturalOrder())
    }

    // G E N E R I C   M E R G I N G   F U N C T I O N S

    /**
     * Supply 'keep' merging function
     *
     * @param V type of element
     */
    private fun <V> keepMerging() = { existing: V, replacement: V ->
        if (existing == replacement) {
            if (LOG_DUPLICATE_VALUES) {
                Tracing.psInfo.println("[W] Duplicate values $existing and $replacement, keeping first")
            }
            //throw new IllegalArgumentException(existing + "," + replacement);
        }
        existing
    }

    /**
     * Supply 'replace' merging function
     *
     * @param V type of element
     */
    private fun <V> replaceMerging() = { existing: V, replacement: V ->
        if (existing == replacement) {
            if (LOG_DUPLICATE_VALUES) {
                Tracing.psInfo.println("[W] Duplicate values $existing and $replacement, replacing first")
            }
            //throw new IllegalArgumentException(existing + "," + replacement);
        }
        replacement
    }

    // S E N S E   A N D   S Y N S E T  M A P   F A C T O R I E S

    /**
     * Senses by id
     *
     * @param senses senses
     * @return senses mapped by id
     */
    fun sensesById(senses: Collection<Sense>): Map<String, Sense> {
        // prioritize cased
        val mergingFunction = { existing: Sense, replacement: Sense ->
            val merged = if (replacement.lex.isCased) (if (existing.lex.isCased) existing else replacement) else existing
            if (existing == replacement) {
                if (LOG_DUPLICATE_VALUES) {
                    Tracing.psInfo.println("[W] Duplicate values $existing and $replacement, merging to $merged")
                }
            }
            merged
        }
        return map(senses, { it.senseKey }, mergingFunction)
    }

    /**
     * Synsets by id
     *
     * @param synsets synsets
     * @return synsets mapped by id
     */
    fun synsetsById(synsets: Collection<Synset>): Map<String, Synset> {
        return map(synsets) { it.synsetId }
    }
}
