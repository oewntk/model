/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.Serializable;
import java.util.*;

/**
 * Lexical item/unit/entry
 * The basic container of senses.
 * Can be thought of as athe pair of a key and value (k, senses).
 * The value is the set of senses while the key is made up of member elements, depending on the key.
 */
public class Lex implements Serializable //, Comparable<Lex>
{
	// lemma

	/**
	 * Lemma written form
	 */
	private final String lemma;

	// par-of-speech / synset type

	/**
	 * Synset type ss_type {n, v, a, r, s}
	 */
	private final char type;

	/**
	 * Part-of-speech
	 */
	private final char partOfSpeech;

	// other

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

	// senses, the value

	/**
	 * Senses
	 */
	private final List<Sense> senses = new ArrayList<>();

	/**
	 * Source file
	 */
	private final String source;

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
	 * Get senses as a set
	 *
	 * @return senses
	 */
	public Set<Sense> getSensesAsSet()
	{
		return new LinkedHashSet<>(senses);
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

	/**
	 * Add sense to the list fo senses
	 *
	 * @param sense sense
	 */
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
	public Lex setForms(final String... forms)
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

	// identify

	@Override
	public boolean equals(final Object o)
	{
		throw new UnsupportedOperationException("Either compare values using 'getSensesAsSet' or keys using 'Key.OEWN.of', 'Key.PWN.of', etc");
	}

	@Override
	public int hashCode()
	{
		int result = Objects.hash(lemma, type, discriminant);
		result = 31 * result + Arrays.hashCode(pronunciations);
		return result;
	}

	// ordering, this is used when sorted maps are made

	static public final Comparator<Lex> comparatorByKeyOEWN = (thisLex, thatLex) -> {

		var thisKey = Key.W_P_A.of_t(thisLex);
		var thatKey = Key.W_P_A.of_t(thatLex);
		return thisKey.compareTo(thatKey);
	};
}
