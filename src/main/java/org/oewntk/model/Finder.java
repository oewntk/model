/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.util.*;

public class Finder
{
	/**
	 * Find lex matching word
	 *
	 * @param lexesByLemma lexes mapped by lemma as per model
	 * @param lemma        lemma (cased)
	 * @return lexes
	 */
	public static List<Lex> getLexes(Map<String, List<Lex>> lexesByLemma, String lemma)
	{
		return lexesByLemma.get(lemma);
	}

	/**
	 * Find lex matching word and type filter
	 *
	 * @param lexesByLemma lexes mapped by lemma as per model
	 * @param lemma        lemma (cased)
	 * @param typeFilter   type filter
	 * @return lexes
	 */
	public static Lex[] getLexesHavingType(Map<String, List<Lex>> lexesByLemma, String lemma, char typeFilter)
	{
		return lexesByLemma.get(lemma) //
				.stream()
				.filter(lex -> lex.getType() == typeFilter) //
				.toArray(Lex[]::new);
	}

	/**
	 * Find lex matching word and part-of-speech filter
	 *
	 * @param lexesByLemma lexes mapped by lemma as per model
	 * @param lemma        lemma (cased)
	 * @param posFilter    part-of-speech filter
	 * @return lexes
	 */
	public static Lex[] getLexesHavingPos(Map<String, List<Lex>> lexesByLemma, String lemma, char posFilter)
	{
		return lexesByLemma.get(lemma) //
				.stream()
				.filter(lex -> lex.getPartOfSpeech() == posFilter) //
				.toArray(Lex[]::new);
	}

	/**
	 * Find lexes matching lemma ignoring case and having the desired type
	 *
	 * @param lexesByLemma lexes by lemma
	 * @param lcLemma      lower-cased lemma
	 * @param typeFilter   type filter
	 * @return array of lexes
	 */
	public static Lex[] getLcLexesHavingType(final Map<String, List<Lex>> lexesByLemma, final String lcLemma, final Character typeFilter)
	{
		return Arrays.stream(getLcLexes(lexesByLemma, lcLemma)) //
				.filter(lex -> lex.getType() == typeFilter) //
				.toArray(Lex[]::new);
	}

	/**
	 * Find lexes matching lemma ignoring case and having the desired pos
	 *
	 * @param lexesByLemma lexes by lemma
	 * @param lcLemma      lower-cased lemma
	 * @param posFilter    pos filter
	 * @return array of lexes
	 */
	public static Lex[] getLcLexesHavingPos(final Map<String, List<Lex>> lexesByLemma, final String lcLemma, final Character posFilter)
	{
		return Arrays.stream(getLcLexes(lexesByLemma, lcLemma)) //
				.filter(lex -> lex.getPartOfSpeech() == posFilter) //
				.toArray(Lex[]::new);
	}

	/**
	 * Find lexes matching lemma ignoring case
	 *
	 * @param lexesByLemma lexes by lemma
	 * @param lcLemma      lower-cased lemma
	 * @return array of lexes
	 */
	public static Lex[] getLcLexes(final Map<String, List<Lex>> lexesByLemma, final String lcLemma)
	{
		return lexesByLemma.keySet() //
				.stream() //
				.filter(lemma -> lemma.equalsIgnoreCase(lcLemma))//
				.map(lexesByLemma::get) //
				.flatMap(List::stream) //
				.toArray(Lex[]::new);
	}

	/**
	 * Find lex matching pronunciations
	 *
	 * @param lexes          lexes
	 * @param pronunciations pronunciations
	 * @return lex
	 * @throws IllegalArgumentException if not found
	 */
	public static Lex getLexHavingPronunciations(Lex[] lexes, final Pronunciation[] pronunciations)
	{
		Set<Pronunciation> set = toSet(pronunciations);
		return Arrays.stream(lexes) //
				.filter(lex -> compareAsSets(set, lex.getPronunciations())) //
				.findFirst().orElseThrow(IllegalArgumentException::new);
	}

	/**
	 * Find lexes matching pronunciations
	 *
	 * @param lexes          lexes
	 * @param pronunciations pronunciations
	 * @return lex
	 * @throws IllegalArgumentException if not found
	 */
	public static Lex[] getLexesHavingPronunciations(final Lex[] lexes, final Pronunciation[] pronunciations)
	{
		Set<Pronunciation> set = toSet(pronunciations);
		return Arrays.stream(lexes) //
				.filter(lex -> compareAsSets(set, lex.getPronunciations())) //
				.toArray(Lex[]::new);
	}

	/**
	 * Find lex matching discriminant
	 *
	 * @param lexes        lexes
	 * @param discriminant discriminant
	 * @return lex
	 * @throws IllegalArgumentException if not found
	 */
	public static Lex getLexHavingDiscriminant(final Lex[] lexes, final String discriminant)
	{
		return Arrays.stream(lexes) //
				.filter(lex -> Objects.equals(lex.getDiscriminant(), discriminant)) //
				.findFirst().orElseThrow(IllegalArgumentException::new);
	}

	// Set comparison

	public static <T> boolean compareAsSets(T[] array1, T[] array2)
	{
		Set<T> set1 = toSet(array1);
		return compareAsSets(set1, array2);
	}

	public static <T> boolean compareAsSets(Set<T> set1, T[] array2)
	{
		Set<T> set2 = toSet(array2);
		return Objects.equals(set1, set2);
	}

	public static <T> Set<T> toSet(final T[] objects)
	{
		if (objects == null)
		{
			return Set.of();
		}
		return Set.of(objects);
	}
}
