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
		return String.format("lexes: %d, senses: %d, synsets: %s", lexes.size(), senses.size(), synsets.size());
	}

	/**
	 * Computed count about this model
	 *
	 * @return info
	 */
	public String counts()
	{
		long csWordCount = lexes.stream().map(Lex::getLemma).distinct().count();
		long lcWordCount = lexes.stream().map(Lex::getLCLemma).distinct().count();
		long casedCount = lexes.stream().filter(Lex::isCased).distinct().count();
		long withMultiSenseLexCount = lexes.stream().filter(lex -> lex.getSenses().size() > 1).count();
		long discriminantCount = lexes.stream().map(Lex::getDiscriminant).filter(Objects::nonNull).distinct().count();
		long withDiscriminantLexCount = lexes.stream().filter(lex -> lex.getDiscriminant() != null).count();
		long withPronunciationLexCount = lexes.stream().filter(lex -> lex.getPronunciations() != null).count();
		long pronunciationRefCount = lexes.stream().filter(lex -> lex.getPronunciations() != null).mapToLong(lex -> lex.getPronunciations().length).sum();
		long pronunciationCount = lexes.stream().filter(lex -> lex.getPronunciations() != null).flatMap(lex -> Arrays.stream(lex.getPronunciations())).distinct().count();
		long withMorphLexCount = lexes.stream().filter(lex -> lex.getForms() != null).count();
		long morphRefCount = lexes.stream().filter(lex -> lex.getForms() != null).mapToLong(lex -> lex.getForms().length).sum();
		long morphCount = lexes.stream().filter(lex -> lex.getForms() != null).flatMap(lex -> Arrays.stream(lex.getForms())).distinct().count();
		long senseCount = lexes.stream().mapToLong(lex -> lex.getSenses().size()).sum();

		long withExamplesSenseCount = senses.stream().filter(sense -> sense.getExamples() != null).count();
		long withVerbFramesSenseCount = senses.stream().filter(sense -> sense.getVerbFrames() != null).count();
		long withVerbTemplatesSenseCount = senses.stream().filter(sense -> sense.getVerbTemplates() != null).count();
		long withTagCountSenseCount = senses.stream().filter(sense -> sense.getTagCount() != null).count();
		long withRelationSenseCount = senses.stream().filter(sense -> sense.getRelations() != null).count();
		long senseExampleCount = senses.stream().filter(sense -> sense.getExamples() != null).distinct().mapToLong(sense -> sense.getExamples().length).sum();
		long senseRelationCount = senses.stream().filter(sense -> sense.getRelations() != null).distinct().mapToLong(sense -> sense.getRelations().size()).sum();

		long withSamplesSynsetCount = synsets.stream().filter(synset -> synset.getExamples() != null).count();
		long sampleCount = synsets.stream().filter(synset -> synset.getExamples() != null).mapToLong(synset -> synset.getExamples().length).sum();
		long withRelationSynsetCount = synsets.stream().filter(synset -> synset.getRelations() != null).count();
		long synsetRelationCount = synsets.stream().filter(synset -> synset.getRelations() != null).distinct().mapToLong(synset -> synset.getRelations().size()).sum();

		String format = "%n%-32s: %6d";
		return String.format(format, "lexes", lexes.size()) + //
				String.format(format, "lemmas(CS)", csWordCount) + //
				String.format(format, "lemmas(LC)", lcWordCount) + //
				String.format(format, "lemmas(cased)", casedCount) + //
				String.format(format, "discriminant types", discriminantCount) + //
				String.format(format, "lexes with discriminant.", withDiscriminantLexCount) + //
				String.format(format, "lexes with pronunciation", withPronunciationLexCount) + //
				String.format(format, "lexes with multi senses", withMultiSenseLexCount) + //
				String.format(format, "lexes with morphs", withMorphLexCount) + //

				String.format(format, "senses", senses.size()) + //
				String.format(format, "senses", senseCount) + //
				String.format(format, "senses with relations", withRelationSenseCount) + //
				String.format(format, "sense relations", senseRelationCount) + //
				String.format(format, "senses with verb frames", withVerbFramesSenseCount) + //
				String.format(format, "senses with verb templates", withVerbTemplatesSenseCount) + //
				String.format(format, "senses with tag count", withTagCountSenseCount) + //
				String.format(format, "senses with examples", withExamplesSenseCount) + //
				String.format(format, "sense examples", senseExampleCount) + //

				String.format(format, "synsets", synsets.size()) + //
				String.format(format, "synsets with relations", withRelationSynsetCount) +  //
				String.format(format, "synset relations", synsetRelationCount) + //
				String.format(format, "synsets with examples", withSamplesSynsetCount) + //
				String.format(format, "synset examples", sampleCount) + //

				String.format(format, "pronunciations", pronunciationCount) + //
				String.format(format, "pronunciation references", pronunciationRefCount) + //
				String.format(format, "morphs", morphCount) + //
				String.format(format, "morph references", morphRefCount) //
				;
	}
}
