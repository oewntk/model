/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * Generic grouping factory
 */
public class Groupings
{
	// group by
	public static <K, V> Map<K, Collection<V>> groupBy(final Collection<V> things, final Function<V, K> groupingFunction)
	{
		return groupBy(things.stream(), groupingFunction);
	}

	public static <K, V> Map<K, Collection<V>> groupBy(final Stream<V> stream, final Function<V, K> groupingFunction)
	{
		return stream //
				.collect(groupingBy(groupingFunction, toCollection(HashSet::new)));
	}

	// counts

	public static <K, V> Map<K, Long> countsBy(final  Stream<V> stream, final Function<V, K> groupingFunction)
	{
		return stream //
				.collect(groupingBy(groupingFunction, TreeMap::new, counting()));
	}

	public static <K, V> Map<K, Long> multipleCountsBy(final  Stream<V> stream, final Function<V, K> groupingFunction)
	{
		return stream //
				.collect(collectingAndThen( //
						groupingBy(groupingFunction, TreeMap::new, counting()), //
						m -> {
							m.values().removeIf(v -> v <= 1L);
							return m;
						}));
	}

	// group by having

	public static <K, V> Map<K, List<V>> groupByHaving(final  Stream<V> stream, final Function<V, K> groupingFunction, final Predicate<List<V>> predicate)
	{
		return stream //
				.collect(collectingAndThen( //
						groupingBy(groupingFunction, TreeMap::new, toList()), //
						map -> {
							map.values().removeIf(predicate);
							return map;
						}));
	}

	public static <K, V> Map<K, List<V>> groupByHavingMultipleCount(final  Stream<V> stream, final Function<V, K> groupingFunction)
	{
		return groupByHaving(stream, groupingFunction, vals -> vals.size() <= 1L);
	}
}
