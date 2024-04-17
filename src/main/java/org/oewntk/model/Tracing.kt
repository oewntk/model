/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.model

import java.io.OutputStream
import java.io.PrintStream

object Tracing {
	@JvmField
	val psInfo: PrintStream = System.out

	val psErr: PrintStream = System.err

	@JvmField
	val psNull: PrintStream = PrintStream(object : OutputStream(
	) {
		override fun write(i: Int) {
			// do nothing
		}
	})
}
