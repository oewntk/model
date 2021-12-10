/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class DeSerialize
{
	public static CoreModel deSerializeCoreModel(final File file) throws IOException,ClassNotFoundException
	{
		try (InputStream is = new FileInputStream(file))
		{
			return deSerializeCoreModel(is);
		}
	}

	public static Model deSerializeModel(final File file) throws IOException,ClassNotFoundException
	{
		try (InputStream is = new FileInputStream(file))
		{
			return deSerializeModel(is);
		}
	}

	public static CoreModel deSerializeCoreModel(final InputStream is) throws IOException,ClassNotFoundException
	{
		return (CoreModel) deSerialize(is);
	}

	public static Model deSerializeModel(final InputStream is) throws IOException,ClassNotFoundException
	{
		return (Model) deSerialize(is);
	}

	private static Object deSerialize(final InputStream is) throws IOException,ClassNotFoundException
	{
		try (ObjectInputStream ois = new ObjectInputStream(is))
		{
			return ois.readObject();
		}
	}
}
