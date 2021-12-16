/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;

import static java.util.stream.Collectors.*;

public class LexGroupings
{
	public static Map<String, Map<String, List<Lex>>> byLCLemma(final CoreModel model)
	{
		return model.getLexesByLemma().entrySet().stream() //
				.collect(groupingBy(e -> e.getKey().toLowerCase(Locale.ENGLISH), toMap(Map.Entry::getKey, Map.Entry::getValue)));
	}

	public static Map<String, List<Lex>> lexesByLCLemma(final CoreModel model)
	{
		return model.getLexesByLemma().entrySet().stream() //
				.collect(groupingBy(e -> e.getKey().toLowerCase(Locale.ENGLISH), TreeMap::new, flatMapping(e2 -> e2.getValue().stream(), toList())));
	}

	public static List<Lex> lexesForLCLemma(final CoreModel model, final String word)
	{
		return lexesByLCLemma(model).get(word);
	}

	public static Map<String, List<String>> lemmasByICLemma(final CoreModel model)
	{
		return model.getLexesByLemma().keySet().stream() //
				.collect(groupingBy(k -> k.toLowerCase(Locale.ENGLISH), TreeMap::new, toList()));
	}

	public static Map<String, Long> countsByICLemma(final CoreModel model)
	{
		return model.getLexesByLemma().keySet().stream() //
				.collect(groupingBy(k -> k.toLowerCase(Locale.ENGLISH), TreeMap::new, counting()));
	}

	public static Map<String, Long> multipleCountsByICLemma(final CoreModel model)
	{
		return model.getLexesByLemma().keySet().stream() //
				.collect(collectingAndThen(groupingBy(k -> k.toLowerCase(Locale.ENGLISH), TreeMap::new, counting()), m -> {
					m.values().removeIf(v -> v <= 1L);
					return m;
				}));
	}

	public static Map<String, List<String>> lemmasByICLemmaHavingMultipleCount(final CoreModel model)
	{
		return lemmasByICLemmaHaving(model, v -> v.size() <= 1L);
	}

	public static Map<String, List<String>> lemmasByICLemmaHaving(final CoreModel model, final Predicate<List<String>> predicate)
	{
		return model.getLexesByLemma().keySet().stream() //
				.collect(collectingAndThen(groupingBy(k -> k.toLowerCase(Locale.ENGLISH), TreeMap::new, toList()), m -> {
					m.values().removeIf(predicate);
					return m;
				}));
	}
}
