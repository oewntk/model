/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

/**
 * Generic grouping factory
 */
object Groupings {

    // group by having

    /**
     * Group elements having
     *
     * @param collection       collection of elements
     * @param groupingFunction map element to key
     * @param predicate        having clause : boolean function of a set of values
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
            .groupBy(groupingFunction) // mapOf(K, listOf(V1, V2, ..))
            .mapValues { it.value.toSet() } // mapOf(K, setOf(V1, V2, ..))
            .toList() // listOf(K to setOf(V1, V2, ..))
            .filterNot { predicate.invoke(it.second) } // filtered listOf(K to setOf(V1, V2, ..))
            .toMap() // mapOf(K to setOf(V1, V2, ..))
            .toSortedMap(naturalOrder()) // sortedMapOf(K to setOf(V1, V2, ..))
    }

    /**
     * Group elements having group count &gt; 1
     *
     * @param collection       collection of elements
     * @param groupingFunction map element to key
     * @param K                grouping key
     * @param V                type of elements
     * @return list of elements grouped and mapped by key
     */
    fun <K : Comparable<K>, V> groupByHavingMultipleCount(collection: Collection<V>, groupingFunction: (V) -> K): Map<K, Set<V>> {
        return groupByHaving(collection, groupingFunction) { values -> values.size <= 1L }
    }

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
            .groupBy(groupingFunction) // mapOf(K, listOf(V1, V2, ..))
            .toSortedMap(naturalOrder()) // sortedMapOf(K, listOf(V1, V2, ..))
            .mapValues { it.value.toSet().size.toLong() } // sortedMapOf(K, sizeOf(listOf(V1, V2, ..)))
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
            .groupBy(groupingFunction) // mapOf(K, listOf(V1, V2, ..))
            .mapValues { it.value.toSet().size.toLong() } // mapOf(K, setOf(V1, V2, ..).size)
            .toList() // listOf(K to size)
            .filter { it.second > 1L } // filtered listOf(K to size)
            .toMap() // mapOf(K to size)
            .toSortedMap(naturalOrder()) // sortedMapOf(K to size)
    }
}
