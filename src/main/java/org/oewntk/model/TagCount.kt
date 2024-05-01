/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import java.io.Serializable

/**
 * Tag count
 *
 * @param senseNum    sense num
 * @param count       tag count
 * @property senseNum sense num
 * @property count    tag count
 */
class TagCount(
    private val senseNum: Int,
    val count: Int,

    ) : Serializable {

    override fun toString(): String {
        return count.toString()
    }
}
