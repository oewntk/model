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
	 * Format for count output
	 */
	private static final String countFormat = "%n%-50s: %6d";

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
		//Tracing.psInfo.print(reportRelations());
		/*int addedSynsetRelations =*/
		InverseRelationFactory.makeSynsetRelations(getSynsetsById());
		//Tracing.psInfo.printf(countFormat, "added inverse relations", addedSynsetRelations);
		/*int addedSenseRelations =*/
		InverseRelationFactory.makeSenseRelations(getSensesById());
		//Tracing.psInfo.printf(countFormat, "added inverse relations", addedSenseRelations);
		//Tracing.psInfo.println(reportRelations());
		return this;
	}

	/**
	 * Cached
	 */
	private transient Map<String, Collection<Lex>> lexesByLemma = null;

	/**
	 * Lexical units mapped by lemma written form.
	 * A multimap: each value is an array of lexes for the lemma.
	 *
	 * @return lexes mapped by lemma written form
	 */
	public Map<String, Collection<Lex>> getLexesByLemma()
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
	private transient Map<String, Collection<Lex>> lexesByLCLemma = null;

	/**
	 * Lexical units mapped by lemma lower-cased written form.
	 * A multimap: each value is an array of lexes for the lemma.
	 *
	 * @return lexes mapped by lemma written form
	 */
	public Map<String, Collection<Lex>> getLexesByLCLemma()
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
	private transient Map<String, Sense> sensesById = null;

	/**
	 * Senses mapped by id (sensekey)
	 *
	 * @return senses mapped by id (sensekey)
	 */
	public Map<String, Sense> getSensesById()
	{
		if (sensesById == null)
		{
			sensesById = MapFactory.sensesById(senses);
		}
		return sensesById;
	}

	/**
	 * Cached
	 */
	private transient Map<String, Synset> synsetsById = null;

	/**
	 * Synsets mapped by id (synset id)
	 *
	 * @return synsets mapped by id (synset id)
	 */
	public Map<String, Synset> getSynsetsById()
	{
		if (synsetsById == null)
		{
			synsetsById = MapFactory.synsetsById(synsets);
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
		long casedCount = lexes.stream().map(Lex::getLemma).filter(lemma -> !lemma.equals(lemma.toLowerCase())).distinct().count();

		long distinctByKeyOEWNLexCount = lexes.stream().map(Key.W_P_A::of_t).distinct().count();
		long distinctByKeyShallowLexCount = lexes.stream().map(Key.W_P_D::of_t).distinct().count();
		long distinctByKeyPOSLexCount = lexes.stream().map(Key.W_P_A::of_p).distinct().count();
		long distinctByKeyICLexCount = lexes.stream().map(Key.W_P_A::of_lc_t).distinct().count();
		long distinctByKeyPWNLexCount = lexes.stream().map(Key.W_P::of_lc_p).distinct().count();
		long distinctSenseGroupsCount = lexes.stream().map(Lex::getSensesAsSet).distinct().count();
		long sensesInSenseGroupsSum = lexes.stream().map(Lex::getSensesAsSet).distinct().mapToLong(Set::size).sum();

		long withMultiSenseLexCount = lexes.stream().filter(lex -> lex.getSenses().size() > 1).count();
		long discriminantCount = lexes.stream().map(Lex::getDiscriminant).filter(Objects::nonNull).distinct().count();
		long withDiscriminantLexCount = lexes.stream().filter(lex -> lex.getDiscriminant() != null).count();
		long withPronunciationLexCount = lexes.stream().filter(lex -> lex.getPronunciations() != null).count();

		long withRelationSenseCount = senses.stream().filter(sense -> sense.getRelations() != null).count();
		long senseRelationSum = senses.stream().map(Sense::getRelations).filter(Objects::nonNull).flatMap(m -> m.values().stream()).mapToLong(Set::size).sum();

		long withRelationSynsetCount = synsets.stream().filter(synset -> synset.getRelations() != null).count();
		long synsetRelationSum = synsets.stream().map(Synset::getRelations).filter(Objects::nonNull).flatMap(m -> m.values().stream()).mapToLong(Set::size).sum();

		return String.format(countFormat, "lexes", lexes.size()) + //
				String.format(countFormat, "lemmas (distinct CS)", csWordCount) + //
				String.format(countFormat, "lemmas (distinct LC)", lcWordCount) + //
				String.format(countFormat, "lemmas (cased)", casedCount) + //

				String.format(countFormat, "discriminant types", discriminantCount) + //
				String.format(countFormat, "lexes with discriminant", withDiscriminantLexCount) + //
				String.format(countFormat, "lexes with pronunciation", withPronunciationLexCount) + //
				String.format(countFormat, "lexes with multi senses", withMultiSenseLexCount) + //

				String.format(countFormat, "distinct lexes by key W_P_A_type (deep)", distinctByKeyOEWNLexCount) + //
				String.format(countFormat, "distinct lexes by key W_P_D_type (shallow)", distinctByKeyShallowLexCount) + //
				String.format(countFormat, "distinct lexes by key W_P_A_pos (pos)", distinctByKeyPOSLexCount) + //
				String.format(countFormat, "distinct lexes by key W_P_A_lc_type (ic)", distinctByKeyICLexCount) + //
				String.format(countFormat, "distinct lexes by key W_P_lc_pos (pwn)", distinctByKeyPWNLexCount) + //

				String.format(countFormat, "senses", senses.size()) + //
				String.format(countFormat, "distinct sense sets in lexes", distinctSenseGroupsCount) + //
				String.format(countFormat, "senses in sense sets", sensesInSenseGroupsSum) + //
				String.format(countFormat, "senses with relations", withRelationSenseCount) + //
				String.format(countFormat, "sense relations", senseRelationSum) + //

				String.format(countFormat, "synsets", synsets.size()) + //
				String.format(countFormat, "synsets with relations", withRelationSynsetCount) +  //
				String.format(countFormat, "synset relations", synsetRelationSum);
	}

	/**
	 * Computed count about this model
	 *
	 * @return info
	 */
	public String xCounts()
	{
		counts();

		long pronunciationRefSum = lexes.stream().filter(lex -> lex.getPronunciations() != null).mapToLong(lex -> lex.getPronunciations().length).sum();
		long pronunciationCount = lexes.stream().filter(lex -> lex.getPronunciations() != null).flatMap(lex -> Arrays.stream(lex.getPronunciations())).distinct().count();
		long withMorphLexCount = lexes.stream().filter(lex -> lex.getForms() != null).count();
		long morphRefSum = lexes.stream().filter(lex -> lex.getForms() != null).mapToLong(lex -> lex.getForms().length).sum();
		long morphCount = lexes.stream().filter(lex -> lex.getForms() != null).flatMap(lex -> Arrays.stream(lex.getForms())).distinct().count();

		long withExamplesSenseCount = senses.stream().filter(sense -> sense.getExamples() != null).count();
		long withVerbFramesSenseCount = senses.stream().filter(sense -> sense.getVerbFrames() != null).count();
		long withVerbTemplatesSenseCount = senses.stream().filter(sense -> sense.getVerbTemplates() != null).count();
		long withTagCountSenseCount = senses.stream().filter(sense -> sense.getTagCount() != null).count();

		long withSamplesSynsetCount = synsets.stream().filter(synset -> synset.getExamples() != null).count();
		long sampleSum = synsets.stream().filter(synset -> synset.getExamples() != null).mapToLong(synset -> synset.getExamples().length).sum();

		return String.format(countFormat, "lexes with morphs", withMorphLexCount) + //

				String.format(countFormat, "senses with verb frames", withVerbFramesSenseCount) + //
				String.format(countFormat, "senses with verb templates", withVerbTemplatesSenseCount) + //
				String.format(countFormat, "senses with tag count", withTagCountSenseCount) + //
				String.format(countFormat, "senses with examples", withExamplesSenseCount) + //

				String.format(countFormat, "synsets with examples", withSamplesSynsetCount) + //
				String.format(countFormat, "synset examples", sampleSum) + //

				String.format(countFormat, "pronunciations", pronunciationCount) + //
				String.format(countFormat, "pronunciation references", pronunciationRefSum) + //
				String.format(countFormat, "morphs", morphCount) + //
				String.format(countFormat, "morph references", morphRefSum) //
				;
	}

	private String reportRelations()
	{
		long[] acc = {0, 0};
		synsets.forEach(synset -> {
			var rr = synset.getRelations();
			if (rr != null && !rr.isEmpty())
			{
				rr.forEach((r, v) -> acc[0] += v.size());
			}
		});
		senses.forEach(sense -> {
			var rr = sense.getRelations();
			if (rr != null && !rr.isEmpty())
			{
				rr.forEach((r, v) -> acc[1] += v.size());
			}
		});
		long synsetRelationSum = synsets.stream().map(Synset::getRelations).filter(Objects::nonNull) //
				.flatMap(m -> m.values().stream()).mapToLong(Set::size).sum();
		long senseRelationSum = senses.stream().map(Sense::getRelations).filter(Objects::nonNull) //
				.flatMap(m -> m.values().stream()).mapToLong(Set::size).sum();
		assert synsetRelationSum == acc[0] : String.format("synset relations %d %d discrepancy", synsetRelationSum, acc[0]);
		assert senseRelationSum == acc[1] : String.format("sense relations %d %d discrepancy", senseRelationSum, acc[1]);
		return String.format(countFormat, "synset relations", synsetRelationSum) + String.format(countFormat, "sense relations", senseRelationSum);
	}
}
