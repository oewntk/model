/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

public class TestKeys
{
	@SuppressWarnings("EmptyMethod")
	@BeforeClass
	public static void init()
	{
	}

	@Test
	public void testRow()
	{
		String wordRow = "row";
		Pronunciation pRowOu = new Pronunciation("ɹəʊ", null);
		Pronunciation pRowAu = new Pronunciation("ɹaʊ", null);

		Lex lexRowOu = new Lex("source1", wordRow, "n-1").setPronunciations(pRowOu);
		Lex lexRowAu = new Lex("source2", wordRow, "n-2").setPronunciations(pRowAu);
		Lex lexRowOuN = new Lex("source1", wordRow, "n").setPronunciations(pRowOu);
		Lex lexRowAuN = new Lex("source2", wordRow, "n").setPronunciations(pRowAu);
		assertNotEquals(lexRowOu, lexRowAu);
		assertNotEquals(Key.OEWN.of(lexRowOu), Key.OEWN.of(lexRowAu));
		assertNotEquals(Key.Shallow.of(lexRowOu), Key.Shallow.of(lexRowAu)); // because discriminant is different
		assertEquals(Key.Shallow.of(lexRowOuN), Key.Shallow.of(lexRowAuN)); // because discriminant is same
		assertEquals(Key.PWN.of(lexRowOu), Key.PWN.of(lexRowAu));
	}

	@Test
	public void testMobile()
	{
		String wordMobile = "mobile";
		Pronunciation pMobile = new Pronunciation("ˈməʊbaɪl", null);
		Pronunciation pMobileGB = new Pronunciation("ˈməʊbaɪl", "GB");
		Pronunciation pMobileUS = new Pronunciation("ˈmoʊbil", "US");
		Pronunciation[] paMobile1 = new Pronunciation[] { pMobile, pMobileGB, pMobileUS };
		Pronunciation[] paMobile2 = new Pronunciation[] { pMobile, pMobileGB, pMobileUS };

		Lex lexMobile0 = new Lex("source1", wordMobile, "n");
		Lex lexMobile1 = new Lex("source1", wordMobile, "n").setPronunciations(paMobile1);
		Lex lexMobile2 = new Lex("source2", wordMobile, "n").setPronunciations(paMobile2);
		assertNotEquals(lexMobile0, lexMobile1);
		assertNotEquals(lexMobile1, lexMobile2);

		assertEquals(Key.OEWN.of(lexMobile1), Key.OEWN.of(lexMobile2));
		assertEquals(Key.Shallow.of(lexMobile1), Key.Shallow.of(lexMobile2));
		assertEquals(Key.PWN.of(lexMobile1), Key.PWN.of(lexMobile2));

		assertNotEquals(Key.OEWN.of(lexMobile1), Key.OEWN.of(lexMobile0));
		assertEquals(Key.Shallow.of(lexMobile1), Key.Shallow.of(lexMobile0)); // because discriminant is both null
		assertEquals(Key.PWN.of(lexMobile1), Key.PWN.of(lexMobile0));
	}

	@Test
	public void testCritical()
	{
		String wordCritical = "critical";
		Lex lexCriticalA = new Lex("source1", wordCritical, "a");
		Lex lexCriticalS = new Lex("source2", wordCritical, "s");

		assertNotEquals(lexCriticalA, lexCriticalS);

		assertNotEquals(Key.OEWN.of(lexCriticalA), Key.OEWN.of(lexCriticalS));
		assertNotEquals(Key.Shallow.of(lexCriticalA), Key.Shallow.of(lexCriticalS));
		assertEquals(Key.PWN.of(lexCriticalA), Key.PWN.of(lexCriticalS));

		assertEquals(Key.Pos.of(lexCriticalA), Key.Pos.of(lexCriticalS)); // A and S are merged
	}

	@Test
	public void testCapitalisation()
	{
		String wordEarthL = "earth";
		String wordEarthU = "Earth";
		Lex lexEarthL = new Lex("source1", wordEarthL, "n");
		Lex lexEarthU = new Lex("source2", wordEarthU, "n");

		assertNotEquals(lexEarthL, lexEarthU);
		assertNotEquals(Key.OEWN.of(lexEarthL), Key.OEWN.of(lexEarthU));
		assertNotEquals(Key.Shallow.of(lexEarthL), Key.Shallow.of(lexEarthU));
		assertNotEquals(Key.PWN.of(lexEarthL), Key.PWN.of(lexEarthU));

		assertEquals(Key.IC.of(lexEarthL), Key.IC.of(lexEarthU));
	}

	@Test
	public void testPronunciations()
	{
		Pronunciation pMobile = new Pronunciation("ˈməʊbaɪl", null);
		Pronunciation pMobileGB = new Pronunciation("ˈməʊbaɪl", "GB");
		Pronunciation pMobileUS = new Pronunciation("ˈmoʊbil", "US");
		Pronunciation[] paMobile1 = new Pronunciation[] { pMobile, pMobileGB, pMobileUS };
		Pronunciation[] paMobile2 = new Pronunciation[] { pMobile, pMobileGB, pMobileUS };
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
		assertArrayNotEquals(paMobile1, new Pronunciation[] { pMobileGB, pMobile, pMobileUS });
		assertArrayNotEquals(paMobile2, new Pronunciation[] { pMobileGB, pMobileUS, pMobile });
		assertArrayEquals(paMobile1, new Pronunciation[] { pMobile, pMobileGB, pMobileUS });
		assertArrayEquals(paMobile2, new Pronunciation[] { pMobile, pMobileGB, pMobileUS });

		assertEquals(psMobile1, Set.of(paMobile1));
		assertEquals(psMobile2, Set.of(paMobile2));
		assertEquals(psMobile1, Set.of(new Pronunciation[] { pMobileGB, pMobile, pMobileUS }));
		assertEquals(psMobile2, Set.of(new Pronunciation[] { pMobile, pMobileGB, pMobileUS }));
		assertEquals(psMobile1,
				Set.of(new Pronunciation[] { new Pronunciation("ˈməʊbaɪl", "GB"), new Pronunciation("ˈməʊbaɪl", null), new Pronunciation("ˈmoʊbil", "US") }));
		assertEquals(psMobile2,
				Set.of(new Pronunciation[] { new Pronunciation("ˈməʊbaɪl", null), new Pronunciation("ˈməʊbaɪl", "GB"), new Pronunciation("ˈmoʊbil", "US") }));

		assertNotEquals(paMobile1, paMobile2);
		assertEquals(psMobile1, psMobile2);

		Pronunciation pRowOu = new Pronunciation("ɹəʊ", null);
		Pronunciation pRowAu = new Pronunciation("ɹaʊ", null);
		Pronunciation[] paRow1 = new Pronunciation[] { pRowOu, pRowAu };
		Pronunciation[] paRow2 = new Pronunciation[] { pRowAu, pRowOu };
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