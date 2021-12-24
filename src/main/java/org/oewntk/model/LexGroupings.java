/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.util.*;

import static java.util.stream.Collectors.*;

public class LexGroupings
{
	/**
	 * Group lexes by CS lemma
	 *
	 * @param lexes lexes
	 * @return lexes grouped by CS lemma
	 */
	public static Map<String, Collection<Lex>> lexesByLemma(final Collection<Lex> lexes)
	{
		return Groupings.groupBy(lexes, Lex::getLemma);
	}

	/**
	 * Group lexes by LC lemma
	 *
	 * @param lexes lexes
	 * @return lexes grouped by LCS lemma
	 */
	public static Map<String, Collection<Lex>> lexesByLCLemma(final Collection<Lex> lexes)
	{
		return Groupings.groupBy(lexes, Lex::getLCLemma);
	}

	/**
	 * Hypermap (LCLemma -> CSLemma -> lexes)
	 *
	 * @param model model
	 * @return 2-tier hypermap (LCLemma -> CSLemma -> lexes)
	 */
	public static Map<String, Map<String, Collection<Lex>>> hyperMapByLCLemmaByLemma(final CoreModel model)
	{
		return model.getLexesByLemma().entrySet().stream() //
				.collect(groupingBy(e -> e.getKey().toLowerCase(Locale.ENGLISH), toMap(Map.Entry::getKey, Map.Entry::getValue)));
	}

	/**
	 * CSLemmas grouped by LCLemma
	 * baroque -> (Baroque, baroque)
	 *
	 * @param model model
	 * @return CS lemmas by LC lemmas
	 */
	public static Map<String, List<String>> cSLemmasByLCLemma(final CoreModel model)
	{
		return model.lexes.stream() //
				.map(Lex::getLemma) //
				.collect(groupingBy(l -> l.toLowerCase(Locale.ENGLISH), TreeMap::new, toList()));
	}

	/**
	 * CSLemmas for LCLemma
	 *
	 * @param model model
	 * @return CS lemmas for given LC lemma
	 */
	public static List<String> cSLemmasForLCLemma(final CoreModel model, final String lcLemma)
	{
		return cSLemmasByLCLemma(model).get(lcLemma);
	}

	// counts

	/**
	 * Counts of CS lemmas by LC lemma
	 *
	 * @param model model
	 * @return counts of CS lemmas by LC lemmas
	 */
	public static Map<String, Long> countsByLCLemma(final CoreModel model)
	{
		return Groupings.countsBy(model.lexes.stream().map(Lex::getLemma), lemma -> lemma.toLowerCase(Locale.ENGLISH));
	}

	/**
	 * Counts of CS lemmas by LC lemma, sme as above but retain entries that have count > 1
	 *
	 * @param model model
	 * @return counts of CS lemmas by LC lemmas, with count > 2
	 */
	public static Map<String, Long> multipleCountsByICLemma(final CoreModel model)
	{
		return Groupings.multipleCountsBy(model.lexes.stream().map(Lex::getLemma), lemma -> lemma.toLowerCase(Locale.ENGLISH));
	}

	/**
	 * CS lemmas by LC lemmas, retain entries that have count > 1
	 *
	 * @param model model
	 * @return CS lemmas by LC lemmas, with count > 2
	 */
	public static Map<String, List<String>> cSLemmasByLCLemmaHavingMultipleCount(final CoreModel model)
	{
		return Groupings.groupByHavingMultipleCount(model.lexes.stream().map(Lex::getLemma), lemma -> lemma.toLowerCase(Locale.ENGLISH));
	}
}
