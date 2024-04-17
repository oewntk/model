/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.util.Set;
import java.util.function.Function;

public class Utils
{
	// Set

	/**
	 * Array to set
	 *
	 * @param objects array of objects
	 * @param <T>     type of objects
	 * @return set of objects
	 */
	static <T> Set<T> toSet(final T[] objects)
	{
		if (objects == null)
		{
			return Set.of();
		}
		return Set.of(objects);
	}

	// Name for extractor

	private static final String dummyUpper = "CASE";

	private static final char dummySatellite = 's';

	private static final Lex dummyLex = new Lex(dummyUpper, Character.toString(dummySatellite), null);

	/**
	 * Name a word extractor (by applying dummy data)
	 *
	 * @param wordExtractor word extractor
	 * @param <L>           word extractor type
	 * @return name
	 */
	public static <L extends Function<Lex, String>> String toWordExtractorString(final L wordExtractor)
	{
		return wordExtractor.apply(dummyLex).equals(dummyUpper) ? "cs" : "lc";
	}

	/**
	 * Name a pos/type extractor (by applying dummy data)
	 *
	 * @param posTypeExtractor word extractor
	 * @param <P>              posType extractor type
	 * @return name
	 */
	public static <P extends Function<Lex, Character>> Object toPosTypeExtractorString(final P posTypeExtractor)
	{
		return posTypeExtractor.apply(dummyLex).equals(dummySatellite) ? "t" : "p";
	}
}
