/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.PrintStream;
import java.util.Collection;

public class LibTestModelKeys
{
	public static int[] testEarthMulti(final CoreModel model, final PrintStream ps)
	{
		Pronunciation pEarthGB = Pronunciation.ipa("ɜːθ", "GB");
		Pronunciation pEarthUS = Pronunciation.ipa("ɝθ", "US");
		Key.IC key_uc = new Key.IC("Earth", 'n', pEarthGB, pEarthUS);
		Key.IC key_lc = new Key.IC("earth", 'n', pEarthGB, pEarthUS);
		Key.IC key_pwn_uc = new Key.IC("Earth", 'n', pEarthGB, pEarthUS);
		Key.IC key_pwn_lc = new Key.IC("earth", 'n', pEarthGB, pEarthUS);
		return testLemmaKeysMulti(model, ps, "Earth", 'n', 'n', key_uc, key_lc, key_pwn_uc, key_pwn_lc);
	}

	public static int[] testEarthMultiNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		Key.IC key_uc = new Key.IC("Earth", 'n');
		Key.IC key_lc = new Key.IC("earth", 'n');
		return testLemmaKeysMulti(model, ps, "Earth", 'n', 'n', key_uc, key_lc);
	}

	public static int[] testEarthMono(final CoreModel model, final PrintStream ps)
	{
		Pronunciation pEarthGB = Pronunciation.ipa("ɜːθ", "GB");
		Pronunciation pEarthUS = Pronunciation.ipa("ɝθ", "US");
		Key.OEWN key_uc = new Key.OEWN("Earth", 'n', pEarthGB, pEarthUS);
		Key.OEWN key_lc = new Key.OEWN("earth", 'n', pEarthGB, pEarthUS);
		return testLemmaKeysMono(model, ps, "Earth", 'n', 'n', key_uc, key_lc);
	}

	public static int[] testEarthMonoNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		Key.OEWN key_uc = new Key.OEWN("Earth", 'n');
		Key.OEWN key_lc = new Key.OEWN("earth", 'n');
		return testLemmaKeysMono(model, ps, "Earth", 'n', 'n', key_uc, key_lc);
	}

	public static int[] testBaroqueMulti(final CoreModel model, final PrintStream ps)
	{
		Pronunciation pBaroqueGB = Pronunciation.ipa("bəˈɹɒk", "GB");
		Pronunciation pBaroqueUS = Pronunciation.ipa("bəˈɹoʊk", "US");
		Key.IC key1_ic = new Key.IC("baroque", 'a', pBaroqueGB, pBaroqueUS);
		Key.IC key2_ic = new Key.IC("baroque", 's', pBaroqueGB, pBaroqueUS);
		Key.IC key3_ic = new Key.IC("baroque", 'a', pBaroqueUS, pBaroqueGB);
		Key.IC key4_ic = new Key.IC("Baroque", 'a', pBaroqueUS, pBaroqueGB);
		return testLemmaKeysMulti(model, ps, "Baroque", 'a', 's', key1_ic, key2_ic, key3_ic, key4_ic);
	}

	public static int[] testBaroqueMultiNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		Key.IC key1_ic = new Key.IC("baroque", 'a');
		Key.IC key2_ic = new Key.IC("baroque", 's');
		Key.IC key3_ic = new Key.IC("Baroque", 'a');
		Key.IC key4_ic = new Key.IC("Baroque", 's');
		return testLemmaKeysMulti(model, ps, "Baroque", 'a', 's', key1_ic, key2_ic, key3_ic, key4_ic);
	}

	public static int[] testBaroqueMono(final CoreModel model, final PrintStream ps)
	{
		Pronunciation pBaroqueGB = Pronunciation.ipa("bəˈɹɒk", "GB");
		Pronunciation pBaroqueUS = Pronunciation.ipa("bəˈɹoʊk", "US");
		Key.OEWN key_uc = new Key.OEWN("Baroque", 'a', pBaroqueUS, pBaroqueGB);
		Key.OEWN key_lc = new Key.OEWN("baroque", 'a', pBaroqueGB, pBaroqueUS);
		return testLemmaKeysMono(model, ps, "Baroque", 'a', 's', key_uc, key_lc);
	}

	public static int[] testBaroqueMonoNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		Key.OEWN key_auc = new Key.OEWN("Baroque", 'a');
		Key.OEWN key_alc = new Key.OEWN("baroque", 'a');
		Key.OEWN key_suc = new Key.OEWN("Baroque", 's');
		Key.OEWN key_slc = new Key.OEWN("baroque", 's');
		return testLemmaKeysMono(model, ps, "Baroque", 'a', 'a', key_auc, key_alc, key_suc, key_slc);
	}

	public static int[] testMobile(final CoreModel model, final PrintStream ps)
	{
		Pronunciation pMobileGB = Pronunciation.ipa("ˈməʊbaɪl", "GB");
		Pronunciation pMobileUS = Pronunciation.ipa("ˈmoʊbil", "US");
		Key.IC key1_ic = new Key.IC("mobile", 'n', pMobileGB, pMobileUS);
		Key.IC key2_ic = new Key.IC("mobile", 'n', pMobileGB, pMobileUS);
		Key.IC key1u_ic = new Key.IC("Mobile", 'n', pMobileGB, pMobileUS);
		Key.IC key2u_ic = new Key.IC("Mobile", 'n', pMobileGB, pMobileUS);
		Key.IC key3u_ic = new Key.IC("MOBILE", 'n', pMobileGB, pMobileUS);
		return testLemmaKeysMulti(model, ps, "mobile", 'n', 'n', key1_ic, key2_ic, key1u_ic, key2u_ic, key3u_ic);
	}

	public static int[] testRowMonoNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		Key.OEWN key_oewn = new Key.OEWN("row", 'n');
		Key.Shallow key_sh = new Key.Shallow("row", 'n', null);
		Key.Pos key_pos = new Key.Pos("row", 'n');
		return testLemmaKeysMono(model, ps, "row", 'n', 'n', key_oewn, key_sh, key_pos);
	}

	public static int[] testRowMultiNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		Key.IC key_ic = new Key.IC("row", 'n');
		Key.PWN key_pwn = new Key.PWN("row", 'n');
		return testLemmaKeysMulti(model, ps, "row", 'n', 'n', key_ic, key_pwn);
	}

	public static int[] testBassMonoNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		Key.OEWN key_oewn = new Key.OEWN("bass", 'n');
		Key.Shallow key_sh = new Key.Shallow("bass", 'n', null);
		Key.Pos key_pos = new Key.Pos("bass", 'n');
		return testLemmaKeysMono(model, ps, "bass", 'n', 'n', key_oewn, key_sh, key_pos);
	}

	public static int[] testBassMultiNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		Key.IC key_ic = new Key.IC("bass", 'n');
		Key.PWN key_pwn = new Key.PWN("bass", 'n');
		return testLemmaKeysMulti(model, ps, "bass", 'n', 'n', key_ic, key_pwn);
	}

	public static int[] testMobileNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		Key.IC key1_ic = new Key.IC("mobile", 'n');
		Key.IC key2_ic = new Key.IC("Mobile", 'n');
		Key.IC key3_ic = new Key.IC("MOBILE", 'n');
		return testLemmaKeysMulti(model, ps, "mobile", 'n', 'n', key1_ic, key2_ic, key3_ic);
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

	public static void testCriticalDeepNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		testLemmaDeep(model, ps, "critical", 'a', 'a');
	}

	public static void testCriticalPos(final CoreModel model, final PrintStream ps)
	{
		testLemmaPos(model, ps, "critical", 'a', 's', Pronunciation.ipa("ˈkɹɪtɪkəl"));
	}

	public static void testCriticalPosNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		testLemmaPos(model, ps, "critical", 'a', 's');
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
			Lex lex1 = key1.apply(model);
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
			Lex lex1_sh = key1_sh.apply(model);
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
			Lex lex_pos = key_pos.apply(model); // find first
			ps.println("\t" + lex_pos);
		}
	}

	public static void testLemmaPWN(final CoreModel model, final PrintStream ps, final String lemma, final char pos)
	{
		dump(lemma, pos, '\0', model, ps);

		Key.PWN key_pwn = new Key.PWN(lemma, pos);
		ps.println(key_pwn.toLongString());
		Lex[] lexes_pwn = key_pwn.apply(model);
		for (Lex lex_pwn : lexes_pwn)
		{
			ps.println("\t" + lex_pwn);
		}
	}

	@SafeVarargs
	public static int[] testLemmaKeysMono(final CoreModel model, final PrintStream ps, final String lemma, final char pos, final char type, final Key.Mono... keys)
	{
		dump(lemma, pos, type, model, ps);

		int[] r = new int[keys.length];
		int i = 0;
		for (var k : keys)
		{
			ps.printf("Key %s %s%n", k.getClass().getSimpleName(), k);
			try
			{
				Lex lex = k.apply(model);
				ps.println("\t" + lex);
				r[i] = 1;
			}
			catch (Exception e)
			{
				r[i] = 0;
			}
			i++;
		}
		return r;
	}

	@SafeVarargs
	public static int[] testLemmaKeysMulti(final CoreModel model, final PrintStream ps, final String lemma, final char pos, final char type, final Key.Multi... keys)
	{
		dump(lemma, pos, type, model, ps);

		int[] r = new int[keys.length];
		int i = 0;
		for (var k : keys)
		{
			ps.printf("Key %s %s%n", k.getClass().getSimpleName(), k);
			Lex[] lexes = k.apply(model);
			r[i] = lexes == null ? 0 : lexes.length;
			for (var lex : lexes)
			{
				ps.println("\t" + lex);
			}
			i++;
		}
		return r;
	}

	public static void dump(final String lemma, final char posFilter, final char typeFilter, final CoreModel model, final PrintStream ps)
	{
		ps.println("----------");
		ps.println("ALL LEMMAS " + lemma);
		Collection<Lex> lexes = Finder.getLexes(model, lemma);
		for (var lex : lexes)
		{
			ps.println("\t" + lex);
		}

		ps.println("ALL LEMMAS WITH POS " + posFilter + " " + lemma);
		Lex[] lexes_p = Finder.getLexesHavingPos(model, lemma, posFilter);
		for (var lex : lexes_p)
		{
			ps.println("\t" + lex);
		}

		if (typeFilter != '\0')
		{
			ps.println("ALL LEMMAS WITH TYPE " + typeFilter + " " + lemma);
			Lex[] lexes_t = Finder.getLexesHavingType(model, lemma, typeFilter);
			for (var lex : lexes_t)
			{
				ps.println("\t" + lex);
			}
		}

		ps.println("ALL LEMMAS IGNORE CASE " + lemma);
		Lex[] lexes_u = Finder.getLcLexes(model, lemma);
		for (var lex : lexes_u)
		{
			ps.println("\t" + lex);
		}

		ps.println("ALL LEMMAS IGNORE CASE WITH POS " + posFilter + " " + lemma);
		Lex[] lexes_up = Finder.getLcLexesHavingPos(model, lemma, posFilter);
		for (var lex : lexes_up)
		{
			ps.println("\t" + lex);
		}

		if (typeFilter != '\0')
		{
			ps.println("ALL LEMMAS IGNORE CASE WITH TYPE " + typeFilter + " " + lemma);
			Lex[] lexes_ut = Finder.getLcLexesHavingType(model, lemma, typeFilter);
			for (var lex : lexes_ut)
			{
				ps.println("\t" + lex);
			}
		}
		ps.println("----------");
	}
}
