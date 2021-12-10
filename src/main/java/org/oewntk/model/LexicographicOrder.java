/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LexicographicOrder
{
	public static final Comparator<String> lowerFirst = (s1, s2) -> {
		int c = s1.compareToIgnoreCase(s2);
		if (c != 0)
		{
			return c;
		}
		// same lower-case map
		return -s1.compareTo(s2);
	};

	public static final Comparator<String> upperFirst = (s1, s2) -> {
		int c = s1.compareToIgnoreCase(s2);
		if (c != 0)
		{
			return c;
		}
		// same lower-case map
		return s1.compareTo(s2);
	};

	public static void main(String[] args)
	{
		List<String> list = new ArrayList<>(List.of("alpha", "Alpha", "beta", "Beta", "gamma", "Gamma", "Alpha", "alPha", "Beta", "beTa", "Gamma", "gaMma"));
		System.out.println("original " + list);
		System.out.println();
		list.sort(String::compareTo);
		System.out.printf("%50s %s%n", "String::compareTo", list);
		list.sort(String::compareToIgnoreCase);
		System.out.printf("%50s %s%n", "String::compareToIgnoreCase", list);
		list.sort(upperFirst);
		System.out.printf("%50s %s%n", "upperFirst", list);
		list.sort(lowerFirst);
		System.out.printf("%50s %s%n", "lowerFirst", list);
	}
}
