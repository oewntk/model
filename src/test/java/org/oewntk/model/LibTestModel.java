/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class LibTestModel
{
	public static <T extends Comparable<T>> Map<T, Integer> makeSortedIndexMap(final Stream<T> stream)
	{
		final int[] i = {0};
		//noinspection UnnecessaryLocalVariable
		Map<T, Integer> map = stream //
				.sequential() //
				.peek(e -> i[0]++) //
				.map(item -> new AbstractMap.SimpleEntry<>(item, i[0])) //
				.collect(toMap( //
						AbstractMap.SimpleEntry::getKey,  //
						AbstractMap.SimpleEntry::getValue, (existing, replacement) -> {
							if (existing.equals(replacement))
							{
								throw new IllegalArgumentException(existing + "," + replacement);
							}
							return existing;
						}, //
						TreeMap::new));
		// map.forEach((k, v) -> ps.printf("%s %s%n", k, v));
		return map;
	}

	public static <T> Map<T, Integer> makeIndexMap(final Stream<T> stream)
	{
		final int[] i = {0};
		//noinspection UnnecessaryLocalVariable
		Map<T, Integer> map = stream //
				.sequential() //
				.peek(e -> i[0]++) //
				.map(item -> new AbstractMap.SimpleEntry<>(item, i[0])) //
				.collect(toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
		// map.forEach((k, v) -> ps.printf("%s %s%n", k, v));
		return map;
	}

	public static void testScanLexesForTestWords(final CoreModel model, final Function<Stream<Lex>, Map<Lex, Integer>> mapFunction, final Set<String> testWords, final boolean peekTestWords, final PrintStream ps)
	{
		// stream of lexes
		Stream<Lex> lexStream = model.getLexesByLemma().entrySet() //
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

		// make lex-to-index map
		Map<Lex, Integer> lexToIndex = mapFunction.apply(lexStream);

		// test map
		ps.printf("%-12s %s%n", "index", "lex");
		for (String word : testWords)
		{
			List<Lex> lexes = model.getLexesByLemma().get(word);
			for (Lex lex : lexes)
			{
				ps.printf("%-12d %s%n", lexToIndex.get(lex), lex);
			}
		}
	}

	public static void testWords(final CoreModel model, final PrintStream ps, String... words)
	{
		for (String word : words)
		{
			List<Lex> lexes = model.getLexesByLemma().get(word);
			for (Lex lex : lexes)
			{
				ps.println(lex);
				dumpKeys(lex, ps);
			}
		}
	}

	public static void testWord(final String lemma, final CoreModel model, final PrintStream ps)
	{
		List<Lex> lexes = Finder.getLexes(model, lemma);
		for (Lex lex : lexes)
		{
			ps.println(lex);
			dumpKeys(lex, ps);
		}
	}

	public static void testWord(final String lemma, char posFilter, final CoreModel model, final PrintStream ps)
	{
		Lex[] lexes = Finder.getLexesHavingPos(model, lemma, posFilter);
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
