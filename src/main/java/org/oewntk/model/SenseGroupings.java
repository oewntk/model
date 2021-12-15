/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class SenseGroupings
{
	static class KeyLCLemmaAndPos
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
		public String toString()
		{
			return "'" + lcLemma + "'-" + pos;
		}
	}

	public static Map<KeyLCLemmaAndPos, List<Sense>> sensesByLCLemmaAndPos(final CoreModel model)
	{
		return sensesBy(model, KeyLCLemmaAndPos::new);
	}

	public static Map<String, List<Sense>> sensesByLCLemma(final CoreModel model)
	{
		return sensesBy(model, s -> s.getLemma().toLowerCase(Locale.ENGLISH));
	}

	public static <K> Map<K, List<Sense>> sensesBy(final CoreModel model, final Function<Sense, K> groupingFunction)
	{
		return model.sensesById.values().stream() //
				.collect(groupingBy(groupingFunction, toList()));
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

	public static List<Sense> sortByDecreasingTagCount(List<Sense> senses)
	{
		return senses.stream() //
				.sorted((s1, s2) -> {
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
					return -1;
				}) //
				.collect(toList());
	}
}
