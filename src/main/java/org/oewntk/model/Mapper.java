/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class Mapper
{
	// G E N E R I C   M A P   F A C T O R Y

	public static <K, V> Map<K, V> map(final Collection<V> things, final Function<V, K> groupingFunction)
	{
		return things.stream() //
				.collect(toMap(groupingFunction, //
						Function.identity(), //
						(existing, replacement) -> {
							if (existing.equals(replacement))
							{
								throw new IllegalArgumentException(existing + "," + replacement);
							}
							return existing;
						},
						//TODO (v1, v2) -> { throw new RuntimeException(String.format("Duplicate key for values %s and %s", v1, v2)); }, //
						TreeMap::new));
	}

	// G E N E R I C   M A P   F A C T O R Y

	public static Map<String, Sense> sensesById(final Collection<Sense> senses)
	{
		return map(senses, Sense::getSensekey);
	}

	public static Map<String, Synset> synsetsById(final Collection<Synset> synsets)
	{
		return map(synsets, Synset::getSynsetId);
	}
}
