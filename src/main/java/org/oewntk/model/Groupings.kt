/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.model

/**
 * Generic grouping factory
 */
object Groupings {

    // counts

    /**
     * Group elements by key and yield count of each group
     *
     * @param collection       collection of elements
     * @param groupingFunction map element to key
     * @param K                type of key
     * @param V                type of element
     * @return count for each key/group
     */
    fun <K : Comparable<K>, V> countsBy(collection: Collection<V>, groupingFunction: (V) -> K): Map<K, Long> {
        return collection
            .groupBy(groupingFunction)
            .toSortedMap(naturalOrder())
            .mapValues { it.value.toSet().size.toLong() }
    }

    /**
     * Group elements by key and yield count of each group, retain only groups whose count &gt; 1
     *
     * @param collection       collection of elements
     * @param groupingFunction map element to key
     * @param K                type of key
     * @param V                type of element
     * @return count for each key/group
     */
    fun <K : Comparable<K>, V> multipleCountsBy(collection: Collection<V>, groupingFunction: (V) -> K): Map<K, Long> {
        return collection
            .groupBy(groupingFunction)
            .mapValues { it.value.toSet().size.toLong() }
            .toList()
            .filter { it.second > 1L }
            .toMap()
            .toSortedMap(naturalOrder())
    }

    // group by having

    /**
     * Group elements having
     *
     * @param collection       collection of elements
     * @param groupingFunction map element to key
     * @param predicate        having clause
     * @param K                grouping key
     * @param V                type of elements
     * @return list of elements grouped and mapped by key
     */
    private fun <K : Comparable<K>, V> groupByHaving(
        collection: Collection<V>,
        groupingFunction: (V) -> K,
        predicate: (Set<V>) -> Boolean,
    ): Map<K, Set<V>> {
        return collection
            .groupBy(groupingFunction)
            .mapValues { it.value.toSet() }
            .toList()
            .filterNot { predicate.invoke(it.second) }
            .toMap()
            .toSortedMap(naturalOrder())
    }

    /**
     * Group elements having group count &gt; 1
     *
     * @param collection       collection of elements
     * @param groupingFunction map element to key
     * @param K             grouping key
     * @param V             type of elements
     * @return list of elements grouped and mapped by key
     */
    fun <K : Comparable<K>, V> groupByHavingMultipleCount(collection: Collection<V>, groupingFunction: (V) -> K): Map<K, Set<V>> {
        return groupByHaving(collection, groupingFunction) { values -> values.size <= 1L }
    }
}
