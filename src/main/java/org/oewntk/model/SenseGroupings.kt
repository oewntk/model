/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.util.*;
import java.util.function.Function;

/**
 * Sense grouping
 */
public class SenseGroupings
{
	// L O W E R - C A S E   A N D   P O S   K E Y   ( P W N )

	/**
	 * Key that matches how indexes are built in PWN (index.sense and index.noun|verb|adj|adv
	 */
	static public class KeyLCLemmaAndPos implements Comparable<KeyLCLemmaAndPos>
	{
		public final String lcLemma;

		public final char pos;

		public KeyLCLemmaAndPos(final Sense sense)
		{
			this(sense.getLemma(), sense.getPartOfSpeech());
		}

		public KeyLCLemmaAndPos(final String lemma, final char pos)
		{
			this.lcLemma = lemma.toLowerCase(Locale.ENGLISH);
			this.pos = pos;
		}

		public static KeyLCLemmaAndPos of(final String lcLemma, final char pos)
		{
			return new KeyLCLemmaAndPos(lcLemma, pos);
		}

		public static KeyLCLemmaAndPos of(final Sense sense)
		{
			return new KeyLCLemmaAndPos(sense);
		}

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
			KeyLCLemmaAndPos that = (KeyLCLemmaAndPos) o;
			return pos == that.pos && lcLemma.equals(that.lcLemma);
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(lcLemma, pos);
		}

		@Override
		public int compareTo(final KeyLCLemmaAndPos that)
		{
			int cmp = lcLemma.compareTo(that.lcLemma);
			if (cmp != 0)
			{
				return cmp;
			}
			return Character.compare(pos, that.pos);
		}

		@Override
		public String toString()
		{
			return "'" + lcLemma + "'-" + pos;
		}
	}

	// S E N S E   M A P S

	/**
	 * Senses grouped and mapped by lower-cased lemma and part-of-speech
	 *
	 * @param senses senses
	 * @return collections of senses grouped by and mapped by lower-cased lemma and part-of-speech
	 */
	public static Map<KeyLCLemmaAndPos, Collection<Sense>> sensesByLCLemmaAndPos(final Collection<Sense> senses)
	{
		return Groupings.groupBy(senses.stream(), KeyLCLemmaAndPos::new);
	}

	/**
	 * Senses grouped and mapped by lower-cased lemma
	 *
	 * @param senses senses
	 * @return collections of senses grouped by and mapped by lower-cased lemma
	 */
	public static Map<String, Collection<Sense>> sensesByLCLemma(final Collection<Sense> senses)
	{
		return Groupings.groupBy(senses.stream(), Sense::getLCLemma);
	}

	// S E N S E S  F O R
	// for debug as it makes a fresh map every time

	/**
	 * Find senses matching key built from sense
	 *
	 * @param senses           senses
	 * @param groupingFunction map sense to key
	 * @param k                key
	 * @param <K>              type of key
	 * @return senses matching this key
	 */
	public static <K> Collection<Sense> sensesFor(final Collection<Sense> senses, final Function<Sense, K> groupingFunction, K k)
	{
		return Groupings.groupBy(senses.stream(), groupingFunction).get(k);
	}

	/**
	 * Find senses for target lower-cased lemma and part-of-speech
	 *
	 * @param senses  senses
	 * @param lcLemma target lower-cased lemma
	 * @param pos     target part-of-speech
	 * @return collection of senses for this target lower-cased lemma and part-of-speech
	 */
	public static Collection<Sense> sensesForLCLemmaAndPos(final Collection<Sense> senses, final String lcLemma, final char pos)
	{
		return sensesFor(senses, KeyLCLemmaAndPos::new, KeyLCLemmaAndPos.of(lcLemma, pos));
	}

	/**
	 * Find senses for target lower-cased lemma
	 *
	 * @param senses  senses
	 * @param lcLemma target lower-cased lemma
	 * @return collection of senses for this target lower-cased lemma
	 */
	public static Collection<Sense> sensesForLCLemma(final Collection<Sense> senses, final String lcLemma)
	{
		return sensesFor(senses, Sense::getLCLemma, lcLemma);
	}

	// C O M P A R A T O R

	/**
	 * Order senses by decreasing frequency order, does not define a total order, must be chained with thenComparing to define a total order, returns 0 if tag counts cannot make one more or less frequent
	 */
	public static final Comparator<Sense> BY_DECREASING_TAGCOUNT = (s1, s2) -> {

		// tag count
		int c1 = s1.getIntTagCount();
		int c2 = s2.getIntTagCount();
		int cmp = Integer.compare(c1, c2);
		if (cmp != 0)
		{
			// tag counts differ, more frequent (larger count) first
			return -cmp;
		}
		// fail, to be chained with thenComparing
		return 0;
	};
}
