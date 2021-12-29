/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.PrintStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

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

	public static Map<KeyLCLemmaAndPos, Collection<Sense>> sensesByLCLemmaAndPos(final Collection<Sense> senses)
	{
		return Groupings.groupBy(senses.stream(), KeyLCLemmaAndPos::new);
	}

	public static Map<String, Collection<Sense>> sensesByLCLemma(final Collection<Sense> senses)
	{
		return Groupings.groupBy(senses.stream(), Sense::getLCLemma);
	}

	// S E N S E S  F O R
	// for debug as it makes a fresh map every time

	public static <K> Collection<Sense> sensesFor(final Collection<Sense> senses, final Function<Sense, K> groupingFunction, K k)
	{
		return Groupings.groupBy(senses.stream(), groupingFunction).get(k);
	}

	public static Collection<Sense> sensesForLCLemmaAndPos(final Collection<Sense> senses, final String lcLemma, final char pos)
	{
		return sensesFor(senses, KeyLCLemmaAndPos::new, KeyLCLemmaAndPos.of(lcLemma, pos));
	}

	public static Collection<Sense> sensesForLCLemma(final Collection<Sense> senses, final String lcLemma)
	{
		return sensesFor(senses, Sense::getLCLemma, lcLemma);
	}

	// C O M P A R A T O R

	public static final Comparator<Sense> byDecreasingTagCount = (s1, s2) -> {

		if (s1.equals(s2))
		{
			return 0;
		}
		int c1 = s1.getIntTagCount();
		int c2 = s2.getIntTagCount();
		int cmp1 = Integer.compare(c1, c2);
		if (cmp1 != 0)
		{
			return -cmp1;
		}
		String lemma1 = s1.getLemma();
		String lemma2 = s2.getLemma();
		if (lemma1.equals(lemma2))
		{
			return Integer.compare(s1.getLexIndex(), s2.getLexIndex());
		}
		// upper-case first
		return lemma1.compareTo(lemma2);
	};

	public static <K> void dumpSensesByDecreasingTagCount(List<Sense> senses, final PrintStream ps)
	{
		senses.sort(byDecreasingTagCount);
		dumpSenses(senses, ps);
	}

	public static <K> void dumpSensesByDecreasingTagCount(Entry<K, List<Sense>> sensesWithKey, final PrintStream ps)
	{
		var k = sensesWithKey.getKey();
		var senses2 = sensesWithKey.getValue();
		ps.printf("%s:%n", k);
		dumpSensesByDecreasingTagCount(senses2, ps);
		ps.println();
	}

	public static <K> void dumpSenses(List<Sense> senses, final PrintStream ps)
	{
		final int[] i = {0};
		senses.forEach(s -> ps.printf("\t[%d] %2d %s%n", i[0]++, s.getIntTagCount(), s));
		ps.println();
	}
}
