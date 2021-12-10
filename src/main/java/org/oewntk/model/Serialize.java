/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class Serialize
{
	static public void serializeModel(final CoreModel model, final File file) throws IOException
	{
		try (OutputStream os = new FileOutputStream(file))
		{
			serializeModel(os, model);
		}
	}

	public static void serializeModel(final OutputStream os, final CoreModel model) throws IOException
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
