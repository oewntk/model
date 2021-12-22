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
	 * @param model model
	 * @param lemma lemma (CS)
	 * @return lexes
	 */
	public static Collection<Lex> getLexes(final CoreModel model, final String lemma)
	{
		return model.getLexesByLemma().get(lemma);
	}

	/**
	 * Find lex matching word and type filter
	 *
	 * @param model      model
	 * @param lemma      lemma (CS)
	 * @param typeFilter type filter
	 * @return lexes
	 */
	public static Lex[] getLexesHavingType(final CoreModel model, final String lemma, final char typeFilter)
	{
		return model.getLexesByLemma().get(lemma) //
				.stream().filter(lex -> lex.getType() == typeFilter) //
				.toArray(Lex[]::new);
	}

	/**
	 * Find lex matching word and part-of-speech filter
	 *
	 * @param model     model
	 * @param lemma     lemma (cased)
	 * @param posFilter part-of-speech filter
	 * @return lexes
	 */
	public static Lex[] getLexesHavingPos(final CoreModel model, final String lemma, final char posFilter)
	{
		return model.getLexesByLemma().get(lemma) //
				.stream().filter(lex -> lex.getPartOfSpeech() == posFilter) //
				.toArray(Lex[]::new);
	}

	/**
	 * Find lexes matching lemma ignoring case and having the desired type
	 *
	 * @param model      model
	 * @param lcLemma    lower-cased lemma
	 * @param typeFilter type filter
	 * @return array of lexes
	 */
	public static Lex[] getLcLexesHavingType(final CoreModel model, final String lcLemma, final Character typeFilter)
	{
		return model.getLexesByLCLemma().get(lcLemma.toLowerCase(Locale.ENGLISH)).stream() //
				.filter(lex -> lex.getType() == typeFilter) //
				.toArray(Lex[]::new);
	}

	/**
	 * Find lexes matching lemma ignoring case and having the desired pos
	 *
	 * @param model      model
	 * @param lcLemma      lower-cased lemma
	 * @param posFilter    pos filter
	 * @return array of lexes
	 */
	public static Lex[] getLcLexesHavingPos(final CoreModel model, final String lcLemma, final Character posFilter)
	{
		return model.getLexesByLCLemma().get(lcLemma.toLowerCase(Locale.ENGLISH)).stream() //
				.filter(lex -> lex.getPartOfSpeech() == posFilter) //
				.toArray(Lex[]::new);
	}

	/**
	 * Find lexes matching lemma ignoring case
	 *
	 * @param model      model
	 * @param lcLemma      lower-cased lemma
	 * @return array of lexes
	 */
	public static Lex[] getLcLexes(final CoreModel model, final String lcLemma)
	{
		return model.getLexesByLCLemma().get(lcLemma.toLowerCase(Locale.ENGLISH))
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
		Set<Pronunciation> set = Utils.toSet(pronunciations);
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
		Set<Pronunciation> set = Utils.toSet(pronunciations);
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
		Set<T> set1 = Utils.toSet(array1);
		return compareAsSets(set1, array2);
	}

	public static <T> boolean compareAsSets(Set<T> set1, T[] array2)
	{
		Set<T> set2 = Utils.toSet(array2);
		return Objects.equals(set1, set2);
	}
}
