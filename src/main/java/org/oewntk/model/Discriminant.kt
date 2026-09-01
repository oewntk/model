package org.oewntk.model

import java.io.Serializable

/**
 * Discriminant implementation
 */
@kotlinx.serialization.Serializable
@JvmInline
value class DiscriminantImpl(val id: String) : Comparable<DiscriminantImpl>, Serializable {
    init {
        require(discriminantRegex.matches(id)) { "Invalid discriminant: '$id'" }
    }

    override fun toString(): String = id
    override fun compareTo(other: DiscriminantImpl): Int = id.compareTo(other.id)

    companion object {

        const val DISCRIMINANT_RE = "^(-[0-9]+)?$"

        val discriminantRegex = "^$DISCRIMINANT_RE$".toRegex()

        fun String.isDiscriminant(): Boolean = discriminantRegex.matches(this)

    }
}