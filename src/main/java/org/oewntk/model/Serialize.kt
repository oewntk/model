/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.*;

/**
 * Serialize models
 */
public class Serialize
{
	/**
	 * Serialize model to file
	 *
	 * @param model model
	 * @param file  file
	 * @throws IOException io exception
	 */
	static public void serializeModel(final Model model, final File file) throws IOException
	{
		try (OutputStream os = new FileOutputStream(file))
		{
			serializeModel(os, model);
		}
	}

	/**
	 * Serialize model to output stream
	 *
	 * @param model model
	 * @param os    output stream
	 * @throws IOException io exception
	 */
	public static void serializeModel(final OutputStream os, final Model model) throws IOException
	{
		serialize(os, model);
	}

	/**
	 * Serialize core model to file
	 *
	 * @param model core model
	 * @param file  file
	 * @throws IOException io exception
	 */
	static public void serializeCoreModel(final CoreModel model, final File file) throws IOException
	{
		try (OutputStream os = new FileOutputStream(file))
		{
			serializeCoreModel(os, model);
		}
	}

	/**
	 * Serialize core model to output stream
	 *
	 * @param model core model
	 * @param os    output stream
	 * @throws IOException io exception
	 */
	public static void serializeCoreModel(final OutputStream os, final CoreModel model) throws IOException
	{
		serialize(os, model);
	}

	/**
	 * Serialize object to output stream
	 *
	 * @param os     output stream
	 * @param object object
	 * @throws IOException io exception
	 */
	private static void serialize(final OutputStream os, final Object object) throws IOException
	{
		try (ObjectOutputStream oos = new ObjectOutputStream(os))
		{
			oos.writeObject(object);
		}
	}
}
