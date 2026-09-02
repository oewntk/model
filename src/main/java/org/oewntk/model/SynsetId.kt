package org.oewntk.model

import java.io.Serializable

/**
 * Synset Id
 */
@kotlinx.serialization.Serializable
@JvmInline
value class SynsetId(val id: String) : Comparable<SynsetId>, Serializable {
    init {
        require(synsetIdRegex.matches(id)) { "Invalid synset id: '$id'" }
    }

    override fun toString(): String = id
    override fun compareTo(other: SynsetId): Int = id.compareTo(other.id)

    companion object {

        const val SYNSET_ID_RE = "\\d{8}-[nvars]"

        val synsetIdRegex = "^$SYNSET_ID_RE$".toRegex()

        fun String.isSynsetId(): Boolean = synsetIdRegex.matches(this)

    }
}