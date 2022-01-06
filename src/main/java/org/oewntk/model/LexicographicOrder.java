/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.util.Comparator;

/**
 * Lexicographic Order
 */
public class LexicographicOrder
{
	/**
	 * Lower-case first
	 */
	public static final Comparator<String> lowerFirst = (s1, s2) -> {
		int c = s1.compareToIgnoreCase(s2);
		if (c != 0)
		{
			return c;
		}
		// same lower-case map
		return -s1.compareTo(s2);
	};

	/**
	 * Upper-case first
	 */
	public static final Comparator<String> upperFirst = (s1, s2) -> {
		int c = s1.compareToIgnoreCase(s2);
		if (c != 0)
		{
			return c;
		}
		// same lower-case map
		return s1.compareTo(s2);
	};
}
