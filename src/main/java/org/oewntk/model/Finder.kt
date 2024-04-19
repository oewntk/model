/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.Utils.toSet
import java.util.stream.Stream

/**
 * Lex finder
 */
object Finder {

	/**
	 * Find lex matching word
	 *
	 * @param model model
	 * @param lemma lemma (CS)
	 * @return stream of lexes
	 */
	@JvmStatic
	fun getLexes(model: CoreModel, lemma: String?): Collection<Lex> {
		return model.lexesByLemma!![lemma!!]!!
	}

	/**
	 * Find lex matching word and type filter
	 *
	 * @param model            model
	 * @param word             word
	 * @param posTypeFilter    posType filter
	 * @param wordExtractor    word extractor
	 * @param posTypeExtractor posType extractor
	 * @param <L>              lemma extractor type
	 * @param <P>              posType extractor type
	 * @return stream of lexes
	</P></L> */
	fun getLexesHaving(
		model: CoreModel,
		word: String,
		posTypeFilter: Char,
		wordExtractor: (Lex) -> String,
		posTypeExtractor: (Lex) -> Char
	): Stream<Lex> {
		return model.lexes.stream()
			.filter { lex: Lex -> word == wordExtractor.invoke(lex) }
			.filter { lex: Lex -> posTypeFilter == posTypeExtractor.invoke(lex) }
	}

	/**
	 * Find lex matching word and type filter
	 *
	 * @param model      model
	 * @param lemma      lemma (CS)
	 * @param typeFilter type filter
	 * @return stream of lexes
	 */
	fun getLexesHavingType(model: CoreModel, lemma: String, typeFilter: Char): Stream<Lex>? {
		return model.lexesByLemma!![lemma]?.stream()?.filter { lex: Lex -> lex.type == typeFilter }
	}

	/**
	 * Find lex matching word and part-of-speech filter
	 *
	 * @param model     model
	 * @param lemma     lemma (cased)
	 * @param posFilter part-of-speech filter
	 * @return stream of lexes
	 */
	@JvmStatic
	fun getLexesHavingPos(model: CoreModel, lemma: String, posFilter: Char): Stream<Lex>? {
		return model.lexesByLemma!![lemma]?.stream()?.filter { lex: Lex -> lex.partOfSpeech == posFilter }
	}

	/**
	 * Find lexes matching lemma ignoring case and having the desired type
	 *
	 * @param model      model
	 * @param lcLemma    lower-cased lemma
	 * @param typeFilter type filter
	 * @return stream of lexes
	 */
	fun getLcLexesHavingType(model: CoreModel, lcLemma: String, typeFilter: Char): Stream<Lex> {
		return model.lexesByLCLemma!![lcLemma.lowercase()]!!.stream() 
			.filter { lex: Lex -> lex.type == typeFilter }
	}

	/**
	 * Find lexes matching lemma ignoring case and having the desired pos
	 *
	 * @param model     model
	 * @param lcLemma   lower-cased lemma
	 * @param posFilter pos filter
	 * @return stream of lexes
	 */
	fun getLcLexesHavingPos(model: CoreModel, lcLemma: String, posFilter: Char): Stream<Lex> {
		return model.lexesByLCLemma!![lcLemma.lowercase()]!!.stream()
			.filter { lex: Lex -> lex.partOfSpeech == posFilter }
	}

	/**
	 * Find lexes matching lemma ignoring case
	 *
	 * @param model   model
	 * @param lcLemma lower-cased lemma
	 * @return stream of lexes
	 */
	@JvmStatic
	fun getLcLexes(model: CoreModel, lcLemma: String): Stream<Lex> {
		return model.lexesByLCLemma!![lcLemma.lowercase()]!!.stream()
	}

	/**
	 * Find lexes matching pronunciations
	 *
	 * @param lexes          lexes
	 * @param pronunciations pronunciations
	 * @return stream of lexes
	 * @throws IllegalArgumentException if not found
	 */
	fun getLexesHavingPronunciations(lexes: Stream<Lex>, pronunciations: Array<Pronunciation>?): Stream<Lex> {
		val set = toSet(pronunciations)
		return lexes.filter { lex: Lex -> compareAsSets(set, lex.pronunciations) }
	}

	/**
	 * Find lex matching discriminant
	 *
	 * @param lexes        lexes
	 * @param discriminant discriminant
	 * @return stream of lexes
	 * @throws IllegalArgumentException if not found
	 */
	fun getLexesHavingDiscriminant(lexes: Stream<Lex>, discriminant: String?): Stream<Lex> {
		return lexes.filter { lex: Lex -> lex.discriminant == discriminant }
	}

	// Set comparison
	/**
	 * Compare two arrays as sets
	 *
	 * @param array1 array 1
	 * @param array2 array 2
	 * @param <T>    type of element
	 * @return true if equals
	</T> */
	fun <T> compareAsSets(array1: Array<T>?, array2: Array<T>?): Boolean {
		val set1 = toSet(array1)
		return compareAsSets(set1, array2)
	}

	/**
	 * Compare set to array as set
	 *
	 * @param set1   set
	 * @param array2 array
	 * @param <T>    type of element
	 * @return true if equals
	</T> */
	private fun <T> compareAsSets(set1: Set<T>, array2: Array<T>?): Boolean {
		val set2 = toSet(array2)
		return set1 == set2
	}
}
