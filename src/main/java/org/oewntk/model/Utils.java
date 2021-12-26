/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.util.Set;
import java.util.function.Function;

public class Utils
{
	static <T> Set<T> toSet(final T[] objects)
	{
		if (objects == null)
		{
			return Set.of();
		}
		return Set.of(objects);
	}

	private static final String dummyUpper = "CASE";

	private static final char dummySatellite = 's';
	private static final Lex dummyLex = new Lex(dummyUpper, Character.toString(dummySatellite), null);

	public static <L extends Function<Lex, String>> String toWordExtractorString(final L wordExtractor)
	{
		return wordExtractor.apply(dummyLex).equals(dummyUpper) ? "cs" : "lc";
	}

	public static <P extends Function<Lex, Character>> Object toPosTypeExtractorString(final P posTypeExtractor)
	{
		return posTypeExtractor.apply(dummyLex).equals(dummySatellite) ? "t" : "p";
	}
}
