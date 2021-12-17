/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.model;

import java.io.OutputStream;
import java.io.PrintStream;

public class Tracing
{
	public static final PrintStream psInfo = System.out;

	public static final PrintStream psErr = System.err;

	public static final PrintStream psNull = new PrintStream(new OutputStream()
		{
			@Override
			public void write(final int i)
			{
				// do nothing
			}
		});
}
