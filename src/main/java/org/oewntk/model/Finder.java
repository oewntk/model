/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Lex finder
 */
public class Finder
{
	/**
	 * Find lex matching word
	 *
	 * @param model model
	 * @param lemma lemma (CS)
	 * @return stream of lexes
	 */
	public static Collection<Lex> getLexes(final CoreModel model, final String lemma)
	{
		return model.getLexesByLemma().get(lemma);
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
	 */
	public static <L extends Function<Lex, String>, P extends Function<Lex, Character>> Stream<Lex> getLexesHaving(final CoreModel model, final String word, final char posTypeFilter, final L wordExtractor, final P posTypeExtractor)
	{
		return model.lexes.stream() //
				.filter(lex -> word.equals(wordExtractor.apply(lex))) //
				.filter(lex -> posTypeFilter == posTypeExtractor.apply(lex));
	}

	/**
	 * Find lex matching word and type filter
	 *
	 * @param model      model
	 * @param lemma      lemma (CS)
	 * @param typeFilter type filter
	 * @return stream of lexes
	 */
	public static Stream<Lex> getLexesHavingType(final CoreModel model, final String lemma, final char typeFilter)
	{
		return model.getLexesByLemma().get(lemma) //
				.stream() //
				.filter(lex -> lex.getType() == typeFilter);
	}

	/**
	 * Find lex matching word and part-of-speech filter
	 *
	 * @param model     model
	 * @param lemma     lemma (cased)
	 * @param posFilter part-of-speech filter
	 * @return stream of lexes
	 */
	public static Stream<Lex> getLexesHavingPos(final CoreModel model, final String lemma, final char posFilter)
	{
		return model.getLexesByLemma().get(lemma) //
				.stream() //
				.filter(lex -> lex.getPartOfSpeech() == posFilter);
	}

	/**
	 * Find lexes matching lemma ignoring case and having the desired type
	 *
	 * @param model      model
	 * @param lcLemma    lower-cased lemma
	 * @param typeFilter type filter
	 * @return stream of lexes
	 */
	public static Stream<Lex> getLcLexesHavingType(final CoreModel model, final String lcLemma, final Character typeFilter)
	{
		return model.getLexesByLCLemma().get(lcLemma.toLowerCase(Locale.ENGLISH)).stream() //
				.filter(lex -> lex.getType() == typeFilter);
	}

	/**
	 * Find lexes matching lemma ignoring case and having the desired pos
	 *
	 * @param model     model
	 * @param lcLemma   lower-cased lemma
	 * @param posFilter pos filter
	 * @return stream of lexes
	 */
	public static Stream<Lex> getLcLexesHavingPos(final CoreModel model, final String lcLemma, final Character posFilter)
	{
		return model.getLexesByLCLemma().get(lcLemma.toLowerCase(Locale.ENGLISH)).stream() //
				.filter(lex -> lex.getPartOfSpeech() == posFilter);
	}

	/**
	 * Find lexes matching lemma ignoring case
	 *
	 * @param model   model
	 * @param lcLemma lower-cased lemma
	 * @return stream of lexes
	 */
	public static Stream<Lex> getLcLexes(final CoreModel model, final String lcLemma)
	{
		return model.getLexesByLCLemma().get(lcLemma.toLowerCase(Locale.ENGLISH)).stream();
	}

	/**
	 * Find lexes matching pronunciations
	 *
	 * @param lexes          lexes
	 * @param pronunciations pronunciations
	 * @return stream of lexes
	 * @throws IllegalArgumentException if not found
	 */
	public static Stream<Lex> getLexesHavingPronunciations(Stream<Lex> lexes, final Pronunciation[] pronunciations)
	{
		Set<Pronunciation> set = Utils.toSet(pronunciations);
		return lexes.filter(lex -> compareAsSets(set, lex.getPronunciations()));
	}

	/**
	 * Find lex matching discriminant
	 *
	 * @param lexes        lexes
	 * @param discriminant discriminant
	 * @return stream of lexes
	 * @throws IllegalArgumentException if not found
	 */
	public static Stream<Lex> getLexesHavingDiscriminant(final Stream<Lex> lexes, final String discriminant)
	{
		return lexes.filter(lex -> Objects.equals(lex.getDiscriminant(), discriminant));
	}

	// Set comparison

	/**
	 * Compare two arrays as sets
	 *
	 * @param array1 array 1
	 * @param array2 array 2
	 * @param <T>    type of element
	 * @return true if equals
	 */
	public static <T> boolean compareAsSets(T[] array1, T[] array2)
	{
		Set<T> set1 = Utils.toSet(array1);
		return compareAsSets(set1, array2);
	}

	/**
	 * Compare set to array as set
	 *
	 * @param set1   set
	 * @param array2 array
	 * @param <T>    type of element
	 * @return true if equals
	 */
	public static <T> boolean compareAsSets(Set<T> set1, T[] array2)
	{
		Set<T> set2 = Utils.toSet(array2);
		return Objects.equals(set1, set2);
	}
}
