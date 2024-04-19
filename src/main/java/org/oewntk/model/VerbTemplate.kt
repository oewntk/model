/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */
package org.oewntk.model

import java.io.Serializable

/**
 * Verb template
 *
 * @property id       verb template id
 * @property template verb template
 */
class VerbTemplate(

	/**
	 * Verb template id
	 */
	val id: VerbTemplateType,

	/**
	 * Verb template
	 */
	@JvmField val template: String

) : Serializable {

	override fun toString(): String {
		return "[$id] '$template'"
	}
}