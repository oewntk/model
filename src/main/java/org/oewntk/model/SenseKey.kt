package org.oewntk.model

import org.oewntk.model.Lemma.Companion.ESC_LEMMA_CHARS_RE
import org.oewntk.model.SynsetId.Companion.SYNSET_ID_RE
import java.io.Serializable

/**
 * SenseKey
 */
@kotlinx.serialization.Serializable
@JvmInline
value class SenseKey(val id: String) : Comparable<SenseKey>, Serializable {
    init {
        require(senseKeyRegex.matches(id)) { "Invalid sense key: '$id'" }
    }

    override fun toString(): String = id
    override fun compareTo(other: SenseKey): Int = id.compareTo(other.id)

    companion object {

        val SENSEKEY_RE = "(?!$SYNSET_ID_RE$)[${ESC_LEMMA_CHARS_RE}]+%\\d+:\\d+:\\d+:[${ESC_LEMMA_CHARS_RE}]*:\\d*"

        val senseKeyRegex = "^$SENSEKEY_RE$".toRegex()

        fun String.isSenseKey(): Boolean = senseKeyRegex.matches(this)

    }
}