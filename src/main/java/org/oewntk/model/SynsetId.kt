package org.oewntk.model

import java.io.Serializable
import kotlin.text.matches

/**
 * Synset Id implementation
 */
@kotlinx.serialization.Serializable
@JvmInline
value class SynsetIdImpl(val id: String) : Comparable<SynsetIdImpl>, Serializable {
    init {
        require(synsetIdRegex.matches(id)) { "Invalid synset id: '$id'" }
    }

    override fun toString(): String = id
    override fun compareTo(other: SynsetIdImpl): Int = id.compareTo(other.id)

    companion object {

        const val SYNSET_ID_RE = "\\d{8}-[nvars]"

        val synsetIdRegex = "^$SYNSET_ID_RE$".toRegex()

        fun String.isSynsetId(): Boolean = synsetIdRegex.matches(this)

    }
}