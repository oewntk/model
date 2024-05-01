/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import java.io.Serializable
import java.util.*

/**
 * Pronunciation
 *
 * @property value   value in IPA
 * @property variety variety
 */
class Pronunciation(
    val value: String,
    val variety: String?,
) : Serializable {

    // identify

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as Pronunciation
        return value == that.value && variety == that.variety
    }

    override fun hashCode(): Int {
        return Objects.hash(value, variety)
    }

    // stringify

    override fun toString(): String {
        return if (variety != null) "[$variety] $value" else "/$value/"
    }

    companion object {

        fun ipa(value: String): Pronunciation {
            return Pronunciation(value, null)
        }

        fun ipa(value: String, variety: String?): Pronunciation {
            return Pronunciation(value, variety)
        }
    }
}
