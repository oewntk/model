/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.model;

import java.util.Map;

/**
 * Formatter to joined representation of items
 */
public class Formatter
{
	/**
	 * Join array of items
	 *
	 * @param <T>   type of item
	 * @param items array of items of type T
	 * @param delim delimiter
	 * @return joined string representation of items
	 */
	public static <T> String join(T[] items, String delim)
	{
		if (items == null)
		{
			return "";
		}
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (T item : items)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				sb.append(delim);
			}
			sb.append(item.toString());
		}
		return sb.toString();
	}

	/**
	 * Join iteration of items
	 *
	 * @param <T>   type of item
	 * @param items array of items of type T
	 * @param delim delimiter
	 * @return joined string representation of items
	 */
	public static <T> String join(Iterable<T> items, String delim)
	{
		if (items == null)
		{
			return "";
		}
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (T item : items)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				sb.append(delim);
			}
			sb.append(item.toString());
		}
		return sb.toString();
	}

	/**
	 * Join items in multimap
	 *
	 * @param <K>   type of key
	 * @param <V>   type of value
	 * @param <T>   type of item
	 * @param map   map of lists of items of type V mapped by K
	 * @param delim delimiter
	 * @return joined string representation of items
	 */
	public static <K, V extends Iterable<T>, T> String join(Map<K, V> map, String delim)
	{
		if (map == null)
		{
			return "";
		}
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Map.Entry<K, V> entry : map.entrySet())
		{
			if (first)
			{
				first = false;
			}
			else
			{
				sb.append(delim);
			}
			String k = entry.getKey().toString();
			Iterable<T> v = entry.getValue();
			sb.append('[');
			sb.append(k);
			sb.append(']');
			sb.append('=');
			sb.append(join(v, ","));
		}
		return sb.toString();
	}

	/**
	 * Join array of ints
	 *
	 * @param items  array of ints
	 * @param delim  delimiter
	 * @param format format
	 * @return joined string representation of each item
	 */
	public static String join(int[] items, char delim, String format)
	{
		if (items == null)
		{
			return "";
		}
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (int item : items)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				sb.append(delim);
			}
			sb.append(String.format(format, item));
		}
		return sb.toString();
	}
}
