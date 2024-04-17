/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import org.junit.Test;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestLexicographicOrder
{
	private static final PrintStream ps = !System.getProperties().containsKey("SILENT") ? Tracing.psInfo : Tracing.psNull;

	@Test
	public void testRow()
	{
		List<String> list = new ArrayList<>(List.of("alpha", "Alpha", "beta", "Beta", "gamma", "Gamma", "Alpha", "alPha", "Beta", "beTa", "Gamma", "gaMma"));

		ps.printf("%50s %s%n", "original", list);

		list.sort(String::compareTo);
		ps.printf("%50s %s%n", "String::compareTo", list);
		assertEquals("[Alpha, Alpha, Beta, Beta, Gamma, Gamma, alPha, alpha, beTa, beta, gaMma, gamma]", list.toString());

		list.sort(String::compareToIgnoreCase);
		ps.printf("%50s %s%n", "String::compareToIgnoreCase", list);
		assertEquals("[Alpha, Alpha, alPha, alpha, Beta, Beta, beTa, beta, Gamma, Gamma, gaMma, gamma]", list.toString());

		list.sort(LexicographicOrder.upperFirst);
		ps.printf("%50s %s%n", "upperFirst", list);
		assertEquals("[Alpha, Alpha, alPha, alpha, Beta, Beta, beTa, beta, Gamma, Gamma, gaMma, gamma]", list.toString());

		list.sort(LexicographicOrder.lowerFirst);
		ps.printf("%50s %s%n", "lowerFirst", list);
		assertEquals("[alpha, alPha, Alpha, Alpha, beta, beTa, Beta, Beta, gamma, gaMma, Gamma, Gamma]", list.toString());
	}
}
