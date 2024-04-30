/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import java.io.Serializable

/**
 * Tag count
 *
 * @property senseNum sense num
 * @property count    tag count
 */
class TagCount(
    /**
     * Get sense number
     */
    private val senseNum: Int,

    /**
     * Get tag count
     *
     * @return tag count
     */
    val count: Int,

    ) : Serializable {

    override fun toString(): String {
        return count.toString()
    }
}
