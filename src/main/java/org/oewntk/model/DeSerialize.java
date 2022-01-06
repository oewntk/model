/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.*;

public class DeSerialize
{
	/**
	 * Deserialize core model from file
	 *
	 * @param file file
	 * @return core model
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	public static CoreModel deSerializeCoreModel(final File file) throws IOException, ClassNotFoundException
	{
		try (InputStream is = new FileInputStream(file))
		{
			return deSerializeCoreModel(is);
		}
	}

	/**
	 * Deserialize model from file
	 *
	 * @param file file
	 * @return model
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	public static Model deSerializeModel(final File file) throws IOException, ClassNotFoundException
	{
		try (InputStream is = new FileInputStream(file))
		{
			return deSerializeModel(is);
		}
	}

	/**
	 * Deserialize core model from file
	 *
	 * @param is input stream
	 * @return core model
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	public static CoreModel deSerializeCoreModel(final InputStream is) throws IOException, ClassNotFoundException
	{
		return (CoreModel) deSerialize(is);
	}

	/**
	 * Deserialize model from file
	 *
	 * @param is input stream
	 * @return model
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	public static Model deSerializeModel(final InputStream is) throws IOException, ClassNotFoundException
	{
		return (Model) deSerialize(is);
	}

	/**
	 * Deserialize object
	 *
	 * @param is input stream
	 * @return object
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	private static Object deSerialize(final InputStream is) throws IOException, ClassNotFoundException
	{
		try (ObjectInputStream ois = new ObjectInputStream(is))
		{
			return ois.readObject();
		}
	}
}
