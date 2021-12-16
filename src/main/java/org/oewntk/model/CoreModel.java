/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.File;
import java.io.Serializable;
import java.util.*;

/**
 * Base language model
 */
public class CoreModel implements Serializable
{
	/**
	 * Lexical items
	 */
	public final Collection<Lex> lexes;

	/**
	 * Senses
	 */
	public final Collection<Sense> senses;

	/**
	 * Synsets
	 */
	public final Collection<Synset> synsets;

	/**
	 * Input directory
	 */
	protected File source;

	/**
	 * Constructor
	 *
	 * @param lexes   lexes
	 * @param senses  senses
	 * @param synsets synsets
	 */
	public CoreModel( //
			final Collection<Lex> lexes, //
			final Collection<Sense> senses, //
			final Collection<Synset> synsets)
	{
		this.lexes = Collections.unmodifiableCollection(lexes);
		this.senses = Collections.unmodifiableCollection(senses);
		this.synsets = Collections.unmodifiableCollection(synsets);
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
		InverseRelationFactory.make(getSynsetsById());
		return this;
	}

	/**
	 * Cached
	 */
	private volatile Map<String, List<Lex>> lexesByLemma = null;

	/**
	 * Lexical units mapped by lemma written form.
	 * A multimap: each value is an array of lexes for the lemma.
	 *
	 * @return lexes mapped by lemma written form
	 */
	public Map<String, List<Lex>> getLexesByLemma()
	{
		if (lexesByLemma == null)
		{
			lexesByLemma = LexGroupings.lexesByLemma(lexes);
		}
		return lexesByLemma;
	}

	/**
	 * Cached
	 */
	private volatile Map<String, List<Lex>> lexesByLCLemma = null;

	/**
	 * Lexical units mapped by lemma lower-cased written form.
	 * A multimap: each value is an array of lexes for the lemma.
	 *
	 * @return lexes mapped by lemma written form
	 */
	public Map<String, List<Lex>> getLexesByLCLemma()
	{
		if (lexesByLCLemma == null)
		{
			lexesByLCLemma = LexGroupings.lexesByLCLemma(lexes);
		}
		return lexesByLCLemma;
	}

	/**
	 * Cached
	 */
	private volatile Map<String, Sense> sensesById = null;

	/**
	 * Senses mapped by id (sensekey)
	 *
	 * @return senses mapped by id (sensekey)
	 */
	public Map<String, Sense> getSensesById()
	{
		if (sensesById == null)
		{
			sensesById = Mapper.sensesById(senses);
		}

		return Mapper.sensesById(senses);
	}

	/**
	 * Cached
	 */
	private volatile Map<String, Synset> synsetsById = null;

	/**
	 * Synsets mapped by id (synset id)
	 *
	 * @return synsets mapped by id (synset id)
	 */
	public Map<String, Synset> getSynsetsById()
	{
		if (synsetsById == null)
		{
			synsetsById = Mapper.synsetsById(synsets);
		}
		return synsetsById;
	}

	/**
	 * Info about this model
	 *
	 * @return info
	 */
	public String info()
	{
		return String.format("casedwords: %d, senses: %d, synsets: %s", lexes.size(), senses.size(), synsets.size());
	}

	/**
	 * Computed count about this model
	 *
	 * @return info
	 */
	public String counts()
	{
		long lexesCount = lexes.size();
		long casedWordsCount = lexes.stream().map(Lex::getLemma).count();
		long wordsCount = lexes.stream().map(Lex::getLCLemma).distinct().count();
		long discriminantsCount = lexes.stream().map(Lex::getDiscriminant).filter(Objects::nonNull).distinct().count();
		long discriminantUsesCount = lexes.stream().filter(lex -> lex.getDiscriminant() != null).count();
		return String.format("lexes: %d, CSwords: %d, ICwords: %d, discriminants: %d discriminant uses: %d", lexesCount, casedWordsCount, wordsCount, discriminantsCount, discriminantUsesCount);
	}
}
