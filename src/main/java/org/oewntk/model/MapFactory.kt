/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.model

import java.util.*
import java.util.function.BinaryOperator
import java.util.function.Function
import java.util.function.Supplier
import java.util.stream.Collectors

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
	@JvmStatic
	fun <K, V> map(things: Collection<V>, groupingFunction: Function<V, K>?): Map<K, V> {
		val mergingFunction = KeepMergingSupplier<V>().get()
		return map(things, groupingFunction, mergingFunction)
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
	@JvmStatic
	fun <K, V> map(
		things: Collection<V>,
		groupingFunction: Function<V, K>?,
		mergingFunction: BinaryOperator<V>?
	): Map<K, V> {
		return things.stream() //
			.collect(Collectors.toMap(groupingFunction, Function.identity(), mergingFunction) { TreeMap() })
	}

	// G E N E R I C   M A P   F A C T O R Y

	/**
	 * Senses by id
	 *
	 * @param senses senses
	 * @return senses mapped by id
	 */
	@JvmStatic
	fun sensesById(senses: Collection<Sense>): Map<String, Sense> {
		val mergingFunction = BinaryOperator { existing: Sense, replacement: Sense ->
			val merged =
				if (replacement.lex.isCased) (if (existing.lex.isCased) existing else replacement) else existing
			if (existing == replacement) {
				if (LOG_DUPLICATE_VALUES) {
					Tracing.psInfo.printf(
						"[W] Duplicate values %s and %s, merging to %s%n",
						existing,
						replacement,
						merged
					)
				}
			}
			merged
		}
		return map(senses, { s: Sense -> s.senseKey }, mergingFunction)
	}

	/**
	 * Synsets by id
	 *
	 * @param synsets synsets
	 * @return synsets mapped by id
	 */
	@JvmStatic
	fun synsetsById(synsets: Collection<Synset>): Map<String, Synset> {
		val f = Function { s: Synset -> s.synsetId }
		return map(synsets, f)
	}

	// G E N E R I C   M A P   F A C T O R Y

	/**
	 * Supply 'keep' merging function
	 *
	 * @param <V> type of element
	</V> */
	private class KeepMergingSupplier<V> : Supplier<BinaryOperator<V>> {
		override fun get(): BinaryOperator<V> {
			return BinaryOperator { existing: V, replacement: V ->
				if (existing == replacement) {
					if (LOG_DUPLICATE_VALUES) {
						Tracing.psInfo.printf("[W] Duplicate values %s and %s, keeping first%n", existing, replacement)
					}
					//throw new IllegalArgumentException(existing + "," + replacement);
				}
				existing
			}
		}
	}

	/**
	 * Supply 'replace' merging function
	 *
	 * @param <V> type of element
	</V> */
	private class ReplaceMergingSupplier<V> : Supplier<BinaryOperator<V>> {
		override fun get(): BinaryOperator<V> {
			return BinaryOperator { existing: V, replacement: V ->
				if (existing == replacement) {
					if (LOG_DUPLICATE_VALUES) {
						Tracing.psInfo.printf(
							"[W] Duplicate values %s and %s, replacing first%n",
							existing,
							replacement
						)
					}
					//throw new IllegalArgumentException(existing + "," + replacement);
				}
				replacement
			}
		}
	}
}
