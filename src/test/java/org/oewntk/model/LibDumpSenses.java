/*
 * Copyright (c) 2022. Bernard Bou.
 */

package org.oewntk.model;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;

public class LibDumpSenses
{
	public static <K> void dumpSensesByDecreasingTagCount(List<Sense> senses, final PrintStream ps)
	{
		senses.sort(SenseGroupings.BY_DECREASING_TAGCOUNT.thenComparing(Sense::getSensekey));
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
