/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestLexicographicOrder
{
	@Test
	public void testRow()
	{
		List<String> list = new ArrayList<>(List.of("alpha", "Alpha", "beta", "Beta", "gamma", "Gamma", "Alpha", "alPha", "Beta", "beTa", "Gamma", "gaMma"));

		Tracing.psInfo.printf("%50s %s%n", "original", list);

		list.sort(String::compareTo);
		Tracing.psInfo.printf("%50s %s%n", "String::compareTo", list);
		assertEquals("[Alpha, Alpha, Beta, Beta, Gamma, Gamma, alPha, alpha, beTa, beta, gaMma, gamma]", list.toString());

		list.sort(String::compareToIgnoreCase);
		Tracing.psInfo.printf("%50s %s%n", "String::compareToIgnoreCase", list);
		assertEquals("[Alpha, Alpha, alPha, alpha, Beta, Beta, beTa, beta, Gamma, Gamma, gaMma, gamma]", list.toString());

		list.sort(LexicographicOrder.upperFirst);
		Tracing.psInfo.printf("%50s %s%n", "upperFirst", list);
		assertEquals("[Alpha, Alpha, alPha, alpha, Beta, Beta, beTa, beta, Gamma, Gamma, gaMma, gamma]", list.toString());

		list.sort(LexicographicOrder.lowerFirst);
		Tracing.psInfo.printf("%50s %s%n", "lowerFirst", list);
		assertEquals("[alpha, alPha, Alpha, Alpha, beta, beTa, Beta, Beta, gamma, gaMma, Gamma, Gamma]", list.toString());
	}
}
