/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */
package org.oewntk.model

import java.io.Serializable

/**
 * Verb frame
 *
 * @property id    verb frame id
 * @property frame verb frame
 */
class VerbFrame(
	/**
	 * Verb frame id
	 */
	@JvmField val id: String,

	/**
	 * Verb frame
	 */
	@JvmField val frame: String

) : Serializable {

	override fun toString(): String {
		return "$id '$frame'"
	}
}