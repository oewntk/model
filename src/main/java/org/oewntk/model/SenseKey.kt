package org.oewntk.model

import org.oewntk.model.LemmaImpl.Companion.ESC_LEMMA_CHARS_RE
import org.oewntk.model.SynsetIdImpl.Companion.SYNSET_ID_RE
import java.io.Serializable
import kotlin.text.matches

/**
 * SenseKey implementation
 */
@kotlinx.serialization.Serializable
@JvmInline
value class SenseKeyImpl(val id: String) : Comparable<SenseKeyImpl>, Serializable {
    init {
        require(senseKeyRegex.matches(id)) { "Invalid sense key: '$id'" }
    }

    override fun toString(): String = id
    override fun compareTo(other: SenseKeyImpl): Int = id.compareTo(other.id)

    companion object {

        val SENSEKEY_RE = "(?!$SYNSET_ID_RE$)[${ESC_LEMMA_CHARS_RE}]+%\\d+:\\d+:\\d+:[${ESC_LEMMA_CHARS_RE}]*:\\d*"

        val senseKeyRegex = "^$SENSEKEY_RE$".toRegex()

        fun String.isSenseKey(): Boolean = senseKeyRegex.matches(this)

    }
}