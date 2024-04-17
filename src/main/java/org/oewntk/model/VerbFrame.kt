/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.Serializable;

/**
 * Verb frame
 */
public class VerbFrame implements Serializable
{
	/**
	 * Verb frame id
	 */
	private final String id;

	/**
	 * Verb frame
	 */
	private final String frame;

	/**
	 * Constructor
	 *
	 * @param id    verb frame id
	 * @param frame verb frame
	 */
	public VerbFrame(final String id, final String frame)
	{
		this.id = id;
		this.frame = frame;
	}

	/**
	 * Get verb frame id
	 *
	 * @return verb frame id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * Get verb frame
	 *
	 * @return verb frame
	 */
	public String getFrame()
	{
		return frame;
	}

	// stringify

	@Override
	public String toString()
	{
		return String.format("%s '%s'", getId(), getFrame());
	}
}