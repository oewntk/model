/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.PrintStream;

public class LibTestModelGroups
{
	public static void testCIMultipleAll(final CoreModel model, final PrintStream ps)
	{
		Groupings.lemmasByICLemmaHavingMultipleCount(model) //
				.forEach((u, cs) -> ps.printf("%s {%s}%n", u, String.join(",", cs)));
	}

	public static void testCILemmas(final CoreModel model, final String word, final PrintStream ps)
	{
		var lemmas = Groupings.lemmasByICLemma(model).get(word);
		ps.printf("%s {%s}%n", word, String.join(",", lemmas));
	}

	public static void testCICounts(final CoreModel model, final String word, final PrintStream ps)
	{
		var count = Groupings.countsByICLemma(model).get(word);
		ps.printf("%s %d%n", word, count);
	}

	public static void testCICountsFromMap(final CoreModel model, final String word, final PrintStream ps)
	{
		var count = Groupings.multipleCountsByICLemma(model).get(word);
		ps.printf("%s %d%n", word, count);
	}

	public static void testCI(final CoreModel model, final String word, final PrintStream ps)
	{
		final var hyperMap = Groupings.byICLemma(model);
		assert hyperMap != null;
		final var map = hyperMap.get(word);
		map.keySet().forEach(cs -> {
			ps.printf("cs '%s'%n", cs);
			map.get(cs) //
					.forEach(s -> ps.printf("\t%s%n", s));
		});
	}
}
