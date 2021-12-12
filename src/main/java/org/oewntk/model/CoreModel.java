/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Base language model
 */
public class CoreModel implements Serializable
{
	/**
	 * Lexical items mapped by lemma written form.
	 * A multimap: each value is an array of lexes for the lemma.
	 */
	public final Map<String, List<Lex>> lexesByLemma;

	/**
	 * Senses mapped by id (sensekey)
	 */
	public final Map<String, Sense> sensesById;

	/**
	 * Synsets mapped by id (synset id)
	 */
	public final Map<String, Synset> synsetsById;

	/**
	 * Input directory
	 */
	protected File source;

	/**
	 * Constructor
	 *
	 * @param lexesByLemma lexes
	 * @param sensesById   senses
	 * @param synsetsById  synsets
	 */
	public CoreModel( //
			final Map<String, List<Lex>> lexesByLemma, //
			final Map<String, Sense> sensesById, //
			final Map<String, Synset> synsetsById)
	{
		this.lexesByLemma = lexesByLemma;
		this.sensesById = sensesById;
		this.synsetsById = synsetsById;
	}

	/**
	 * Stream of lexes
	 *
	 * @return stream of lexes
	 */
	public Stream<Lex> getStreamOfLexes()
	{
		return lexesByLemma.entrySet() //
				.stream() //
				.flatMap(e -> e.getValue().stream()); //
	}

	/**
	 * Record input directories
	 *
	 * @param source source
	 * @return this
	 */
	public CoreModel setSource(final File source)
	{
		this.source = source;
		return this;
	}

	/**
	 * Get source
	 *
	 * @return source
	 */
	public File getSource()
	{
		return source;
	}

	/**
	 * Generate inverse relations
	 *
	 * @return this model
	 */
	public CoreModel generateInverseRelations()
	{
		InverseRelationFactory.make(synsetsById);
		return this;
	}

	/**
	 * Info about this model
	 *
	 * @return info
	 */
	public String info()
	{
		return String.format("casedwords: %d, senses: %d, synsets: %s", lexesByLemma.size(), sensesById.size(), synsetsById.size());
	}

	/**
	 * Computed count about this model
	 *
	 * @return info
	 */
	public String counts()
	{
		long lexesCount = lexesByLemma.values().stream().mapToLong(List::size).sum();
		//noinspection ReplaceInefficientStreamCount
		long casedWordsCount = lexesByLemma.keySet().stream().count();
		long wordsCount = lexesByLemma.keySet().stream().map(String::toLowerCase).distinct().count();
		long wordsCountEnglish = lexesByLemma.keySet().stream().map(s -> s.toLowerCase(Locale.ENGLISH)).distinct().count();
		long discriminantsCount = lexesByLemma.values().stream().flatMap(List::stream).map(Lex::getDiscriminant).filter(Objects::nonNull).distinct().count();
		long discriminantUsesCount = lexesByLemma.values().stream().flatMap(List::stream).filter(lex -> lex.getDiscriminant() != null).count();
		return String.format("lexes: %d, casedwords: %d, words: %d(default lowercase), words(English lowercase): %d, discriminants: %d discriminant uses: %d", lexesCount, casedWordsCount, wordsCount, wordsCountEnglish, discriminantsCount, discriminantUsesCount);
	}
}
