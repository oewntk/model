package org.oewntk.model

import java.io.Serializable

/**
 * Key2 (level 2 key in YAML files)
 */
@kotlinx.serialization.Serializable
@JvmInline
value class Key2(val id: String) : Comparable<Key2>, Serializable {
    init {
        require(key2Regex.matches(id)) { "Invalid key2: '$id'" }
    }

    override fun toString(): String = id
    override fun compareTo(other: Key2): Int = id.compareTo(other.id)

    companion object {

        const val KEY2_RE = "^[nvar](-[0-9]+)?$"

        val key2Regex = "^$KEY2_RE$".toRegex()

        fun String.isKey2(): Boolean = key2Regex.matches(this)

    }
}