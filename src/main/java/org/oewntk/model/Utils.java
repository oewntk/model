/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.util.Set;

public class Utils
{
	static <T> Set<T> toSet(final T[] objects)
	{
		if (objects == null)
		{
			return Set.of();
		}
		return Set.of(objects);
	}
}
