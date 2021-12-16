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
		final var senses = sensesForLCLemma(model.getSensesById().values(), word);
		return sensesToString(senses);
	}

	private static String testCISensesGroupingByLCLemmaAndPosString(final CoreModel model, final String word, final char pos)
	{
		final var senses = sensesForLCLemmaAndPos(model.getSensesById().values(), word, pos);
		return sensesToString(senses);
	}

	private static String sensesToString(final List<Sense> senses)
	{
		if (senses == null || senses.isEmpty())
		{
			return "\t<none>";
		}
		senses.sort(SenseGroupings.byDecreasingTagCount);
		StringWriter sw = new StringWriter();
		senses.forEach(sense -> {
			sw.write(String.format("\t%d %s%n", sense.getIntTagCount(), sense));
		});
		return sw.toString();
	}
}
