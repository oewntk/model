/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class LibTestModelKeys
{
	public static void testEarthMulti(final CoreModel model, final PrintStream ps)
	{
		Pronunciation pEarthGB = Pronunciation.ipa("ɜːθ", "GB");
		Pronunciation pEarthUS = Pronunciation.ipa("ɝθ", "US");
		Key.IC key_uc = new Key.IC("Earth", 'n', pEarthGB, pEarthUS);
		Key.IC key_lc = new Key.IC("earth", 'n', pEarthGB, pEarthUS);
		Key.IC key_pwn_uc = new Key.IC("Earth", 'n', pEarthGB, pEarthUS);
		Key.IC key_pwn_lc = new Key.IC("earth", 'n', pEarthGB, pEarthUS);

		testLemmaKeysMulti(model, ps, "Earth", 'n', 'n', key_uc, key_lc, key_pwn_uc, key_pwn_lc);
	}

	public static void testEarthMono(final CoreModel model, final PrintStream ps)
	{
		Pronunciation pEarthGB = Pronunciation.ipa("ɜːθ", "GB");
		Pronunciation pEarthUS = Pronunciation.ipa("ɝθ", "US");
		Key.OEWN key_uc = new Key.OEWN("Earth", 'n', pEarthGB, pEarthUS);
		Key.OEWN key_lc = new Key.OEWN("earth", 'n', pEarthGB, pEarthUS);

		testLemmaKeysMono(model, ps, "Earth", 'n', 'n', key_uc, key_lc);
	}

	public static void testBaroqueMulti(final CoreModel model, final PrintStream ps)
	{
		Pronunciation pBaroqueGB = Pronunciation.ipa("bəˈɹɒk", "GB");
		Pronunciation pBaroqueUS = Pronunciation.ipa("bəˈɹoʊk", "US");
		Key.IC key1_ic = new Key.IC("baroque", 'a', pBaroqueGB, pBaroqueUS);
		Key.IC key2_ic = new Key.IC("baroque", 's', pBaroqueGB, pBaroqueUS);
		Key.IC key3_ic = new Key.IC("baroque", 'a', pBaroqueUS, pBaroqueGB);
		Key.IC key4_ic = new Key.IC("Baroque", 'a', pBaroqueUS, pBaroqueGB);

		testLemmaKeysMulti(model, ps, "Baroque", 'a', 's', key1_ic, key2_ic, key3_ic, key4_ic);
	}

	public static void testBaroqueMono(final CoreModel model, final PrintStream ps)
	{
		Pronunciation pBaroqueGB = Pronunciation.ipa("bəˈɹɒk", "GB");
		Pronunciation pBaroqueUS = Pronunciation.ipa("bəˈɹoʊk", "US");
		Key.OEWN key_uc = new Key.OEWN("Baroque", 'a', pBaroqueUS, pBaroqueGB);
		Key.OEWN key_lc = new Key.OEWN("baroque", 'a', pBaroqueGB, pBaroqueUS);

		testLemmaKeysMono(model, ps, "Baroque", 'a', 's', key_uc, key_lc);
	}

	public static void testMobile(final CoreModel model, final PrintStream ps)
	{
		Pronunciation pMobileGB = Pronunciation.ipa("ˈməʊbaɪl", "GB");
		Pronunciation pMobileUS = Pronunciation.ipa("ˈmoʊbil", "US");

		Key.IC key1_ic = new Key.IC("mobile", 'n', pMobileGB, pMobileUS);
		Key.IC key2_ic = new Key.IC("mobile", 'n', pMobileGB, pMobileUS);
		Key.IC key1u_ic = new Key.IC("Mobile", 'n', pMobileGB, pMobileUS);
		Key.IC key2u_ic = new Key.IC("Mobile", 'n', pMobileGB, pMobileUS);
		Key.IC key3u_ic = new Key.IC("MOBILE", 'n', pMobileGB, pMobileUS);

		testLemmaKeysMulti(model, ps, "mobile", 'n', 'n', key1_ic, key2_ic, key1u_ic, key2u_ic, key3u_ic);
	}


	public static void testBassDeep(final CoreModel model, final PrintStream ps)
	{
		testLemmaDeep(model, ps, "bass", 'n', 'n', Pronunciation.ipa("beɪs"), Pronunciation.ipa("bæs"));
	}

	public static void testBassShallow(final CoreModel model, final PrintStream ps)
	{
		testLemmaShallow(model, ps, "bass", 'n', 'n', "-1", "-2");
	}


	public static void testRowDeep(final CoreModel model, final PrintStream ps)
	{
		testLemmaDeep(model, ps, "row", 'n', 'n', Pronunciation.ipa("ɹəʊ"), Pronunciation.ipa("ɹaʊ"));
	}

	public static void testRowShallow(final CoreModel model, final PrintStream ps)
	{
		testLemmaShallow(model, ps, "row", 'n', 'n', "-1", "-2");
	}


	public static void testCriticalDeep(final CoreModel model, final PrintStream ps)
	{
		testLemmaDeep(model, ps, "critical", 'a', 'a', Pronunciation.ipa("ˈkɹɪtɪkəl"));
	}

	public static void testCriticalPos(final CoreModel model, final PrintStream ps)
	{
		testLemmaPos(model, ps, "critical", 'a', 's', Pronunciation.ipa("ˈkɹɪtɪkəl"));
	}

	public static void testCriticalPWN(final CoreModel model, final PrintStream ps)
	{
		testLemmaPWN(model, ps, "critical", 'a');
	}


	public static void testLemmaDeep(final CoreModel model, final PrintStream ps, final String lemma, final char pos, final char type, final Pronunciation... pronunciations)
	{
		dump(lemma, pos, type, model, ps);
		for (Pronunciation p : pronunciations)
		{
			Key.OEWN key1 = new Key.OEWN(lemma, type, p);
			ps.println(key1.toLongString());
			Lex lex1 = key1.apply(model.getLexesByLemma());
			ps.println("\t" + lex1);
		}
	}

	public static void testLemmaShallow(final CoreModel model, final PrintStream ps, final String lemma, final char pos, final char type, final String... discriminants)
	{
		dump(lemma, pos, type, model, ps);
		for (String d : discriminants)
		{
			Key.Shallow key1_sh = new Key.Shallow(lemma, type, d);
			ps.println(key1_sh.toLongString());
			Lex lex1_sh = key1_sh.apply(model.getLexesByLemma());
			ps.println("\t" + lex1_sh);
		}
	}

	public static void testLemmaPos(final CoreModel model, final PrintStream ps, final String lemma, final char pos, final char type, final Pronunciation... pronunciations)
	{
		dump(lemma, pos, type, model, ps);
		for (Pronunciation p : pronunciations)
		{
			Key.Pos key_pos = new Key.Pos(lemma, pos, p);
			ps.println(key_pos.toLongString());
			Lex lex_pos = key_pos.apply(model.getLexesByLemma()); // first
			ps.println("\t" + lex_pos);
		}
	}

	public static void testLemmaPWN(final CoreModel model, final PrintStream ps, final String lemma, final char pos)
	{
		dump(lemma, pos, '\0', model, ps);

		Key.PWN key_pwn = new Key.PWN(lemma, pos);
		ps.println(key_pwn.toLongString());
		Lex[] lexes_pwn = key_pwn.apply(model.getLexesByLemma());
		for (Lex lex_pwn : lexes_pwn)
		{
			ps.println("\t" + lex_pwn);
		}
	}

	@SafeVarargs
	public static void testLemmaKeysMono(final CoreModel model, final PrintStream ps, final String lemma, final char pos, final char type, final Function<Map<String, List<Lex>>, Lex>... keys)
	{
		dump(lemma, pos, type, model, ps);
		for (var k : keys)
		{
			ps.println(k);
			Lex lex = k.apply(model.getLexesByLemma());
			ps.println("\t" + lex);
		}
	}

	@SafeVarargs
	public static void testLemmaKeysMulti(final CoreModel model, final PrintStream ps, final String lemma, final char pos, final char type, final Function<Map<String, List<Lex>>, Lex[]>... keys)
	{
		dump(lemma, pos, type, model, ps);
		for (var k : keys)
		{
			ps.println(k);
			Lex[] lexes = k.apply(model.getLexesByLemma());
			for (var lex : lexes)
			{
				ps.println("\t" + lex);
			}
		}
	}

	public static void dump(final String lemma, final char posFilter, final char typeFilter, final CoreModel model, final PrintStream ps)
	{
		ps.println("----------");
		ps.println("ALL LEMMAS " + lemma);
		List<Lex> lexes = Finder.getLexes(model.getLexesByLemma(), lemma);
		for (var lex : lexes)
		{
			ps.println("\t" + lex);
		}

		ps.println("ALL LEMMAS WITH POS " + posFilter + " " + lemma);
		Lex[] lexes_p = Finder.getLexesHavingPos(model.getLexesByLemma(), lemma, posFilter);
		for (var lex : lexes_p)
		{
			ps.println("\t" + lex);
		}

		if (typeFilter != '\0')
		{
			ps.println("ALL LEMMAS WITH TYPE " + typeFilter + " " + lemma);
			Lex[] lexes_t = Finder.getLexesHavingType(model.getLexesByLemma(), lemma, typeFilter);
			for (var lex : lexes_t)
			{
				ps.println("\t" + lex);
			}
		}

		ps.println("ALL LEMMAS IGNORE CASE " + lemma);
		Lex[] lexes_u = Finder.getLcLexes(model.getLexesByLemma(), lemma);
		for (var lex : lexes_u)
		{
			ps.println("\t" + lex);
		}

		ps.println("ALL LEMMAS IGNORE CASE WITH POS " + posFilter + " " + lemma);
		Lex[] lexes_up = Finder.getLcLexesHavingPos(model.getLexesByLemma(), lemma, posFilter);
		for (var lex : lexes_up)
		{
			ps.println("\t" + lex);
		}

		if (typeFilter != '\0')
		{
			ps.println("ALL LEMMAS IGNORE CASE WITH TYPE " + typeFilter + " " + lemma);
			Lex[] lexes_ut = Finder.getLcLexesHavingType(model.getLexesByLemma(), lemma, typeFilter);
			for (var lex : lexes_ut)
			{
				ps.println("\t" + lex);
			}
		}
		ps.println("----------");
	}
}
