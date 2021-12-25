/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class TestKeys
{
	@Test
	public void testRow()
	{
		String wordRow = "row";
		Pronunciation pRowOu = new Pronunciation("ɹəʊ", null);
		Pronunciation pRowAu = new Pronunciation("ɹaʊ", null);

		Lex lexRowOu = new Lex(wordRow, "n-1", "source1").setPronunciations(pRowOu);
		Lex lexRowAu = new Lex(wordRow, "n-2", "source2").setPronunciations(pRowAu);
		Lex lexRowOuN = new Lex(wordRow, "n", "source1").setPronunciations(pRowOu);
		Lex lexRowAuN = new Lex(wordRow, "n", "source2").setPronunciations(pRowAu);

		assertNotEquals(Key.W_P_A.of_t(lexRowOu), Key.W_P_A.of_t(lexRowAu));
		assertNotEquals(Key.W_P_D.of_t(lexRowOu), Key.W_P_D.of_t(lexRowAu)); // because discriminant is different
		assertEquals(Key.W_P_D.of_t(lexRowOuN), Key.W_P_D.of_t(lexRowAuN)); // because discriminant is same
		assertEquals(Key.W_P.of(lexRowOu, Lex::getLemma, Lex::getType), Key.W_P.of(lexRowAu, Lex::getLemma, Lex::getType));
	}

	@Test
	public void testMobile()
	{
		String wordMobile = "mobile";
		Pronunciation pMobile = new Pronunciation("ˈməʊbaɪl", null);
		Pronunciation pMobileGB = new Pronunciation("ˈməʊbaɪl", "GB");
		Pronunciation pMobileUS = new Pronunciation("ˈmoʊbil", "US");
		Pronunciation[] paMobile1 = new Pronunciation[]{pMobile, pMobileGB, pMobileUS};
		Pronunciation[] paMobile2 = new Pronunciation[]{pMobile, pMobileGB, pMobileUS};

		Lex lexMobile0 = new Lex(wordMobile, "n", "source1");
		Lex lexMobile1 = new Lex(wordMobile, "n", "source1").setPronunciations(paMobile1);
		Lex lexMobile2 = new Lex(wordMobile, "n", "source2").setPronunciations(paMobile2);

		assertEquals(Key.W_P_A.of_t(lexMobile1), Key.W_P_A.of_t(lexMobile2));
		assertEquals(Key.W_P_D.of_t(lexMobile1), Key.W_P_D.of_t(lexMobile2));
		assertEquals(Key.W_P.of_t(lexMobile1), Key.W_P.of_t(lexMobile2));

		assertNotEquals(Key.W_P_A.of_t(lexMobile1), Key.W_P_A.of_t(lexMobile0));
		assertEquals(Key.W_P_D.of_t(lexMobile1), Key.W_P_D.of_t(lexMobile0)); // because discriminant is both null
		assertEquals(Key.W_P.of_t(lexMobile1), Key.W_P.of_t(lexMobile0));
	}

	@Test
	public void testCritical()
	{
		String wordCritical = "critical";
		Lex lexCriticalA = new Lex(wordCritical, "a", "source1");
		Lex lexCriticalS = new Lex(wordCritical, "s", "source2");

		assertNotEquals(Key.W_P_A.of_t(lexCriticalA), Key.W_P_A.of_t(lexCriticalS));
		assertNotEquals(Key.W_P_D.of_t(lexCriticalA), Key.W_P_D.of_t(lexCriticalS));
		assertEquals(Key.W_P.of_p(lexCriticalA), Key.W_P.of_p(lexCriticalS));
		assertEquals(Key.W_P_A.of_p(lexCriticalA), Key.W_P_A.of_p(lexCriticalS)); // A and S are merged
	}

	@Test
	public void testCapitalisation()
	{
		String wordEarthL = "earth";
		String wordEarthU = "Earth";
		Lex lexEarthL = new Lex(wordEarthL, "n", "source1");
		Lex lexEarthU = new Lex(wordEarthU, "n", "source2");

		assertNotEquals(Key.W_P_A.of_t(lexEarthL), Key.W_P_A.of_t(lexEarthU));
		assertNotEquals(Key.W_P_D.of_t(lexEarthL), Key.W_P_D.of_t(lexEarthU));
		assertNotEquals(Key.W_P.of_lc_t(lexEarthL), Key.W_P.of_lc_t(lexEarthU));
		assertEquals(Key.W_P_A.of_lc_t(lexEarthL), Key.W_P_A.of_lc_t(lexEarthU));
	}

	@Test
	public void testPronunciations()
	{
		Pronunciation pMobile = new Pronunciation("ˈməʊbaɪl", null);
		Pronunciation pMobileGB = new Pronunciation("ˈməʊbaɪl", "GB");
		Pronunciation pMobileUS = new Pronunciation("ˈmoʊbil", "US");
		Pronunciation[] paMobile1 = new Pronunciation[]{pMobile, pMobileGB, pMobileUS};
		Pronunciation[] paMobile2 = new Pronunciation[]{pMobile, pMobileGB, pMobileUS};
		Set<Pronunciation> psMobile1 = Set.of(paMobile1);
		Set<Pronunciation> psMobile2 = Set.of(paMobile2);

		assertEquals(pMobile, new Pronunciation("ˈməʊbaɪl", null));
		assertEquals(pMobileGB, new Pronunciation("ˈməʊbaɪl", "GB"));
		assertNotEquals(pMobile, null);
		assertNotEquals(pMobile, pMobileGB);
		assertNotEquals(pMobile, pMobileUS);
		assertNotEquals(pMobileGB, pMobileUS);

		assertArrayNotEquals(paMobile1, null);
		assertArrayNotEquals(paMobile1, new Pronunciation[0]);
		assertArrayNotEquals(paMobile1, new Pronunciation[]{pMobileGB, pMobile, pMobileUS});
		assertArrayNotEquals(paMobile2, new Pronunciation[]{pMobileGB, pMobileUS, pMobile});
		assertArrayEquals(paMobile1, new Pronunciation[]{pMobile, pMobileGB, pMobileUS});
		assertArrayEquals(paMobile2, new Pronunciation[]{pMobile, pMobileGB, pMobileUS});

		assertEquals(psMobile1, Set.of(paMobile1));
		assertEquals(psMobile2, Set.of(paMobile2));
		assertEquals(psMobile1, Set.of(new Pronunciation[]{pMobileGB, pMobile, pMobileUS}));
		assertEquals(psMobile2, Set.of(new Pronunciation[]{pMobile, pMobileGB, pMobileUS}));
		assertEquals(psMobile1, Set.of(new Pronunciation[]{new Pronunciation("ˈməʊbaɪl", "GB"), new Pronunciation("ˈməʊbaɪl", null), new Pronunciation("ˈmoʊbil", "US")}));
		assertEquals(psMobile2, Set.of(new Pronunciation[]{new Pronunciation("ˈməʊbaɪl", null), new Pronunciation("ˈməʊbaɪl", "GB"), new Pronunciation("ˈmoʊbil", "US")}));

		assertNotEquals(paMobile1, paMobile2);
		assertEquals(psMobile1, psMobile2);

		Pronunciation pRowOu = new Pronunciation("ɹəʊ", null);
		Pronunciation pRowAu = new Pronunciation("ɹaʊ", null);
		Pronunciation[] paRow1 = new Pronunciation[]{pRowOu, pRowAu};
		Pronunciation[] paRow2 = new Pronunciation[]{pRowAu, pRowOu};
		Set<Pronunciation> psRow1 = Set.of(paRow1);
		Set<Pronunciation> psRow2 = Set.of(paRow2);

		assertNotEquals(pRowOu, pRowAu);
		assertNotEquals(paRow1, paRow2);
		assertEquals(psRow1, psRow2);
	}

	private void assertArrayNotEquals(final Object[] expecteds, final Object[] actuals)
	{
		try
		{
			assertArrayEquals(expecteds, actuals);
		}
		catch (AssertionError e)
		{
			return;
		}
		fail();
	}
}
