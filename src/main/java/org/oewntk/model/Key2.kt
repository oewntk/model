package org.oewntk.model

import java.io.Serializable

/**
 * Key2 implementation
 */
@kotlinx.serialization.Serializable
@JvmInline
value class Key2Impl(val id: String) : Comparable<Key2Impl>, Serializable {
    init {
        require(key2Regex.matches(id)) { "Invalid key2: '$id'" }
    }

    override fun toString(): String = id
    override fun compareTo(other: Key2Impl): Int = id.compareTo(other.id)

    companion object {

        const val KEY2_RE = "^[nvar](-[0-9]+)?$"

        val key2Regex = "^$KEY2_RE$".toRegex()

        fun String.isKey2(): Boolean = key2Regex.matches(this)

    }
}