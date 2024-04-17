/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Pronunciation
 */
public class Pronunciation implements Serializable
{
	private final String value;

	private final String variety;

	public static Pronunciation ipa(final String value)
	{
		return new Pronunciation(value, null);
	}

	public static Pronunciation ipa(final String value, final String variety)
	{
		return new Pronunciation(value, variety);
	}

	/**
	 * Constructor
	 *
	 * @param value   value in IPA
	 * @param variety variety
	 */
	public Pronunciation(final String value, final String variety)
	{
		this.value = value;
		this.variety = variety;
	}

	/**
	 * Get value in IPA
	 *
	 * @return value in IPA
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * Get variety
	 *
	 * @return variety
	 */
	public String getVariety()
	{
		return variety;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		Pronunciation that = (Pronunciation) o;
		return Objects.equals(value, that.value) && Objects.equals(variety, that.variety);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(value, variety);
	}

	// stringify

	@Override
	public String toString()
	{
		String value = "/" + this.value + '/';
		if (variety != null)
		{
			return String.format("[%s] %s", variety, value);
		}
		return value;
	}
}
