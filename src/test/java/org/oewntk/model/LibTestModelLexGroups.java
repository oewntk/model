/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.PrintStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.oewntk.model.SenseGroupings.sensesForLCLemma;
import static org.oewntk.model.SenseGroupings.sensesForLCLemmaAndPos;

public class LibTestModelLexGroups
{
	public static void testCIMultipleAll(final CoreModel model, final PrintStream ps)
	{
		LexGroupings.lemmasByICLemmaHavingMultipleCount(model) //
				.forEach((u, cs) -> ps.printf("%s {%s}%n", u, String.join(",", cs)));
	}

	public static void testCILemmas(final CoreModel model, final String word, final PrintStream ps)
	{
		var lemmas = LexGroupings.lemmasByICLemma(model).get(word);
		ps.printf("%s {%s}%n", word, String.join(",", lemmas));
	}

	public static void testCICounts(final CoreModel model, final String word, final PrintStream ps)
	{
		var count = LexGroupings.countsByICLemma(model).get(word);
		ps.printf("%s %d%n", word, count);
	}

	public static void testCICountsFromMap(final CoreModel model, final String word, final PrintStream ps)
	{
		var count = LexGroupings.multipleCountsByICLemma(model).get(word);
		ps.printf("%s %d%n", word, count);
	}

	public static void testCIHypermap3(final CoreModel model, String word, final PrintStream ps)
	{
		String s1 = testCIHypermapString(model, word);
		ps.printf("ci '%s'%n", word);
		ps.println(s1);

		word = word.toLowerCase(Locale.ENGLISH);
		String s2 = testCIHypermapString(model, word);
		ps.printf("ci '%s'%n", word);
		ps.println(s2);

		word = word.toUpperCase(Locale.ENGLISH);
		String s3 = testCIHypermapString(model, word);
		ps.printf("ci '%s'%n", word);
		ps.println(s3);

		assertEquals(s1, s2);
		assertEquals(s2, s3);
	}

	public static void testCILexesFor3(final CoreModel model, String word, final PrintStream ps)
	{
		String s1 = testCILexesString(model, word);
		ps.printf("ci '%s'%n", word);
		ps.println(s1);

		word = word.toLowerCase(Locale.ENGLISH);
		String s2 = testCILexesString(model, word);
		ps.printf("ci '%s'%n", word);
		ps.println(s2);

		word = word.toUpperCase(Locale.ENGLISH);
		String s3 = testCILexesString(model, word);
		ps.printf("ci '%s'%n", word);
		ps.println(s3);

		assertEquals(s1, s2);
		assertEquals(s2, s3);
	}

	public static void testCIHypermap(final CoreModel model, final String word, final PrintStream ps)
	{
		ps.printf("ci '%s'%n", word);
		ps.println(testCIHypermapString(model, word));
	}

	public static void testCILexes(final CoreModel model, final String word, final PrintStream ps)
	{
		ps.printf("ci '%s'%n", word);
		ps.println(testCILexesString(model, word));
	}

	public static void testCILexesFor(final CoreModel model, final String word, final PrintStream ps)
	{
		ps.printf("ci '%s'%n", word);
		ps.println(testCILexesForWordString(model, word));
	}

	public static String testCIHypermapString(final CoreModel model, final String word)
	{
		StringWriter sw = new StringWriter();
		final var hyperMap = LexGroupings.byLCLemma(model);
		assert hyperMap != null;
		final var map = hyperMap.get(word.toLowerCase(Locale.ENGLISH));
		map.keySet().forEach(cs -> {
			sw.write(String.format("\tcs '%s'%n", cs));
			map.get(cs) //
					.forEach(s -> sw.write(String.format("\t\t%s%n", s)));
		});
		return sw.toString();
	}

	public static String testCILexesString(final CoreModel model, final String word)
	{
		StringWriter sw = new StringWriter();
		final var map = LexGroupings.lexesByLCLemma(model);
		assert map != null;
		final var lexes = map.get(word.toLowerCase(Locale.ENGLISH));
		assert lexes != null;
		return lexesToString(lexes);
	}

	public static String testCILexesForWordString(final CoreModel model, final String word)
	{
		StringWriter sw = new StringWriter();
		final var lexes = LexGroupings.lexesForLCLemma(model, word);
		assert lexes != null;
		return lexesToString(lexes);
	}

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
		StringWriter sw = new StringWriter();
		senses.forEach(sense -> {
			sw.write(String.format("\t%s%n", sense));
		});
		return sw.toString();
	}

	private static String lexesToString(final List<Lex> lexes)
	{
		if (lexes == null || lexes.isEmpty())
		{
			return "\t<none>";
		}
		StringWriter sw = new StringWriter();
		lexes.forEach(lex -> {
			sw.write(String.format("\t%s%n", lex));
		});
		return sw.toString();
	}
}