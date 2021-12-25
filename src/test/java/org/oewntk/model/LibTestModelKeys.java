/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.PrintStream;

public class LibTestModelKeys
{
	public static int[] testEarthMulti(final CoreModel model, final PrintStream ps)
	{
		Pronunciation pEarthGB = Pronunciation.ipa("ɜːθ", "GB");
		Pronunciation pEarthUS = Pronunciation.ipa("ɝθ", "US");
		var key_cs = KeyF.W_P_A_functional.Multi.from(Lex::getLemma, Lex::getType, "Earth", 'n', pEarthGB, pEarthUS);
		var key_lc = KeyF.W_P_A_functional.Multi.from(Lex::getLCLemma, Lex::getType, "earth", 'n', pEarthGB, pEarthUS);
		var key_pwn_cs = KeyF.W_P_functional.Multi.from(Lex::getLemma, Lex::getType, "Earth", 'n');
		var key_pwn_lc = KeyF.W_P_functional.Multi.from(Lex::getLemma, Lex::getType, "earth", 'n');
		return testLemmaKeysMulti(model, ps, "Earth", 'n', 'n', key_cs, key_lc, key_pwn_cs, key_pwn_lc);
	}

	public static int[] testEarthMultiNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		var key_cs = KeyF.W_P_functional.Multi.from(Lex::getLemma, Lex::getType, "Earth", 'n');
		var key_lc = KeyF.W_P_functional.Multi.from(Lex::getLCLemma, Lex::getType, "earth", 'n');
		return testLemmaKeysMulti(model, ps, "Earth", 'n', 'n', key_cs, key_lc);
	}

	public static int[] testEarthMono(final CoreModel model, final PrintStream ps)
	{
		Pronunciation pEarthGB = Pronunciation.ipa("ɜːθ", "GB");
		Pronunciation pEarthUS = Pronunciation.ipa("ɝθ", "US");
		var key_cs = KeyF.W_P_A_functional.Mono.from(Lex::getLemma, Lex::getType, "Earth", 'n', pEarthGB, pEarthUS);
		var key_lc = KeyF.W_P_A_functional.Mono.from(Lex::getLemma, Lex::getType, "earth", 'n', pEarthGB, pEarthUS);
		return testLemmaKeysMono(model, ps, "Earth", 'n', 'n', key_cs, key_lc);
	}

	public static int[] testEarthMonoNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		var key_cs = KeyF.W_P_functional.Mono.from(Lex::getLemma, Lex::getType, "Earth", 'n');
		var key_lc = KeyF.W_P_functional.Mono.from(Lex::getLCLemma, Lex::getType, "earth", 'n');
		return testLemmaKeysMono(model, ps, "Earth", 'n', 'n', key_cs, key_lc);
	}

	public static int[] testBaroqueMulti(final CoreModel model, final PrintStream ps)
	{
		Pronunciation pBaroqueGB = Pronunciation.ipa("bəˈɹɒk", "GB");
		Pronunciation pBaroqueUS = Pronunciation.ipa("bəˈɹoʊk", "US");
		var key1_ic = KeyF.W_P_A_functional.Multi.from(Lex::getLemma, Lex::getPartOfSpeech, "baroque", 'a', pBaroqueGB, pBaroqueUS);
		var key2_ic = KeyF.W_P_A_functional.Multi.from(Lex::getLemma, Lex::getType, "baroque", 's', pBaroqueGB, pBaroqueUS);
		var key3_ic = KeyF.W_P_A_functional.Multi.from(Lex::getLCLemma, Lex::getType, "baroque", 'a', pBaroqueUS, pBaroqueGB);
		var key4_ic = KeyF.W_P_A_functional.Multi.from(Lex::getLCLemma, Lex::getType, "Baroque", 'a', pBaroqueUS, pBaroqueGB);
		return testLemmaKeysMulti(model, ps, "Baroque", 'a', 's', key1_ic, key2_ic, key3_ic, key4_ic);
	}

	public static int[] testBaroqueMultiNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		var key1_ic = KeyF.W_P_functional.Multi.from(Lex::getLemma, Lex::getPartOfSpeech, "baroque", 'a');
		var key2_ic = KeyF.W_P_functional.Multi.from(Lex::getLemma, Lex::getType, "baroque", 's');
		var key3_ic = KeyF.W_P_functional.Multi.from(Lex::getLemma, Lex::getType, "Baroque", 'a');
		var key4_ic = KeyF.W_P_functional.Multi.from(Lex::getLemma, Lex::getType, "Baroque", 's');
		return testLemmaKeysMulti(model, ps, "Baroque", 'a', 's', key1_ic, key2_ic, key3_ic, key4_ic);
	}

	public static int[] testBaroqueMono(final CoreModel model, final PrintStream ps)
	{
		Pronunciation pBaroqueGB = Pronunciation.ipa("bəˈɹɒk", "GB");
		Pronunciation pBaroqueUS = Pronunciation.ipa("bəˈɹoʊk", "US");
		var key_cs = KeyF.W_P_A_functional.Mono.from(Lex::getLemma, Lex::getPartOfSpeech, "Baroque", 'a', pBaroqueUS, pBaroqueGB);
		var key_lc = KeyF.W_P_A_functional.Mono.from(Lex::getLCLemma, Lex::getPartOfSpeech, "baroque", 'a', pBaroqueGB, pBaroqueUS);
		return testLemmaKeysMono(model, ps, "Baroque", 'a', 's', key_cs, key_lc);
	}

	public static int[] testBaroqueMonoNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		var key_auc = KeyF.W_P_A_functional.Mono.from(Lex::getLemma, Lex::getPartOfSpeech, "Baroque", 'a');
		var key_alc = KeyF.W_P_A_functional.Mono.from(Lex::getLCLemma, Lex::getPartOfSpeech, "baroque", 'a');
		var key_suc = KeyF.W_P_A_functional.Mono.from(Lex::getLemma, Lex::getType, "Baroque", 's');
		var key_slc = KeyF.W_P_A_functional.Mono.from(Lex::getLCLemma, Lex::getType, "baroque", 's');
		return testLemmaKeysMono(model, ps, "Baroque", 'a', 'a', key_auc, key_alc, key_suc, key_slc);
	}

	public static int[] testMobile(final CoreModel model, final PrintStream ps)
	{
		Pronunciation pMobileGB = Pronunciation.ipa("ˈməʊbaɪl", "GB");
		Pronunciation pMobileUS = Pronunciation.ipa("ˈmoʊbil", "US");
		var key1_ic = KeyF.W_P_A_functional.Multi.from(Lex::getLemma, Lex::getPartOfSpeech, "mobile", 'n', pMobileGB, pMobileUS);
		var key2_ic = KeyF.W_P_A_functional.Multi.from(Lex::getLCLemma, Lex::getPartOfSpeech, "mobile", 'n', pMobileGB, pMobileUS);
		var key1u_ic = KeyF.W_P_A_functional.Multi.from(Lex::getLCLemma, Lex::getPartOfSpeech, "Mobile", 'n', pMobileGB, pMobileUS);
		var key2u_ic = KeyF.W_P_A_functional.Multi.from(Lex::getLCLemma, Lex::getPartOfSpeech, "Mobile", 'n', pMobileGB, pMobileUS);
		var key3u_ic = KeyF.W_P_A_functional.Multi.from(Lex::getLCLemma, Lex::getPartOfSpeech, "MOBILE", 'n', pMobileGB, pMobileUS);
		return testLemmaKeysMulti(model, ps, "mobile", 'n', 'n', key1_ic, key2_ic, key1u_ic, key2u_ic, key3u_ic);
	}

	public static int[] testRowMonoNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		var key_oewn = KeyF.W_P_A_functional.Mono.from(Lex::getLemma, Lex::getType, "row", 'n');
		var key_sh = KeyF.W_P_D_functional.Mono.from(Lex::getLemma, Lex::getType, "row", 'n', null);
		var key_pos = KeyF.W_P_A_functional.Mono.from(Lex::getLemma, Lex::getPartOfSpeech, "row", 'n');
		return testLemmaKeysMono(model, ps, "row", 'n', 'n', key_oewn, key_sh, key_pos);
	}

	public static int[] testRowMultiNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		var key_oewn = KeyF.W_P_A_functional.Multi.from(Lex::getLemma, Lex::getType, "row", 'n');
		var key_sh = KeyF.W_P_D_functional.Multi.from(Lex::getLemma, Lex::getType, "row", 'n', null);
		var key_pos = KeyF.W_P_A_functional.Multi.from(Lex::getLemma, Lex::getPartOfSpeech, "row", 'n');
		return testLemmaKeysMulti(model, ps, "row", 'n', 'n', key_oewn, key_sh, key_pos);
	}

	public static int[] testBassMonoNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		var key_oewn = KeyF.W_P_A_functional.Mono.from(Lex::getLemma, Lex::getType, "bass", 'n');
		var key_sh = KeyF.W_P_D_functional.Mono.from(Lex::getLemma, Lex::getType, "bass", 'n', null);
		var key_pos = KeyF.W_P_A_functional.Mono.from(Lex::getLemma, Lex::getPartOfSpeech, "bass", 'n');
		return testLemmaKeysMono(model, ps, "bass", 'n', 'n', key_oewn, key_sh, key_pos);
	}

	public static int[] testBassMultiNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		var key_oewn = KeyF.W_P_A_functional.Multi.from(Lex::getLemma, Lex::getType, "bass", 'n');
		var key_sh = KeyF.W_P_D_functional.Multi.from(Lex::getLemma, Lex::getType, "bass", 'n', null);
		var key_pos = KeyF.W_P_A_functional.Multi.from(Lex::getLemma, Lex::getPartOfSpeech, "bass", 'n');
		return testLemmaKeysMulti(model, ps, "bass", 'n', 'n', key_oewn, key_sh, key_pos);
	}

	public static int[] testMobileNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		var key1_ic = KeyF.W_P_functional.Multi.from(Lex::getLCLemma, Lex::getType, "mobile", 'n');
		var key2_ic = KeyF.W_P_functional.Multi.from(Lex::getLCLemma, Lex::getType, "Mobile", 'n');
		var key3_ic = KeyF.W_P_functional.Multi.from(Lex::getLCLemma, Lex::getType, "MOBILE", 'n');
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
			var key = KeyF.W_P_functional.Mono.from(Lex::getLemma, Lex::getType, lemma, type);
			ps.println(key.toLongString());
			Lex lex = key.apply(model);
			ps.println("\t" + lex);
		}
	}

	public static void testLemmaShallow(final CoreModel model, final PrintStream ps, final String lemma, final char pos, final char type, final String... discriminants)
	{
		dump(lemma, pos, type, model, ps);
		for (String d : discriminants)
		{
			var key = KeyF.W_P_D_functional.Mono.from(Lex::getLemma, Lex::getType, lemma, type, d);
			ps.println(key.toLongString());
			Lex lex = key.apply(model);
			ps.println("\t" + lex);
		}
	}

	public static void testLemmaPos(final CoreModel model, final PrintStream ps, final String lemma, final char pos, final char type, final Pronunciation... pronunciations)
	{
		dump(lemma, pos, type, model, ps);
		for (Pronunciation p : pronunciations)
		{
			var key = KeyF.W_P_A_functional.Mono.from(Lex::getLemma, Lex::getPartOfSpeech, lemma, type, p);
			ps.println(key.toLongString());
			Lex lex = key.apply(model);
			ps.println("\t" + lex);
		}
	}

	public static void testLemmaPWN(final CoreModel model, final PrintStream ps, final String lemma, final char pos)
	{
		dump(lemma, pos, '\0', model, ps);
		var key = KeyF.W_P_functional.Multi.from(Lex::getLemma, Lex::getPartOfSpeech, lemma, pos);
		ps.println(key.toLongString());
		Lex[] lexes_pwn = key.apply(model);
		for (Lex lex : lexes_pwn)
		{
			ps.println("\t" + lex);
		}
	}

	@SafeVarargs
	public static int[] testLemmaKeysMono(final CoreModel model, final PrintStream ps, final String lemma, final char pos, final char type, final KeyF.MonoValued... keys)
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
	public static int[] testLemmaKeysMulti(final CoreModel model, final PrintStream ps, final String lemma, final char pos, final char type, final KeyF.MultiValued... keys)
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
		Finder.getLexes(model, lemma).forEach(lex -> ps.println("\t" + lex));

		ps.println("ALL LEMMAS WITH POS " + posFilter + " " + lemma);
		Finder.getLexesHavingPos(model, lemma, posFilter).forEach(lex -> ps.println("\t" + lex));

		if (typeFilter != '\0')
		{
			ps.println("ALL LEMMAS WITH TYPE " + typeFilter + " " + lemma);
			Finder.getLexesHavingType(model, lemma, typeFilter).forEach(lex -> ps.println("\t" + lex));
		}

		ps.println("ALL LEMMAS IGNORE CASE " + lemma);
		Finder.getLcLexes(model, lemma).forEach(lex -> ps.println("\t" + lex));

		ps.println("ALL LEMMAS IGNORE CASE WITH POS " + posFilter + " " + lemma);
		Finder.getLcLexesHavingPos(model, lemma, posFilter).forEach(lex -> ps.println("\t" + lex));

		if (typeFilter != '\0')
		{
			ps.println("ALL LEMMAS IGNORE CASE WITH TYPE " + typeFilter + " " + lemma);
			Finder.getLcLexesHavingType(model, lemma, typeFilter).forEach(lex -> ps.println("\t" + lex));
		}
		ps.println("----------");
	}
}
