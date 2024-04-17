/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.model

object Utils {

	// Set

	/**
	 * Array to set
	 *
	 * @param objects array of objects
	 * @param <T>     type of objects
	 * @return set of objects
	</T> */
	@JvmStatic
	fun <T> toSet(objects: Array<T>?): Set<T> {
		if (objects == null) {
			return setOf()
		}
		return mutableSetOf(*objects)
	}

	// Name for extractor

	private const val dummyUpper = "CASE"

	private const val dummySatellite = 's'

	private val dummyLex = Lex(dummyUpper, dummySatellite.toString(), null)

	/**
	 * Name a word extractor (by applying dummy data)
	 *
	 * @param wordExtractor word extractor
	 * @param <L>           word extractor type
	 * @return name
	 */
	fun toWordExtractorString(wordExtractor: (Lex) -> String): String {
		return if (wordExtractor.invoke(dummyLex) == dummyUpper) "cs" else "lc"
	}

	/**
	 * Name a pos/type extractor (by applying dummy data)
	 *
	 * @param posTypeExtractor word extractor
	 * @param <P>              posType extractor type
	 * @return name
	 */
	fun toPosTypeExtractorString(posTypeExtractor: (Lex) -> Char): String {
		return if (posTypeExtractor.invoke(dummyLex) == dummySatellite) "t" else "p"
	}
}
