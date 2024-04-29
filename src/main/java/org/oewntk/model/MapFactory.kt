/*
 * Copyright (c) 2021. Bernard Bou.
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
		groupingFunction: (V) -> K
	): Map<K, V> {
		return map(things, groupingFunction, keepMerging())
	}

	fun <K : Comparable<K>, V> mapAlt(
		things: Collection<V>,
		groupingFunction: (V) -> K
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
		mergingFunction: (V, V) -> V
	): Map<K, V> {
		return things
			.groupBy(groupingFunction)
			.mapValues { (_, values) -> values.reduce(mergingFunction) }
			.toSortedMap(naturalOrder())
	}

	// G E N E R I C   M A P   F A C T O R Y

	/**
	 * Senses by id
	 *
	 * @param senses senses
	 * @return senses mapped by id
	 */
	fun sensesById(senses: Collection<Sense>): Map<String, Sense> {
		val mergingFunction = { existing: Sense, replacement: Sense ->
			val merged = if (replacement.lex.isCased) (if (existing.lex.isCased) existing else replacement) else existing
			if (existing == replacement) {
				if (LOG_DUPLICATE_VALUES) {
					Tracing.psInfo.printf("[W] Duplicate values %s and %s, merging to %s%n", existing, replacement, merged)
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
		val f = { s: Synset -> s.synsetId }
		return map(synsets, f)
	}

	// G E N E R I C   M A P   F A C T O R Y

	/**
	 * Supply 'keep' merging function
	 *
	 * @param <V> type of element
	 */
	private fun <V> keepMerging() = { existing: V, replacement: V ->
		if (existing == replacement) {
			if (LOG_DUPLICATE_VALUES) {
				Tracing.psInfo.printf("[W] Duplicate values %s and %s, keeping first%n", existing, replacement)
			}
			//throw new IllegalArgumentException(existing + "," + replacement);
		}
		existing
	}

	/**
	 * Supply 'replace' merging function
	 *
	 * @param <V> type of element
	</V> */
	private fun <V> replaceMerging() = { existing: V, replacement: V ->
		if (existing == replacement) {
			if (LOG_DUPLICATE_VALUES) {
				Tracing.psInfo.printf("[W] Duplicate values %s and %s, replacing first%n", existing, replacement)
			}
			//throw new IllegalArgumentException(existing + "," + replacement);
		}
		replacement
	}
}
