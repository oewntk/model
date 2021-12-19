/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Lexical item/unit/entry
 */
public class Lex implements Comparable<Lex>, Serializable
{
	/**
	 * Source file
	 */
	private final String source;

	/**
	 * Lemma written form
	 */
	private final String lemma;

	/**
	 * Synset type ss_type {n, v, a, r, s}
	 */
	private final char type;

	/**
	 * Part-of-speech
	 */
	private final char partOfSpeech;

	/**
	 * Discriminant appended to type that distinguishes same-type lexes (because of pronunciation or morphological forms )
	 * Current values are '-1','-2'
	 */
	private final String discriminant;

	/**
	 * Pronunciations
	 */
	private Pronunciation[] pronunciations;

	/**
	 * Morphological forms
	 */
	private String[] forms;

	/**
	 * Senses
	 */
	private final List<Sense> senses = new ArrayList<>();

	/**
	 * Constructor
	 *
	 * @param lemma  lemma written form
	 * @param code   {n,v,a,r,s]+discriminant, current values are a,n,n-1,n-2,r,s,s-1,s-2,v,v-1,v-2
	 * @param source source file
	 */
	public Lex(final String lemma, final String code, final String source)
	{
		this.lemma = lemma;
		this.type = code.charAt(0);
		this.partOfSpeech = this.type == 's' ? 'a' : this.type;
		this.discriminant = code.length() > 1 ? code.substring(1) : null;
		this.source = source;
	}

	/**
	 * Get first key item (=lemma)
	 *
	 * @return lex's first item of the keys
	 */
	public String getKey1()
	{
		return lemma;
	}

	/**
	 * Get second key item (=lemma)
	 *
	 * @return lex's second item of the keys
	 */
	public String getKey2()
	{
		return type + discriminant;
	}

	/**
	 * Get lower-cased lemma written form
	 *
	 * @return lower-cased lemma written form
	 */
	public String getLemma()
	{
		return lemma;
	}

	/**
	 * Get lemma written form
	 *
	 * @return lemma written form
	 */
	public String getLCLemma()
	{
		return lemma.toLowerCase(Locale.ENGLISH);
	}

	/**
	 * Get lemma written form
	 *
	 * @return lemma written form
	 */
	public boolean isCased()
	{
		return !lemma.equals(getLCLemma());
	}

	/**
	 * Get type
	 *
	 * @return n, v, a, r, s
	 */
	public char getType()
	{
		return type;
	}

	/**
	 * Get part-of-speech (sme as part-of-speech except for satellite adjective)
	 *
	 * @return n, v, a, r
	 */
	public char getPartOfSpeech()
	{
		return partOfSpeech;
	}

	/**
	 * Get discriminant
	 *
	 * @return discriminant amongst same-type lexes (num)
	 */
	public String getDiscriminant()
	{
		return discriminant;
	}

	/**
	 * Get pronunciations
	 *
	 * @return pronunciations
	 */
	public Pronunciation[] getPronunciations()
	{
		return pronunciations;
	}

	/**
	 * Get morphological forms
	 *
	 * @return morphological forms
	 */
	public String[] getForms()
	{
		return forms;
	}

	// non-final property delayed set

	/**
	 * Get senses
	 *
	 * @return senses
	 */
	public List<Sense> getSenses()
	{
		return senses;
	}

	/**
	 * Set senses
	 *
	 * @param senses senses
	 */
	public void setSenses(final Sense[] senses)
	{
		this.senses.addAll(List.of(senses));
	}

	public void addSense(final Sense sense)
	{
		this.senses.add(sense);
	}

	/**
	 * Set pronunciations
	 *
	 * @param pronunciations pronunciations
	 * @return this
	 */
	@SuppressWarnings("UnusedReturnValue")
	public Lex setPronunciations(final Pronunciation... pronunciations)
	{
		this.pronunciations = pronunciations;
		return this;
	}

	/**
	 * Set morphological forms
	 *
	 * @param forms morphological forms
	 * @return this
	 */
	@SuppressWarnings("UnusedReturnValue")
	public Lex setMorphs(final String... forms)
	{
		this.forms = forms;
		return this;
	}

	/**
	 * Get source file
	 *
	 * @return source file
	 */
	public String getSource()
	{
		return source;
	}

	// stringify

	@Override
	public String toString()
	{
		String pronunciationsStr = Formatter.join(getPronunciations(), ",");
		String sensesStr = Formatter.join(getSenses(), ",");
		return String.format("%s %c%s %s {%s}", getLemma(), getType(), discriminant == null ? "" : discriminant, pronunciationsStr, sensesStr);
	}

	@Override
	public int compareTo(final Lex otherLex)
	{
		int lemmaComp = getLemma().compareTo(otherLex.getLemma());
		if (lemmaComp != 0)
		{
			return lemmaComp;
		}
		int typeComp = Character.compare(getType(), otherLex.getType());
		if (typeComp != 0)
		{
			return lemmaComp;
		}
		String discriminant = getDiscriminant();
		String otherDiscriminant = otherLex.getDiscriminant();
		if (discriminant == null && otherDiscriminant == null)
		{
			return 0;
		}
		if (discriminant == null)
		{
			return -2;
		}
		return discriminant.compareTo(otherDiscriminant);
	}
}
