/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import java.io.Serializable

/**
 * Verb frame
 *
 * @param id       verb frame id
 * @param frame    verb frame
 * @property id    verb frame id
 * @property frame verb frame
 */
@kotlinx.serialization.Serializable
data class VerbFrame(

    val id: VerbFrameType,
    val frame: String,

    ) : Serializable {

    override fun toString(): String {
        return "$id '$frame'"
    }
}