/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.StringWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Utils
{
	public static String sensesToString(final List<Sense> senses)
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

	public static String sensesToStringByDecreasingTagCount(final List<Sense> senses)
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

	public static String lexesToString(final List<Lex> lexes)
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
	public static String lexHypermapForLemmaToString(final Map<String, Map<String, List<Lex>>> lexHypermap, final String lemma)
	{
		final var map = lexHypermap.get(lemma.toLowerCase(Locale.ENGLISH));
		StringWriter sw = new StringWriter();
		map.keySet().forEach(cs -> {
			sw.write(String.format("\tcs '%s'%n", cs));
			map.get(cs) //
					.forEach(s -> sw.write(String.format("\t\t%s%n", s)));
		});
		return sw.toString();
	}
}