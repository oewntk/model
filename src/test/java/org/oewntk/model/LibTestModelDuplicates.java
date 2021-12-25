/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import org.junit.Assert;

import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

public class LibTestModelDuplicates
{
	public static <T> void testDuplicatesForKeyMono(final CoreModel model, final Function<Lex, KeyF.MonoValued> key, final PrintStream ps)
	{
		var dups = model.lexes //
				.stream() // stream of lexes
				.map(key) // stream of values
				.collect(groupingBy(Function.identity(), counting())) // map(key, count));
				.entrySet().stream() // stream of pairs(key,count)
				.filter(m -> m.getValue() > 1) // if map value > 1, duplicate element
				//.sorted(Comparator.comparingLong(Map.Entry::getValue)) //
				.sorted(Comparator.comparing(e -> e.getKey().toString())) //
				.collect(toCollection(LinkedHashSet::new));
		ps.println(dups.size());
		dups.forEach(ps::println);
		Assert.assertEquals(0, dups.size());
	}

	public static <T> void testDuplicatesForKeyMulti(final CoreModel model, final Function<Lex, KeyF.MultiValued> key, final PrintStream ps)
	{
		var dups = model.lexes //
				.stream() // stream of lexes
				.map(key) // stream of keys
				.collect(groupingBy(Function.identity(), counting())) // map(key, count));
				.entrySet().stream() // stream of map entries
				.filter(m -> m.getValue() > 1) // if map value > 1, duplicate element
				//.sorted(Comparator.comparingLong(Map.Entry::getValue)) //
				.sorted(Comparator.comparing(e -> e.getKey().toString())) //
				.collect(toCollection(LinkedHashSet::new));
		ps.println(dups.size());
		dups.forEach(ps::println);
	}

	public static void testDuplicatesForKeyOEWN(final CoreModel model, final PrintStream ps)
	{
		testDuplicatesForKeyMono(model, lex-> KeyF.W_P_A_functional.Mono.of(Lex::getLemma, Lex::getType, lex), ps);
	}

	public static void testDuplicatesForKeyPos(final CoreModel model, final PrintStream ps)
	{
		testDuplicatesForKeyMono(model, lex-> KeyF.W_P_A_functional.Mono.of(Lex::getLemma, Lex::getPartOfSpeech, lex), ps);
	}

	public static void testDuplicatesForKeyIC(final CoreModel model, final PrintStream ps)
	{
		testDuplicatesForKeyMulti(model, lex-> KeyF.W_P_A_functional.Multi.of(Lex::getLCLemma, Lex::getType, lex), ps);
	}

	public static void testDuplicatesForKeyPWN(final CoreModel model, final PrintStream ps)
	{
		testDuplicatesForKeyMulti(model, lex-> KeyF.W_P_functional.Multi.of(Lex::getLCLemma, Lex::getType, lex), ps);
	}
}
