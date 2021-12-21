/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.*;

public class Serialize
{
	static public void serializeModel(final Model model, final File file) throws IOException
	{
		try (OutputStream os = new FileOutputStream(file))
		{
			serializeModel(os, model);
		}
	}

	public static void serializeModel(final OutputStream os, final Model model) throws IOException
	{
		serialize(os, model);
	}

	static public void serializeCoreModel(final CoreModel model, final File file) throws IOException
	{
		try (OutputStream os = new FileOutputStream(file))
		{
			serializeCoreModel(os, model);
		}
	}

	public static void serializeCoreModel(final OutputStream os, final CoreModel model) throws IOException
	{
		serialize(os, model);
	}

	private static void serialize(final OutputStream os, final Object object) throws IOException
	{
		try (ObjectOutputStream oos = new ObjectOutputStream(os))
		{
			oos.writeObject(object);
		}
	}
}
