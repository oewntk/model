/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.model

import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * Generic grouping factory
 */
object Groupings {

	// group by

	/**
	 * Group elements in collection
	 *
	 * @param things           collection of elements
	 * @param groupingFunction map element to key
	 * @param K             type of key
	 * @param V             type of element
	 * @return collections of elements grouped by and mapped by key
	 */
	fun <K, V> groupBy(things: Collection<V>, groupingFunction: (V) -> K): Map<K, Collection<V>> {
		return groupBy(things.stream(), groupingFunction)
	}

	/**
	 * Group elements in stream
	 *
	 * @param stream           stream of elements
	 * @param groupingFunction map element to key
	 * @param K             type of key
	 * @param V             type of element
	 * @return collections of elements grouped by and mapped by key
	 */
	fun <K, V> groupBy(stream: Stream<V>, groupingFunction: (V) -> K): Map<K, Collection<V>> {
		return stream
			.collect(Collectors.groupingBy(groupingFunction, Collectors.toCollection { HashSet() }))
	}

	// counts

	/**
	 * Group elements by key and yield count of each group
	 *
	 * @param stream           stream of elements
	 * @param groupingFunction map element to key
	 * @param K             type of key
	 * @param V             type of element
	 * @return count for each key/group
	 */
	fun <K, V> countsBy(stream: Stream<V>, groupingFunction: (V) -> K): Map<K, Long> {
		return stream
			.collect(
				Collectors.groupingBy(
					groupingFunction,
					{ TreeMap() },
					Collectors.counting()
				)
			)
	}

	/**
	 * Group elements by key and yield count of each group, retain only groups whose count &gt; 1
	 *
	 * @param stream           stream of elements
	 * @param groupingFunction map element to key
	 * @param K              type of key
	 * @param V              type of element
	 * @return count for each key/group
	 */
	fun <K, V> multipleCountsBy(stream: Stream<V>, groupingFunction: (V) -> K): Map<K, Long> {
		val c = Collectors.collectingAndThen(
			Collectors.groupingBy(
				groupingFunction,
				{ TreeMap() },
				Collectors.counting()
			)
		) {
			it.values.removeIf { it2 -> it2 <= 1L }
			it
		}
		return stream
			.collect(c)
	}

	// group by having

	/**
	 * Group elements having
	 *
	 * @param stream           stream of elements
	 * @param groupingFunction map element to key
	 * @param predicate        having clause
	 * @param K             grouping key
	 * @param V             type of elements
	 * @return list of elements grouped and mapped by key
	 */
	private fun <K, V> groupByHaving(
		stream: Stream<V>,
		groupingFunction: (V) -> K,
		predicate: Predicate<List<V>>?
	): Map<K, List<V>> {
		val c = Collectors.collectingAndThen(
			Collectors.groupingBy(
				groupingFunction,
				{ TreeMap() },
				Collectors.toList()
			)
		) {
			it.values.removeIf(predicate!!)
			it
		}
		return stream
			.collect(c)
	}

	/**
	 * Group elements having group count &gt; 1
	 *
	 * @param stream           stream of elements
	 * @param groupingFunction map element to key
	 * @param K             grouping key
	 * @param V             type of elements
	 * @return list of elements grouped and mapped by key
	 */
	fun <K, V> groupByHavingMultipleCount(stream: Stream<V>, groupingFunction: (V) -> K): Map<K, List<V>> {
		return groupByHaving(stream, groupingFunction) { values -> values.size <= 1L }
	}
}
