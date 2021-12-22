/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.joining;

public class LibTestModelQueries
{
	private static final Function<Lex, Optional<String>> nullableDiscriminant = lex -> Optional.ofNullable(lex.getDiscriminant());

	private static final Function<Lex, Optional<Pronunciation[]>> nullablePronunciations = lex -> Optional.ofNullable(lex.getPronunciations());

	private static <K> String keyToString(K k)
	{
		if (k instanceof Optional<?>)
		{
			Optional<?> o = (Optional<?>) k;
			if (o.isPresent())
			{
				Object v = o.get();
				if (v instanceof Object[])
				{
					return Arrays.toString((Object[]) v);
				}
				return v.toString();
			}
			return "-";
		}
		return k.toString();
	}

	public static void testWordByType(final CoreModel model, final String lemma, final PrintStream ps)
	{
		ps.println(lemma);
		var lexes = model.getLexesByLemma().get(lemma);
		dump(lexes, Lex::getType, nullableDiscriminant, model, ps);
	}

	public static void testWordByPos(final CoreModel model, final String lemma, final PrintStream ps)
	{
		ps.println(lemma);
		var lexes = model.getLexesByLemma().get(lemma);
		dump(lexes, Lex::getPartOfSpeech, nullableDiscriminant, model, ps);
	}

	public static void testWordByTypeAndPronunciation(final CoreModel model, final String lemma, final PrintStream ps)
	{
		ps.println(lemma);
		var lexes = model.getLexesByLemma().get(lemma);
		dump(lexes, Lex::getType, nullablePronunciations, model, ps);
	}

	public static void testWordByPosAndPronunciation(final CoreModel model, final String lemma, final PrintStream ps)
	{
		ps.println(lemma);
		var lexes = model.getLexesByLemma().get(lemma);
		dump(lexes, Lex::getPartOfSpeech, nullablePronunciations, model, ps);
	}

	private static <K> void dump(final Collection<Lex> lexes, final Function<Lex, ? extends K> classifier2, final Function<Lex, ? extends K> classifier3, final CoreModel model, final PrintStream ps)
	{
		var map2 = lexes.stream().collect((groupingBy(classifier2, toList())));
		for (K k2 : map2.keySet())
		{
			ps.printf("\t%s:%n", k2);
			var map3 = map2.get(k2).stream().collect((groupingBy(classifier3, toList())));
			for (K k3 : map3.keySet())
			{
				ps.printf("\t\t%s:%n", keyToString(k3));
				map3.get(k3).forEach(lex -> dump(lex.getSenses(), model, "\t\t\t", ps));
			}
		}
	}

	private static void dump(final List<Sense> senses, final CoreModel model, final String indent, final PrintStream ps)
	{
		senses.forEach(sense -> {
			ps.printf("%s%s%n", indent, sense);
			dump(sense, model, indent + "\t", ps);
		});
	}

	private static void dump(final Sense sense, final CoreModel model, final String indent, final PrintStream ps)
	{
		ps.printf("%ssk=%s type=%c pos=%c lemma='%s' index=%d adj=%s synset=%s%n", indent, sense.getSensekey(), sense.getType(), sense.getPartOfSpeech(), sense.getLemma(), sense.getLexIndex(), sense.getAdjPosition(), toShortSynset(sense.getSynsetId(), model));
		dumpSynset(sense.getSynsetId(), model, indent + "\t", ps);

		// relations
		var relations = sense.getRelations();
		if (relations != null)
		{
			relations.keySet().forEach(type -> ps.printf("%s%-28s: [%s]%n", indent, type, String.join(",", relations.get(type))));
		}

		// verbframes
		var verbFrames = sense.getVerbFrames();
		if (verbFrames != null)
		{
			ps.printf("%sframes: [%s]%n", indent, String.join(",", verbFrames));
		}

		// verbtemplates
		var verbTemplates = sense.getVerbTemplates();
		if (verbTemplates != null)
		{
			ps.printf("%stemplates: [%s]%n", indent, Arrays.stream(verbTemplates).mapToObj(Integer::toString).collect(joining(",")));
		}
	}

	private static void dumpSynset(final String synsetId, final CoreModel model, final String indent, final PrintStream ps)
	{
		Synset synset = model.getSynsetsById().get(synsetId);
		dump(synset, indent, ps);
	}

	private static String toShortSynset(final String synsetId, final CoreModel model)
	{
		Synset synset = model.getSynsetsById().get(synsetId);
		return toShort(synset);
	}

	private static String toShort(final Synset synset)
	{
		return String.format("%s %s '%s'", synset.getSynsetId(), Arrays.toString(synset.getMembers()), toShortDefinition(synset));
	}

	private static String toShortDefinition(final Synset synset)
	{
		String definition = synset.getDefinition();
		if (definition.length() > 32)
		{
			return definition.substring(0, 32) + "...";
		}
		return definition;
	}

	private static void dump(final Synset synset, final String indent, final PrintStream ps)
	{
		ps.printf("%s%s%n", indent, synset.getSynsetId());
		ps.printf("%s{%s}%n", indent + "\t", String.join(",", synset.getMembers()));
		ps.printf("%s%s%n", indent + "\t", String.join(",", synset.getDefinitions()));
		var relations = synset.getRelations();
		if (relations != null)
		{
			relations.keySet().forEach(type -> ps.printf("%s%-20s: %s%n", indent + "\t", type, relations.get(type)));
		}
	}
}
