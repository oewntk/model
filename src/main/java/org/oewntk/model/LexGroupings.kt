/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.model

import java.util.*
import java.util.stream.Collectors

object LexGroupings {

	/**
	 * Group lexes by CS lemma
	 *
	 * @param lexes lexes
	 * @return lexes grouped by CS lemma
	 */
	fun lexesByLemma(lexes: Collection<Lex>): Map<String, Collection<Lex>> {
		return lexes.groupBy(Lex::lemma)
			.mapValues { it.value.toSet() }
	}

	/**
	 * Group lexes by LC lemma
	 *
	 * @param lexes lexes
	 * @return lexes grouped by LCS lemma
	 */
	fun lexesByLCLemma(lexes: Collection<Lex>): Map<String, Collection<Lex>> {
		return lexes
			.groupBy { it.lCLemma }
			.mapValues { it.value.toSet() }
	}

	/**
	 * Hypermap (LCLemma -&gt; CSLemma -&gt; lexes)
	 *
	 * @param model model
	 * @return 2-tier hypermap (LCLemma -&gt; CSLemma -&gt; lexes)
	 */
	@JvmStatic
	fun hyperMapByLCLemmaByLemma(model: CoreModel): MutableMap<String, Map<String, Collection<Lex>>> {
		return model.lexesByLemma!!.entries.stream()
			.collect(
				Collectors.groupingBy(
					{ it.key.lowercase() },
					Collectors.toMap({ it.key }, { it.value })
				)
			)
	}

	/**
	 * CSLemmas grouped by LCLemma
	 * baroque -&gt; (Baroque, baroque)
	 *
	 * @param model model
	 * @return CS lemmas by LC lemmas
	 */
	@JvmStatic
	fun cSLemmasByLCLemma(model: CoreModel): Map<String, Set<String>> {
		return model.lexes
			.map(Lex::lemma)
			.groupBy { it.lowercase(Locale.ENGLISH) }
			.mapValues { it.value.toSortedSet() }
	}

	/**
	 * CSLemmas for LCLemma
	 *
	 * @param model   model
	 * @param lcLemma lower-cased lemma
	 * @return CS lemmas for given LC lemma
	 */
	fun cSLemmasForLCLemma(model: CoreModel, lcLemma: String): Set<String> {
		return cSLemmasByLCLemma(model)[lcLemma]!!
	}

	// counts

	/**
	 * Counts of CS lemmas by LC lemma
	 *
	 * @param model model
	 * @return counts of CS lemmas by LC lemmas
	 */
	@JvmStatic
	fun countsByLCLemma(model: CoreModel): Map<String, Long> {
		return model.lexes
			.map(Lex::lemma)
			.groupBy { it.lowercase(Locale.ENGLISH) }
			.toSortedMap(naturalOrder())
			.mapValues { it.value.toSet().size.toLong() }
	}

	/**
	 * Counts of CS lemmas by LC lemma, same as above but retain entries that have count &gt; 2
	 *
	 * @param model model
	 * @return counts of CS lemmas by LC lemmas, with count &gt; 2
	 */
	@JvmStatic
	fun multipleCountsByICLemma(model: CoreModel): Map<String, Long> {
		return model.lexes
			.map(Lex::lemma)
			.groupBy { it.lowercase(Locale.ENGLISH) }
			.mapValues { it.value.toSet().size.toLong() }
			.toList()
			.filter { it.second > 1L }
			.toMap()
			.toSortedMap(naturalOrder())
	}

	/**
	 * CS lemmas by LC lemmas, retain entries that have count &gt; 2
	 *
	 * @param model model
	 * @return CS lemmas by LC lemmas, with count &gt; 2
	 */
	@JvmStatic
	fun cSLemmasByLCLemmaHavingMultipleCount(model: CoreModel): Map<String, Set<String>> {
		return Groupings.groupByHavingMultipleCount(model.lexes.map(Lex::lemma)) { it.lowercase() }
	}
}
