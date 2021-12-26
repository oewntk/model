/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LibTestModelKeys
{
	// M O B I L E

	public static int[] testMobile(final CoreModel model, final PrintStream ps)
	{
		Pronunciation pGB = Pronunciation.ipa("ˈməʊbaɪl", "GB");
		Pronunciation pUS = Pronunciation.ipa("ˈmoʊbil", "US");
		Pronunciation[] pronunciations = {pGB, pUS};
		return testWordMulti(model, ps, "Mobile", pronunciations, 'n');
	}

	public static int[] testMobileNoPronunciation(final CoreModel model, final PrintStream ps)
	{
		return testWordNoPronunciationMulti(model, ps, "Mobile", 'n');
	}

	// E A R T H (case)

	public static int[] testEarthMulti(final CoreModel model, final PrintStream ps)
	{
		return testWordNoPronunciationMulti(model, ps, "Earth", 'n');
	}

	public static int[] testEarthMono(final CoreModel model, final PrintStream ps)
	{
		return testWordNoPronunciationMono(model, ps, "Earth", 'n');
	}

	// B A R O Q U E (case)

	public static int[] testBaroqueMulti(final CoreModel model, final PrintStream ps)
	{
		return testWordNoPronunciationMulti(model, ps, "Baroque", 'a', 's');
	}

	public static int[] testBaroqueMono(final CoreModel model, final PrintStream ps)
	{
		return testWordNoPronunciationMono(model, ps, "Baroque", 'a', 's');
	}

	// C R I T I C A L (part-of-speech/type)

	public static int[] testCriticalMulti(final CoreModel model, final PrintStream ps)
	{
		return testWordNoPronunciationMulti(model, ps, "critical", 'a', 's');
	}

	public static int[] testCriticalMono(final CoreModel model, final PrintStream ps)
	{
		return testWordNoPronunciationMono(model, ps, "critical", 'a', 's');
	}

	// R O W

	public static int[] testRowDeep(final CoreModel model, final PrintStream ps)
	{
		return testPronunciations(model, ps, "row", 'n', 'n', Pronunciation.ipa("ɹəʊ"), Pronunciation.ipa("ɹaʊ"));
	}

	public static int[] testRowShallow(final CoreModel model, final PrintStream ps)
	{
		return testShallow(model, ps, "row", 'n', 'n', "-1", "-2");
	}

	public static int[] testRowNoPronunciationDeep(final CoreModel model, final PrintStream ps)
	{
		return testPronunciations(model, ps, "row", 'n', 'n');
	}

	public static int[] testRowNoPronunciationShallow(final CoreModel model, final PrintStream ps)
	{
		return testShallow(model, ps, "row", 'n', 'n');
	}

	// B A S S

	public static int[] testBassDeep(final CoreModel model, final PrintStream ps)
	{
		return testPronunciations(model, ps, "bass", 'n', 'n', Pronunciation.ipa("beɪs"), Pronunciation.ipa("bæs"));
	}

	public static int[] testBassShallow(final CoreModel model, final PrintStream ps)
	{
		return testShallow(model, ps, "bass", 'n', 'n', "-1", "-2");
	}

	public static int[] testBassNoPronunciationDeep(final CoreModel model, final PrintStream ps)
	{
		return testPronunciations(model, ps, "bass", 'n', 'n');
	}

	public static int[] testBassNoPronunciationShallow(final CoreModel model, final PrintStream ps)
	{
		return testShallow(model, ps, "bass", 'n', 'n');
	}

	// W O R D    T E S T S

	private static int[] testWordMulti(final CoreModel model, final PrintStream ps, String cased, Pronunciation[] p, char... posTypes)
	{
		String lc = cased.toLowerCase(Locale.ENGLISH);
		boolean isCased = !lc.equals(cased);

		List<KeyF.MultiValued> keys = new ArrayList<>();
		for (char posType : posTypes)
		{
			keys.add(KeyF.F_W_P_A.Multi.from(Lex::getLemma, Lex::getType, cased, posType, p));
			if (p.length > 1)
			{
				keys.add(KeyF.F_W_P_A.Multi.from(Lex::getLemma, Lex::getType, cased, posType, p[1], p[0]));
			}
			if (isCased)
			{
				keys.add(KeyF.F_W_P_A.Multi.from(Lex::getLemma, Lex::getType, lc, posType, p));
				keys.add(KeyF.F_W_P_A.Multi.from(Lex::getLCLemma, Lex::getType, lc, posType, p));
				keys.add(KeyF.F_W_P_A.Multi.from(Lex::getLCLemma, Lex::getType, lc, posType, p));
			}
			if (posType == 's' || posType == 'a')
			{
				keys.add(KeyF.F_W_P_A.Multi.from(Lex::getLemma, Lex::getPartOfSpeech, cased, posType, p));
				if (isCased)
				{
					keys.add(KeyF.F_W_P_A.Multi.from(Lex::getLemma, Lex::getPartOfSpeech, lc, posType, p));
					keys.add(KeyF.F_W_P_A.Multi.from(Lex::getLCLemma, Lex::getPartOfSpeech, lc, posType, p));
					keys.add(KeyF.F_W_P_A.Multi.from(Lex::getLCLemma, Lex::getPartOfSpeech, lc, posType, p));
				}
			}
		}
		return testKeysMulti(model, ps, cased, posTypes[0], posTypes[posTypes.length > 1 ? 1 : 0], keys.toArray(new KeyF.MultiValued[0]));
	}

	private static int[] testWordNoPronunciationMulti(final CoreModel model, final PrintStream ps, String cased, char... posTypes)
	{
		String lc = cased.toLowerCase(Locale.ENGLISH);
		boolean isCased = !lc.equals(cased);

		List<KeyF.MultiValued> keys = new ArrayList<>();
		for (char posType : posTypes)
		{
			keys.add(KeyF.F_W_P.Multi.from(Lex::getLemma, Lex::getType, cased, posType));
			if (isCased)
			{
				keys.add(KeyF.F_W_P.Multi.from(Lex::getLemma, Lex::getType, lc, posType));
				keys.add(KeyF.F_W_P.Multi.from(Lex::getLCLemma, Lex::getType, lc, posType));
				keys.add(KeyF.F_W_P.Multi.from(Lex::getLCLemma, Lex::getType, lc, posType));
			}
			if (posType == 's' || posType == 'a')
			{
				keys.add(KeyF.F_W_P.Multi.from(Lex::getLemma, Lex::getPartOfSpeech, cased, posType));
				if (isCased)
				{
					keys.add(KeyF.F_W_P.Multi.from(Lex::getLemma, Lex::getPartOfSpeech, cased.toLowerCase(Locale.ENGLISH), posType));
					keys.add(KeyF.F_W_P.Multi.from(Lex::getLCLemma, Lex::getPartOfSpeech, cased.toLowerCase(Locale.ENGLISH), posType));
					keys.add(KeyF.F_W_P.Multi.from(Lex::getLCLemma, Lex::getPartOfSpeech, cased.toLowerCase(Locale.ENGLISH), posType));
				}
			}
		}
		return testKeysMulti(model, ps, cased, posTypes[0], posTypes[posTypes.length > 1 ? 1 : 0], keys.toArray(new KeyF.MultiValued[0]));
	}

	private static int[] testWordMono(final CoreModel model, final PrintStream ps, String cased, Pronunciation[] p, char... posTypes)
	{
		String lc = cased.toLowerCase(Locale.ENGLISH);
		boolean isCased = !lc.equals(cased);

		List<KeyF.MonoValued> keys = new ArrayList<>();
		for (char posType : posTypes)
		{
			keys.add(KeyF.F_W_P_A.Mono.from(Lex::getLemma, Lex::getType, cased, posType, p));
			if (p.length > 1)
			{
				keys.add(KeyF.F_W_P_A.Mono.from(Lex::getLemma, Lex::getType, cased, posType, p[1], p[0]));
			}
			if (isCased)
			{
				keys.add(KeyF.F_W_P_A.Mono.from(Lex::getLemma, Lex::getType, cased.toLowerCase(Locale.ENGLISH), posType, p));
				keys.add(KeyF.F_W_P_A.Mono.from(Lex::getLCLemma, Lex::getType, cased.toLowerCase(Locale.ENGLISH), posType, p));
				keys.add(KeyF.F_W_P_A.Mono.from(Lex::getLCLemma, Lex::getType, cased.toLowerCase(Locale.ENGLISH), posType, p));
			}

			if (posType == 's' || posType == 'a')
			{
				keys.add(KeyF.F_W_P_A.Mono.from(Lex::getLemma, Lex::getPartOfSpeech, cased, posType, p));
				if (isCased)
				{
					keys.add(KeyF.F_W_P_A.Mono.from(Lex::getLemma, Lex::getPartOfSpeech, cased.toLowerCase(Locale.ENGLISH), posType, p));
					keys.add(KeyF.F_W_P_A.Mono.from(Lex::getLCLemma, Lex::getPartOfSpeech, cased.toLowerCase(Locale.ENGLISH), posType, p));
					keys.add(KeyF.F_W_P_A.Mono.from(Lex::getLCLemma, Lex::getPartOfSpeech, cased.toLowerCase(Locale.ENGLISH), posType, p));
				}
			}
		}
		return testKeysMono(model, ps, cased, posTypes[0], posTypes[posTypes.length > 1 ? 1 : 0], keys.toArray(new KeyF.MonoValued[0]));
	}

	private static int[] testWordNoPronunciationMono(final CoreModel model, final PrintStream ps, String cased, char... posTypes)
	{
		String lc = cased.toLowerCase(Locale.ENGLISH);
		boolean isCased = !lc.equals(cased);

		List<KeyF.MonoValued> keys = new ArrayList<>();
		for (char posType : posTypes)
		{
			keys.add(KeyF.F_W_P.Mono.from(Lex::getLemma, Lex::getType, cased, posType));
			if (isCased)
			{
				keys.add(KeyF.F_W_P.Mono.from(Lex::getLemma, Lex::getType, cased.toLowerCase(Locale.ENGLISH), posType));
				keys.add(KeyF.F_W_P.Mono.from(Lex::getLCLemma, Lex::getType, cased.toLowerCase(Locale.ENGLISH), posType));
				keys.add(KeyF.F_W_P.Mono.from(Lex::getLCLemma, Lex::getType, cased.toLowerCase(Locale.ENGLISH), posType));
			}

			if (posType == 's' || posType == 'a')
			{
				keys.add(KeyF.F_W_P.Mono.from(Lex::getLemma, Lex::getPartOfSpeech, cased, posType));
				if (isCased)
				{
					keys.add(KeyF.F_W_P.Mono.from(Lex::getLemma, Lex::getPartOfSpeech, cased.toLowerCase(Locale.ENGLISH), posType));
					keys.add(KeyF.F_W_P.Mono.from(Lex::getLCLemma, Lex::getPartOfSpeech, cased.toLowerCase(Locale.ENGLISH), posType));
					keys.add(KeyF.F_W_P.Mono.from(Lex::getLCLemma, Lex::getPartOfSpeech, cased.toLowerCase(Locale.ENGLISH), posType));
				}
			}
		}
		return testKeysMono(model, ps, cased, posTypes[0], posTypes[posTypes.length > 1 ? 1 : 0], keys.toArray(new KeyF.MonoValued[0]));
	}

	// P R O N U N C I A T I O N   / S H A L L O W    T E S T S

	public static int[] testShallow(final CoreModel model, final PrintStream ps, final String lemma, final char pos, final char type, final String... discriminants)
	{
		List<KeyF.MultiValued> keys = new ArrayList<>();
		for (String d : discriminants)
		{
			keys.add(KeyF.F_W_P_D.Multi.from(Lex::getLemma, Lex::getType, lemma, type, d));
		}
		keys.add(KeyF.F_W_P.Multi.from(Lex::getLemma, Lex::getType, lemma, type));
		return testKeysMulti(model, ps, lemma, pos, type, keys.toArray(new KeyF.MultiValued[0]));
	}

	public static int[] testPronunciations(final CoreModel model, final PrintStream ps, final String lemma, final char pos, final char type, final Pronunciation... pronunciations)
	{
		List<KeyF.MultiValued> keys = new ArrayList<>();
		for (Pronunciation p : pronunciations)
		{
			keys.add(KeyF.F_W_P_A.Multi.from(Lex::getLemma, Lex::getPartOfSpeech, lemma, type, p));
		}
		keys.add(KeyF.F_W_P.Multi.from(Lex::getLemma, Lex::getType, lemma, type));
		return testKeysMulti(model, ps, lemma, pos, type, keys.toArray(new KeyF.MultiValued[0]));
	}

	// M O N O / M U L T I    T E S T S

	@SafeVarargs
	public static int[] testKeysMono(final CoreModel model, final PrintStream ps, final String lemma, final char pos, final char type, final KeyF.MonoValued... keys)
	{
		int[] r = new int[keys.length];
		int i = 0;
		for (var k : keys)
		{
			ps.printf("%s%n", k.toLongString());
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
		dumpContext(lemma, pos, type, model, ps);
		return r;
	}

	@SafeVarargs
	public static int[] testKeysMulti(final CoreModel model, final PrintStream ps, final String lemma, final char pos, final char type, final KeyF.MultiValued... keys)
	{
		int[] r = new int[keys.length];
		int i = 0;
		for (var k : keys)
		{
			ps.printf("%s%n", k.toLongString());
			Lex[] lexes = k.apply(model);
			r[i] = lexes == null ? 0 : lexes.length;
			if (lexes != null)
			{
				for (var lex : lexes)
				{
					ps.println("\t" + lex);
				}
			}
			i++;
		}
		dumpContext(lemma, pos, type, model, ps);
		return r;
	}

	// C O N T E X T

	public static void dumpContext(final String lemma, final char posFilter, final char typeFilter, final CoreModel model, final PrintStream ps)
	{
		ps.println("----------");
		/*
		ps.println("ALL LEMMAS " + lemma);
		Finder.getLexes(model, lemma).forEach(lex -> ps.println("\t" + lex));

		ps.println("ALL LEMMAS WITH POS " + posFilter + " " + lemma);
		Finder.getLexesHavingPos(model, lemma, posFilter).forEach(lex -> ps.println("\t" + lex));

		if (typeFilter != '\0')
		{
			ps.println("ALL LEMMAS WITH TYPE " + typeFilter + " " + lemma);
			Finder.getLexesHavingType(model, lemma, typeFilter).forEach(lex -> ps.println("\t" + lex));
		}
		*/
		ps.println("ALL LEMMAS IGNORE CASE " + lemma);
		Finder.getLcLexes(model, lemma).forEach(lex -> ps.println("\t" + lex));
		/*
		ps.println("ALL LEMMAS IGNORE CASE WITH POS " + posFilter + " " + lemma);
		Finder.getLcLexesHavingPos(model, lemma, posFilter).forEach(lex -> ps.println("\t" + lex));

		if (typeFilter != '\0')
		{
			ps.println("ALL LEMMAS IGNORE CASE WITH TYPE " + typeFilter + " " + lemma);
			Finder.getLcLexesHavingType(model, lemma, typeFilter).forEach(lex -> ps.println("\t" + lex));
		}
		*/
	}
}
