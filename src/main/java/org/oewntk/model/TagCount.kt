/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.Serializable;

/**
 * Tag count
 */
public class TagCount implements Serializable
{
	private final int senseNum;

	private final int count;

	/**
	 * Constructor
	 *
	 * @param senseNum sense num
	 * @param count    tag count
	 */
	public TagCount(final int senseNum, final int count)
	{
		this.senseNum = senseNum;
		this.count = count;
	}

	/**
	 * Get tag count
	 *
	 * @return tag count
	 */
	public int getCount()
	{
		return count;
	}

	/**
	 * Get sense number for that sensekey (index of sense in pos)
	 *
	 * @return sense num
	 */
	public int getSenseNum()
	{
		return senseNum;
	}

	// stringify

	@Override
	public String toString()
	{
		return Integer.toString(getCount());
	}
}
