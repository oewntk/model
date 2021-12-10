/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.Serializable;

/**
 * Verb template
 */
public class VerbTemplate implements Serializable
{
	private final int id;

	private final String template;

	/**
	 * Constructor
	 *
	 * @param id       verb template id
	 * @param template verb template
	 */
	public VerbTemplate(final int id, final String template)
	{
		this.id = id;
		this.template = template;
	}

	/**
	 * Verb template id
	 *
	 * @return template id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Verb template
	 *
	 * @return template
	 */
	public String getTemplate()
	{
		return template;
	}

	// stringify

	@Override
	public String toString()
	{
		return String.format("[%d] '%s'", getId(), getTemplate());
	}
}