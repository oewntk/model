/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.PrintStream;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class LibTestModel
{
	public static Map<Key, Integer> makeIndexMap(final Stream<Key> stream)
	{
		final int[] i = {0};
		//noinspection UnnecessaryLocalVariable
		Map<Key, Integer> map = stream //
				.sequential() //
				.peek(e -> i[0]++) //
				.map(item -> new AbstractMap.SimpleEntry<>(item, i[0])) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue));
		// map.forEach((k, v) -> ps.printf("%s %s%n", k, v));
		return map;
	}

	public static Map<Key, Integer> makeSortedIndexMap(final Stream<Key> stream)
	{
		final int[] i = {0};
		//noinspection UnnecessaryLocalVariable
		Map<Key, Integer> map = stream //
				.sequential() //
				.peek(e -> i[0]++) //
				.map(item -> new AbstractMap.SimpleEntry<>(item, i[0])) //
				.collect(toMap( //
						SimpleEntry::getKey,  //
						SimpleEntry::getValue, //
						(existing, replacement) -> {
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

	public static void testScanLexesForTestWords(final CoreModel model, final Function<Lex, Key> keyGetter, final Function<Stream<Key>, Map<Key, Integer>> indexerByKey, final Set<String> testWords, final boolean peekTestWords, final PrintStream ps)
	{
		// stream of lexes
		Stream<Key> lexKeyStream = model.lexes.stream().peek(lex -> {
			if (testWords.contains(lex.getLemma()))
			{
				if (peekTestWords)
				{
					ps.println("@" + lex);
				}
			}
		}).map(keyGetter);

		// make lex-to-index map
		Map<Key, Integer> lexKeyToIndex = indexerByKey.apply(lexKeyStream);

		// test map
		ps.printf("%-12s %s%n", "index", "lex");
		for (String word : testWords)
		{
			Collection<Lex> lexes = model.getLexesByLemma().get(word);
			for (Lex lex : lexes)
			{
				ps.printf("%-12d %s%n", lexKeyToIndex.get(keyGetter.apply(lex)), lex);
			}
		}
	}

	public static void testWords(final CoreModel model, final PrintStream ps, String... words)
	{
		for (String word : words)
		{
			var lexes = model.getLexesByLemma().get(word);
			for (Lex lex : lexes)
			{
				ps.println(lex);
				dumpKeys(lex, ps);
			}
		}
	}

	public static void testWord(final String lemma, final CoreModel model, final PrintStream ps)
	{
		var lexes = Finder.getLexes(model, lemma);
		for (Lex lex : lexes)
		{
			ps.println(lex);
			dumpKeys(lex, ps);
		}
	}

	public static void testWord(final String lemma, char posFilter, final CoreModel model, final PrintStream ps)
	{
		Lex[] lexes = Finder.getLexesHavingPos(model, lemma, posFilter).toArray(Lex[]::new);
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
		ps.printf("\t--- key = %s%n", Key.W_P_A.of_t(lex1).equals(Key.W_P_A.of_t(lex2)));
		ps.printf("\tsha key = %s%n", Key.W_P_D.of_t(lex1).equals(Key.W_P_D.of_t(lex2)));
		ps.printf("\tic  key = %s%n", Key.W_P_D.of_lc_t(lex1).equals(Key.W_P_D.of_lc_t(lex2)));
		ps.printf("\tpos key = %s%n", Key.W_P_A.of_p(lex1).equals(Key.W_P_A.of_p(lex2)));
		ps.printf("\tpwn key = %s%n", Key.W_P.of_lc_p(lex1).equals(Key.W_P.of_lc_p(lex2)));
	}

	private static void dumpKeys(Lex lex, final PrintStream ps)
	{
		ps.printf("\t--- key = %s%n", Key.W_P_A.of_t(lex));
		ps.printf("\tsha key = %s%n", Key.W_P_D.of_t(lex));
		ps.printf("\tigc key = %s%n", Key.W_P_A.of_lc_t(lex));
		ps.printf("\tpos key = %s%n", Key.W_P_A.of_p(lex));
		ps.printf("\tpwn key = %s%n", Key.W_P.of_lc_p(lex));
	}
}
