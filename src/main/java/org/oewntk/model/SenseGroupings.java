/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class SenseGroupings
{
	static public class KeyLCLemmaAndPos implements Comparable
	{
		public final String lcLemma;

		public final char pos;

		public KeyLCLemmaAndPos(final Sense sense)
		{
			this(sense.getLemma(), sense.getPartOfSpeech());
		}

		public KeyLCLemmaAndPos(final String lcLemma, final char pos)
		{
			this.lcLemma = lcLemma.toLowerCase(Locale.ENGLISH);
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
		public int compareTo(final Object o)
		{
			KeyLCLemmaAndPos that = (KeyLCLemmaAndPos) o;
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

	public static Map<KeyLCLemmaAndPos, List<Sense>> sensesByLCLemmaAndPos(final CoreModel model)
	{
		return sensesBy(model.sensesById.values(), KeyLCLemmaAndPos::new);
	}

	public static <K> Map<K, List<Sense>> sensesBy(final CoreModel model, final Function<Sense, K> groupingFunction)
	{
		return sensesBy(model.sensesById.values(), groupingFunction);
	}

	public static Map<String, List<Sense>> sensesByLCLemma(final CoreModel model)
	{
		return sensesBy(model.sensesById.values(), s -> s.getLemma().toLowerCase(Locale.ENGLISH));
	}

	public static Map<KeyLCLemmaAndPos, List<Sense>> sensesByLCLemmaAndPos(final Collection<Sense> senses)
	{
		return sensesBy(senses, KeyLCLemmaAndPos::new);
	}

	public static Map<String, List<Sense>> sensesByLCLemma(final Collection<Sense> senses)
	{
		return sensesBy(senses, s -> s.getLemma().toLowerCase(Locale.ENGLISH));
	}

	public static <K> Map<K, List<Sense>> sensesBy(final Collection<Sense> senses, final Function<Sense, K> groupingFunction)
	{
		return senses.stream() //
				.collect(groupingBy(groupingFunction, TreeMap::new, toList()));
	}

	public static List<Sense> sensesForLCLemmaAndPos(final CoreModel model, final String lcLemma, final char pos)
	{
		return sensesFor(model, KeyLCLemmaAndPos::new, KeyLCLemmaAndPos.of(lcLemma, pos));
	}

	public static List<Sense> sensesForLCLemma(final CoreModel model, final String lcLemma)
	{
		return sensesFor(model, s -> s.getLemma().toLowerCase(Locale.ENGLISH), lcLemma);
	}

	public static <K> List<Sense> sensesFor(final CoreModel model, final Function<Sense, K> groupingFunction, K k)
	{
		return model.sensesById.values().stream() //
				.collect(groupingBy(groupingFunction, toList())).get(k);
	}

	public static Comparator<Sense> byDecreasingTagCount = (s1, s2) -> {

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
		return Integer.compare(s1.getLexIndex(), s2.getLexIndex());
	};

	public static <K> void dumpSensesByDecreasingTagCount(List<Sense> senses, final PrintStream ps)
	{
		senses.sort(byDecreasingTagCount);
		dumpSenses(senses, ps);
	}

	public static <K> void dumpSensesByDecreasingTagCount(Map.Entry<K, List<Sense>> sensesWithKey, final PrintStream ps)
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
