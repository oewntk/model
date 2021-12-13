/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.PrintStream;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.Assert;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.counting;

public class LibTestModel
{
	public static void testKey(final CoreModel model, final PrintStream ps)
	{
		var dups = model.lexesByLemma.entrySet() //
				.stream() // stream of map entries
				.flatMap(e -> e.getValue().stream()) // stream of lexes
				.map(Key.OEWN::of) // stream of keys
				.collect(groupingBy(Function.identity(), counting())) // map(key, count));
				.entrySet().stream() // stream of map entries
				.filter(m -> m.getValue() > 1) // if map value > 1, duplicate element
				//.sorted(Comparator.comparingLong(Map.Entry::getValue)) //
				.sorted(Comparator.comparing(e -> e.getKey().toString())) //
				.collect(toCollection(LinkedHashSet::new));
		ps.println(dups.size());
		dups.forEach(ps::println);
		Assert.assertEquals(0, dups.size());
	}

	public static void testKeyPos(final CoreModel model, final PrintStream ps)
	{
		var dups = model.lexesByLemma.entrySet() //
				.stream() // stream of map entries
				.flatMap(e -> e.getValue().stream()) // stream of lexes
				.map(Key.Pos::of) // stream of keys
				.collect(groupingBy(Function.identity(), counting())) // map(key, count));
				.entrySet().stream() // stream of map entries
				.filter(m -> m.getValue() > 1) // if map value > 1, duplicate element
				//.sorted(Comparator.comparingLong(Map.Entry::getValue)) //
				.sorted(Comparator.comparing(e -> e.getKey().toString())) //
				.collect(toCollection(LinkedHashSet::new));
		ps.println(dups.size());
		dups.forEach(ps::println);
		Assert.assertNotEquals(0, dups.size());
	}

	public static void testKeyIC(final CoreModel model, final PrintStream ps)
	{
		var dups = model.lexesByLemma.entrySet() //
				.stream() // stream of map entries
				.flatMap(e -> e.getValue().stream()) // stream of lexes
				.map(Key.IC::of) // stream of keys
				.collect(groupingBy(Function.identity(), counting())) // map(key, count));
				.entrySet().stream() // stream of map entries
				.filter(m -> m.getValue() > 1) // if map value > 1, duplicate element
				//.sorted(Comparator.comparingLong(Map.Entry::getValue)) //
				.sorted(Comparator.comparing(e -> e.getKey().toString())) //
				.collect(toCollection(LinkedHashSet::new));
		ps.println(dups.size());
		// dups.forEach(ps::println);
		Assert.assertNotEquals(0, dups.size());
	}

	public static void testKeyPWN(final CoreModel model, final PrintStream ps)
	{
		var dups = model.lexesByLemma.entrySet() //
				.stream() // stream of map entries
				.flatMap(e -> e.getValue().stream()) // stream of lexes
				.map(Key.PWN::of) // stream of keys
				.collect(groupingBy(Function.identity(), counting())) // map(key, count));
				.entrySet().stream() // stream of map entries
				.filter(m -> m.getValue() > 1) // if map value > 1, duplicate element
				//.sorted(Comparator.comparingLong(Map.Entry::getValue)) //
				.sorted(Comparator.comparing(e -> e.getKey().toString())) //
				.collect(toCollection(LinkedHashSet::new));
		ps.println(dups.size());
		// dups.forEach(ps::println);
		Assert.assertNotEquals(0, dups.size());
	}

	public static <T extends Comparable<T>> Map<T, Integer> makeSortedMap(final Stream<T> stream)
	{
		final int[] i = { 0 };
		//noinspection UnnecessaryLocalVariable
		Map<T, Integer> map = stream //
				.sequential() //
				.peek(e -> i[0]++) //
				.map(item -> new AbstractMap.SimpleEntry<>(item, i[0])) //
				.collect(toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue, (existing, replacement) -> {
					if (existing.equals(replacement))
					{
						throw new IllegalArgumentException(existing + "," + replacement);
					}
					return existing;
				}, TreeMap::new));
		// map.forEach((k, v) -> ps.printf("%s %s%n", k, v));
		return map;
	}

	public static <T> Map<T, Integer> makeMap(final Stream<T> stream)
	{
		final int[] i = { 0 };
		//noinspection UnnecessaryLocalVariable
		Map<T, Integer> map = stream //
				.sequential() //
				.peek(e -> i[0]++) //
				.map(item -> new AbstractMap.SimpleEntry<>(item, i[0])) //
				.collect(toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
		// map.forEach((k, v) -> ps.printf("%s %s%n", k, v));
		return map;
	}

	public static void testStreams(final CoreModel model, final Function<Stream<Lex>, Map<Lex, Integer>> mapFunction, final Set<String> testWords, final boolean peekTestWords, final PrintStream ps)
	{
		// stream of lexes
		Stream<Lex> lexStream = model.lexesByLemma.entrySet() //
				.stream() //
				.flatMap(e -> e.getValue().stream()) //
				.peek(lex -> {
					if (testWords.contains(lex.getLemma()))
					{
						if (peekTestWords)
						{
							ps.println("@" + lex);
						}
					}
				});

		// make lex-to-nid map
		Map<Lex, Integer> lexToNID = mapFunction.apply(lexStream);

		// test map
		for (String word : testWords)
		{
			List<Lex> lexes = model.lexesByLemma.get(word);
			for (Lex lex : lexes)
			{
				ps.printf("%d %s%n", lexToNID.get(lex), lex);
			}
		}
	}

	public static void testWords(final CoreModel model, final PrintStream ps, String... words)
	{
		for (String word : words)
		{
			List<Lex> lexes = model.lexesByLemma.get(word);
			for (Lex lex : lexes)
			{
				ps.println(lex);
				dumpKeys(lex, ps);
			}
		}
	}

	public static void testWord(final String lemma, final CoreModel model, final PrintStream ps)
	{
		List<Lex> lexes = model.lexesByLemma.get(lemma);
		for (Lex lex : lexes)
		{
			ps.println(lex);
			dumpKeys(lex, ps);
		}
	}

	public static void testWord(final String lemma, char posFilter, final CoreModel model, final PrintStream ps)
	{
		Lex[] lexes = Finder.getLexesHavingPos(model.lexesByLemma, lemma, posFilter);
		int i = 0;
		for (Lex lex : lexes)
		{
			ps.printf("[%d] %s%n", ++i, lex);
			dumpKeys(lex, ps);
		}

		if (lexes.length > 1)
		{
			ps.println();
			ps.printf("comparing keys equals() for [%d] and [%d]%n", 1, 2);

			ps.println(lexes[0]);
			ps.println(lexes[1]);
			dumpKeyEquals(lexes[0], lexes[1], ps);
		}
	}

	private static void dumpKeyEquals(Lex lex1, Lex lex2, final PrintStream ps)
	{
		ps.printf("\t--- key = %s%n", Key.OEWN.of(lex1).equals(Key.OEWN.of(lex2)));
		ps.printf("\tsha key = %s%n", Key.Shallow.of(lex1).equals(Key.Shallow.of(lex2)));
		ps.printf("\tigc key = %s%n", Key.IC.of(lex1).equals(Key.IC.of(lex2)));
		ps.printf("\tpos key = %s%n", Key.Pos.of(lex1).equals(Key.Pos.of(lex2)));
		ps.printf("\tpwn key = %s%n", Key.PWN.of(lex1).equals(Key.PWN.of(lex2)));
	}

	private static void dumpKeys(Lex lex, final PrintStream ps)
	{
		ps.printf("\t--- key = %s%n", Key.OEWN.of(lex));
		ps.printf("\tsha key = %s%n", Key.Shallow.of(lex));
		ps.printf("\tigc key = %s%n", Key.IC.of(lex));
		ps.printf("\tpos key = %s%n", Key.Pos.of(lex));
		ps.printf("\tpwn key = %s%n", Key.PWN.of(lex));
	}
}
