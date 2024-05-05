/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import java.io.Serializable

/**
 * Verb template
 *
 * @param id          verb template id
 * @param template    verb template
 * @property id       verb template id
 * @property template verb template
 */
@kotlinx.serialization.Serializable
data class VerbTemplate(

    val id: VerbTemplateType,
    val template: String,

    ) : Serializable {

    override fun toString(): String {
        return "[$id] '$template'"
    }
}