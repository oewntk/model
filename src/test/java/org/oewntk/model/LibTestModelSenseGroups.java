/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.PrintStream;
import java.io.StringWriter;
import java.util.List;

import static org.oewntk.model.SenseGroupings.sensesForLCLemma;
import static org.oewntk.model.SenseGroupings.sensesForLCLemmaAndPos;

public class LibTestModelSenseGroups
{
	public static void testCISensesGroupingByLCLemmaAndPos(final CoreModel model, final String word, final char pos, final PrintStream ps)
	{
		ps.printf("ci '%s' %s%n", word, pos);
		ps.println(testCISensesGroupingByLCLemmaAndPosString(model, word, pos));
	}

	public static void testCISensesGroupingByLCLemma(final CoreModel model, final String word, final PrintStream ps)
	{
		ps.printf("ci '%s'%n", word);
		ps.println(testCISensesGroupingByLCLemmaString(model, word));
	}

	private static String testCISensesGroupingByLCLemmaString(final CoreModel model, final String word)
	{
		final var senses = sensesForLCLemma(model, word);
		return sensesToString(senses);
	}

	private static String testCISensesGroupingByLCLemmaAndPosString(final CoreModel model, final String word, final char pos)
	{
		final var senses = sensesForLCLemmaAndPos(model, word, pos);
		return sensesToString(senses);
	}

	private static String sensesToString(final List<Sense> senses)
	{
		if (senses == null || senses.isEmpty())
		{
			return "\t<none>";
		}
		List<Sense> senses2 = SenseGroupings.sortByDecreasingTagCount(senses);
		StringWriter sw = new StringWriter();
		senses2.forEach(sense2 -> {
			sw.write(String.format("\t%d %s%n", sense2.getIntTagCount(), sense2));
		});
		return sw.toString();
	}
}
