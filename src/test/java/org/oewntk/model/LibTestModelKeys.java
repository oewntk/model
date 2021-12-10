/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.PrintStream;
import java.util.List;

public class LibTestModelKeys
{
	public static void testEarth(final CoreModel model, final PrintStream ps)
	{
		String lemma = "Earth";
		Pronunciation pEarthGB = Pronunciation.ipa("ɜːθ", "GB");
		Pronunciation pEarthUS = Pronunciation.ipa("ɝθ", "US");
		dump(lemma, 'n', 'n', model, ps);

		ps.println();

		Key.IC key_ic = new Key.IC(lemma, 'n', pEarthGB, pEarthUS);
		ps.println(key_ic.toLongString());
		Lex[] lexes_ic = key_ic.apply(model.lexesByLemma);
		for (var lex : lexes_ic)
		{
			ps.println("\t" + lex);
		}

		Key.IC key_pwn = new Key.IC(lemma, 'n', pEarthGB, pEarthUS);
		ps.println(key_pwn.toLongString());
		Lex[] lexes_pwn = key_pwn.apply(model.lexesByLemma);
		for (var lex : lexes_pwn)
		{
			ps.println("\t" + lex);
		}
	}

	public static void testBaroque(final CoreModel model, final PrintStream ps)
	{
		String lemma = "baroque";
		Pronunciation pBaroqueGB = Pronunciation.ipa("bəˈɹɒk", "GB");
		Pronunciation pBaroqueUS = Pronunciation.ipa("bəˈɹoʊk", "US");

		dump(lemma, 'a', 's', model, ps);

		Key.IC key1_ic = new Key.IC(lemma, 'a', pBaroqueGB, pBaroqueUS);
		ps.println(key1_ic.toLongString());
		Lex[] lexes1 = key1_ic.apply(model.lexesByLemma);
		for (var lex : lexes1)
		{
			ps.println("\t" + lex);
		}

		Key.IC key2_ic = new Key.IC(lemma, 's', pBaroqueGB, pBaroqueUS);
		ps.println(key2_ic.toLongString());
		Lex[] lexes2 = key2_ic.apply(model.lexesByLemma);
		for (var lex : lexes2)
		{
			ps.println("\t" + lex);
		}

		Key.IC key3_ic = new Key.IC(lemma, 'a', pBaroqueUS, pBaroqueGB);
		ps.println(key3_ic.toLongString());
		Lex[] lexes3 = key1_ic.apply(model.lexesByLemma);
		for (var lex : lexes3)
		{
			ps.println("\t" + lex);
		}

		lemma = "Baroque";
		Key.IC key4_ic = new Key.IC(lemma, 'a', pBaroqueUS, pBaroqueGB);
		ps.println(key4_ic.toLongString());
		Lex[] lexes4 = key1_ic.apply(model.lexesByLemma);
		for (var lex : lexes4)
		{
			ps.println("\t" + lex);
		}
	}

	public static void testMobile(final CoreModel model, final PrintStream ps)
	{
		String lemma = "mobile";
		Pronunciation pMobileGB = Pronunciation.ipa("ˈməʊbaɪl", "GB");
		Pronunciation pMobileUS = Pronunciation.ipa("ˈmoʊbil", "US");
		dump(lemma, 'n', 'n', model, ps);

		Key.IC key1_ic = new Key.IC(lemma, 'n', pMobileGB, pMobileUS);
		ps.println(key1_ic.toLongString());
		Lex[] lexes1 = key1_ic.apply(model.lexesByLemma);
		for (var lex : lexes1)
		{
			ps.println("\t" + lex);
		}

		Key.IC key2_ic = new Key.IC(lemma, 'n', pMobileGB, pMobileUS);
		ps.println(key2_ic.toLongString());
		Lex[] lexes2 = key2_ic.apply(model.lexesByLemma);
		for (var lex : lexes2)
		{
			ps.println("\t" + lex);
		}

		lemma = "Mobile";
		Key.IC key1u_ic = new Key.IC(lemma, 'n', pMobileGB, pMobileUS);
		ps.println(key1u_ic.toLongString());
		Lex[] lexes1u = key1u_ic.apply(model.lexesByLemma);
		for (var lex : lexes1u)
		{
			ps.println("\t" + lex);
		}

		Key.IC key2u_ic = new Key.IC(lemma, 'n', pMobileGB, pMobileUS);
		ps.println(key2u_ic.toLongString());
		Lex[] lexes2u = key2u_ic.apply(model.lexesByLemma);
		for (var lex : lexes2u)
		{
			ps.println("\t" + lex);
		}

		lemma = "MOBILE";
		Key.IC key3u_ic = new Key.IC(lemma, 'n', pMobileGB, pMobileUS);
		ps.println(key3u_ic.toLongString());
		Lex[] lexes3u = key1u_ic.apply(model.lexesByLemma);
		for (var lex : lexes3u)
		{
			ps.println("\t" + lex);
		}
	}

	public static void testBass(final CoreModel model, final PrintStream ps)
	{
		String lemma = "bass";
		Pronunciation pBass1 = Pronunciation.ipa("beɪs");
		Pronunciation pBass2 = Pronunciation.ipa("bæs");
		String d1 = "-1";
		String d2 = "-2";
		dump(lemma, 'n', 'n', model, ps);

		Key.OEWN key1 = new Key.OEWN(lemma, 'n', pBass1);
		ps.println(key1.toLongString());
		Lex lex1 = key1.apply(model.lexesByLemma);
		ps.println("\t" + lex1);

		Key.Shallow key1_sh = new Key.Shallow(lemma, 'n', d1);
		ps.println(key1_sh.toLongString());
		Lex lex1_sh = key1_sh.apply(model.lexesByLemma);
		ps.println("\t" + lex1_sh);

		Key.OEWN key2 = new Key.OEWN(lemma, 'n', pBass2);
		ps.println(key2.toLongString());
		Lex lex2 = key2.apply(model.lexesByLemma);
		ps.println("\t" + lex2);

		Key.Shallow key2_sh = new Key.Shallow(lemma, 'n', d2);
		ps.println(key2_sh.toLongString());
		Lex lex2_sh = key2_sh.apply(model.lexesByLemma);
		ps.println("\t" + lex2_sh);
	}

	public static void testRow(final CoreModel model, final PrintStream ps)
	{
		String lemma = "row";
		Pronunciation pRowOu = Pronunciation.ipa("ɹəʊ" );
		Pronunciation pRowAu = Pronunciation.ipa("ɹaʊ" );
		String d1 = "-1";
		String d2 = "-2";
		dump(lemma, 'n', 'n', model, ps);

		Key.OEWN key1 = new Key.OEWN(lemma, 'n', pRowOu);
		ps.println(key1.toLongString());
		Lex lex1 = key1.apply(model.lexesByLemma);
		ps.println("\t" + lex1);

		Key.Shallow key1_sh = new Key.Shallow(lemma, 'n', d1);
		ps.println(key1_sh.toLongString());
		Lex lex1_sh = key1_sh.apply(model.lexesByLemma);
		ps.println("\t" + lex1_sh);

		Key.OEWN key2 = new Key.OEWN(lemma, 'n', pRowAu);
		ps.println(key2.toLongString());
		Lex lex2 = key2.apply(model.lexesByLemma);
		ps.println("\t" + lex2);

		Key.Shallow key2_sh = new Key.Shallow(lemma, 'n', d2);
		ps.println(key2_sh.toLongString());
		Lex lex2_sh = key2_sh.apply(model.lexesByLemma);
		ps.println("\t" + lex2_sh);
	}

	public static void testCritical(final CoreModel model, final PrintStream ps)
	{
		String lemma = "critical";
		Pronunciation pCritical = Pronunciation.ipa("ˈkɹɪtɪkəl");
		dump(lemma, 'a', 's', model, ps);

		Key.OEWN key = new Key.OEWN(lemma, 'a', pCritical);
		ps.println(key.toLongString());
		Lex lex = key.apply(model.lexesByLemma);
		ps.println("\t" + lex);

		Key.Shallow key_sh = new Key.Shallow(lemma, 's', null);
		ps.println(key_sh.toLongString());
		Lex lex_sh = key_sh.apply(model.lexesByLemma);
		ps.println("\t" + lex_sh);

		Key.Pos key_pos = new Key.Pos(lemma, 'a', pCritical);
		ps.println(key_pos.toLongString());
		Lex lex_pos = key_pos.apply(model.lexesByLemma); // first
		ps.println("\t" + lex_pos);

		Key.PWN key_pwn = new Key.PWN(lemma, 'a');
		ps.println(key_pwn.toLongString());
		Lex[] lexes_pwn = key_pwn.apply(model.lexesByLemma);
		for (Lex lex_pwn : lexes_pwn)
		{
			ps.println("\t" + lex_pwn);
		}
	}

	public static void dump(final String lemma, final char posFilter, final char typeFilter, final CoreModel model, final PrintStream ps)
	{
		ps.println("----------");
		ps.println("ALL LEMMAS " + lemma);
		List<Lex> lexes = Finder.getLexes(model.lexesByLemma, lemma);
		for (var lex : lexes)
		{
			ps.println("\t" + lex);
		}

		ps.println("ALL LEMMAS WITH POS " + posFilter + " " + lemma);
		Lex[] lexes_p = Finder.getLexesHavingPos(model.lexesByLemma, lemma, posFilter);
		for (var lex : lexes_p)
		{
			ps.println("\t" + lex);
		}

		ps.println("ALL LEMMAS WITH TYPE " + typeFilter + " " + lemma);
		Lex[] lexes_t = Finder.getLexesHavingType(model.lexesByLemma, lemma, typeFilter);
		for (var lex : lexes_t)
		{
			ps.println("\t" + lex);
		}

		ps.println("ALL LEMMAS IGNORE CASE " + lemma);
		Lex[] lexes_u = Finder.getLcLexes(model.lexesByLemma, lemma);
		for (var lex : lexes_u)
		{
			ps.println("\t" + lex);
		}

		ps.println("ALL LEMMAS IGNORE CASE WITH POS " + posFilter + " " + lemma);
		Lex[] lexes_up = Finder.getLcLexesHavingPos(model.lexesByLemma, lemma, posFilter);
		for (var lex : lexes_up)
		{
			ps.println("\t" + lex);
		}

		ps.println("ALL LEMMAS IGNORE CASE WITH TYPE " + typeFilter + " " + lemma);
		Lex[] lexes_ut = Finder.getLcLexesHavingType(model.lexesByLemma, lemma, typeFilter);
		for (var lex : lexes_ut)
		{
			ps.println("\t" + lex);
		}
		ps.println("----------");
	}
}
