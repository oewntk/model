/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Sense
 */
public class Sense implements Comparable<Sense>, Serializable
{
	/**
	 * Sense id, sensekey
	 */
	private final String senseId;

	/**
	 * Lex this sense is contained in
	 */
	private final Lex lex;

	/**
	 * Synset type ss_type {n, v, a, r, s}
	 */
	private final char type;

	/**
	 * Synset type ss_type {n, v, a, r, s}
	 */
	private final char partOfSpeech;

	/**
	 * Index of this sense in lex list/array of senses
	 */
	private final int indexInLex;

	/**
	 * Synset id
	 */
	private final String synsetId;

	/**
	 * Examples
	 */
	private final String[] examples;

	/**
	 * Verb frames
	 */
	private final String[] verbFrames;

	/**
	 * Verb sentence templates
	 */
	private int[] verbTemplates;

	/**
	 * Adjective position {a, ip, p} meaning {attribute,immediate postnominal,predicate}
	 */
	private final String adjPosition;

	/**
	 * Tag count
	 */
	private TagCount tagCount;

	/**
	 * Sense relations
	 */
	private final Map<String, List<String>> relations;

	/**
	 * Constructor
	 *
	 * @param senseId     sense id / sensekey
	 * @param lex         lexical item
	 * @param type        {n,v,a,r,s}
	 * @param indexInLex  index of this sense in lex
	 * @param synsetId    synset id
	 * @param examples    examples
	 * @param verbFrames  verb frames
	 * @param adjPosition adjective position
	 * @param relations   sense relations
	 */
	public Sense(final String senseId, final Lex lex, final char type, final int indexInLex, final String synsetId, final String[] examples, final String[] verbFrames, final String adjPosition, final Map<String, List<String>> relations)
	{
		this.lex = lex;
		this.senseId = senseId;
		this.indexInLex = indexInLex;
		this.type = type;
		this.partOfSpeech = this.type == 's' ? 'a' : this.type;
		this.synsetId = synsetId;
		this.examples = examples;
		this.verbFrames = verbFrames;
		this.adjPosition = adjPosition;
		this.relations = relations;
	}

	/**
	 * Get sense id or sensekey
	 *
	 * @return sense id or sensekey
	 */
	public String getSenseId()
	{
		return senseId;
	}

	/**
	 * Get sensekey
	 *
	 * @return sensekey
	 */
	public String getSensekey()
	{
		return senseId;
	}

	/**
	 * Get lexical item
	 *
	 * @return lexical item
	 */
	public Lex getLex()
	{
		return lex;
	}

	/**
	 * Get lemma
	 *
	 * @return lemma
	 */
	public String getLemma()
	{
		return lex.getLemma();
	}

	/**
	 * Get lower-case lemma
	 *
	 * @return lower-case lemma
	 */
	public String getLCLemma()
	{
		return lex.getLCLemma();
	}

	/**
	 * Get type
	 *
	 * @return type
	 */
	public char getType()
	{
		return type;
	}

	/**
	 * Get part-of-speech
	 *
	 * @return part-of-speech
	 */
	public char getPartOfSpeech()
	{
		return partOfSpeech;
	}

	/**
	 * Get synset id
	 *
	 * @return synset id
	 */
	public String getSynsetId()
	{
		return synsetId;
	}

	/**
	 * Get lex senses index
	 *
	 * @return index of this sense in lex
	 */
	public int getLexIndex()
	{
		return indexInLex;
	}

	/**
	 * Get examples
	 *
	 * @return examples
	 */
	public String[] getExamples()
	{
		return examples;
	}

	/**
	 * Get verb frames
	 *
	 * @return verb frames
	 */
	public String[] getVerbFrames()
	{
		return verbFrames;
	}

	/**
	 * Get verb templates
	 *
	 * @return verb templates
	 */
	public int[] getVerbTemplates()
	{
		return verbTemplates;
	}

	/**
	 * Get adjective position (attribute, immediate post-nominal, predicate)
	 *
	 * @return adjective position (a|ip|p)
	 */
	public String getAdjPosition()
	{
		return adjPosition;
	}

	/**
	 * Get tag count
	 *
	 * @return tag count
	 */
	public TagCount getTagCount()
	{
		return tagCount;
	}

	/**
	 * Get int tag count
	 *
	 * @return tag count integer
	 */
	public int getIntTagCount()
	{
		return tagCount == null ? 0 : tagCount.getCount();
	}

	/**
	 * Get sense relations
	 *
	 * @return sense relations
	 */
	public Map<String, List<String>> getRelations()
	{
		return relations;
	}

	/**
	 * Get source file
	 *
	 * @return source file
	 */
	public String getSource()
	{
		return lex.getSource();
	}

	// non-final property delayed set

	/**
	 * Set verb templates
	 *
	 * @param verbTemplates verb templates
	 * @return this
	 */
	@SuppressWarnings("UnusedReturnValue")
	public Sense setVerbTemplates(final int[] verbTemplates)
	{
		this.verbTemplates = verbTemplates;
		return this;
	}

	/**
	 * Set tag count
	 *
	 * @param tagCount tag count
	 * @return this
	 */
	@SuppressWarnings("UnusedReturnValue")
	public Sense setTagCount(final TagCount tagCount)
	{
		this.tagCount = tagCount;
		return this;
	}

	// computed

	/**
	 * Find synset member index
	 *
	 * @param synsetsById synsets mapped by id
	 * @return index of this sense in synset members
	 */
	public int findSynsetIndex(Map<String, Synset> synsetsById)
	{
		Synset synset = synsetsById.get(getSynsetId());
		return synset.findIndexOfMember(getLex().getLemma());
	}

	/**
	 * Find lexid
	 * WNDB
	 * One digit hexadecimal integer that, when appended onto lemma , uniquely identifies a sense within a lexicographer file.
	 * lex_id numbers usually start with 0 , and are incremented as additional senses of the word are added to the same file,
	 * although there is no requirement that the numbers be consecutive or begin with 0 .
	 * Note that a value of 0 is the default, and therefore is not present in lexicographer files.
	 * synset = synset_offset  lex_filenum  ss_type  w_cnt  word  lex_id  [word  lex_id...] ...
	 * <p>
	 * SENSEIDX
	 * Two digit decimal integer that, when appended onto lemma , uniquely identifies a sense within a lexicographer file.
	 * lex_id numbers usually start with 00 , and are incremented as additional senses of the word are added to the same file,
	 * although there is no requirement that the numbers be consecutive or begin with 00 .
	 * Note that a value of 00 is the default, and therefore is not present in lexicographer files. (senseidx)
	 * sensekey = ss_type:lex_filenum:lex_id:head_word:head_id
	 * <p>
	 * SENSEKEY
	 * lemma % lex_sense
	 * where lex_sense is encoded as:
	 * ss_type:lex_filenum:lex_id:head_word:head_id
	 *
	 * @return lexid
	 */
	public int findLexid()
	{
		return Integer.parseInt(senseId.split("%")[1].split(":")[2]);
	}

	// stringify

	@Override
	public String toString()
	{
		return String.format(" %s (%dth of '%s', %s %s) ", getSenseId(), getLexIndex(), getLex().getLemma(), getSynsetId(), getType());
	}

	public String toLongString()
	{
		String relationsStr = Formatter.join(getRelations(), ",");
		return String.format("[%d] of '%s' %s %s %s {%s}", getLexIndex(), getLex().getLemma(), getSenseId(), getType(), getSynsetId(), relationsStr);
	}

	// identity

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		Sense sense = (Sense) o;
		return senseId.equals(sense.senseId);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(senseId);
	}

	// ordering

	@Override
	public int compareTo(final Sense that)
	{
		return getSensekey().compareTo(that.getSensekey());
	}
}
