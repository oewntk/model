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

	/**
	 * Group elements in collection
	 *
	 * @param things           collection of elements
	 * @param groupingFunction map element to key
	 * @param <K>              type of key
	 * @param <V>              type of element
	 * @return collections of elements grouped by and mapped by key
	 */
	public static <K, V> Map<K, Collection<V>> groupBy(final Collection<V> things, final Function<V, K> groupingFunction)
	{
		return groupBy(things.stream(), groupingFunction);
	}

	/**
	 * Group elements in stream
	 *
	 * @param stream           stream of elements
	 * @param groupingFunction map element to key
	 * @param <K>              type of key
	 * @param <V>              type of element
	 * @return collections of elements grouped by and mapped by key
	 */
	public static <K, V> Map<K, Collection<V>> groupBy(final Stream<V> stream, final Function<V, K> groupingFunction)
	{
		return stream //
				.collect(groupingBy(groupingFunction, toCollection(HashSet::new)));
	}

	// counts

	/**
	 * Group elements by key and yield count of each group
	 *
	 * @param stream           stream of elements
	 * @param groupingFunction map element to key
	 * @param <K>              type of key
	 * @param <V>              type of element
	 * @return count for each key/group
	 */
	public static <K, V> Map<K, Long> countsBy(final Stream<V> stream, final Function<V, K> groupingFunction)
	{
		return stream //
				.collect(groupingBy(groupingFunction, TreeMap::new, counting()));
	}

	/**
	 * Group elements by key and yield count of each group, retain only groups whose count &gt; 1
	 *
	 * @param stream           stream of elements
	 * @param groupingFunction map element to key
	 * @param <K>              type of key
	 * @param <V>              type of element
	 * @return count for each key/group
	 */
	public static <K, V> Map<K, Long> multipleCountsBy(final Stream<V> stream, final Function<V, K> groupingFunction)
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

	/**
	 * Group elements having
	 *
	 * @param stream           stream of elements
	 * @param groupingFunction map element to key
	 * @param predicate        having clause
	 * @param <K>              grouping key
	 * @param <V>              type of elements
	 * @return list of elements grouped and mapped by key
	 */
	public static <K, V> Map<K, List<V>> groupByHaving(final Stream<V> stream, final Function<V, K> groupingFunction, final Predicate<List<V>> predicate)
	{
		return stream //
				.collect(collectingAndThen( //
						groupingBy(groupingFunction, TreeMap::new, toList()), //
						map -> {
							map.values().removeIf(predicate);
							return map;
						}));
	}

	/**
	 * Group elements having group count &gt; 1
	 *
	 * @param stream           stream of elements
	 * @param groupingFunction map element to key
	 * @param <K>              grouping key
	 * @param <V>              type of elements
	 * @return list of elements grouped and mapped by key
	 */
	public static <K, V> Map<K, List<V>> groupByHavingMultipleCount(final Stream<V> stream, final Function<V, K> groupingFunction)
	{
		return groupByHaving(stream, groupingFunction, values -> values.size() <= 1L);
	}
}
