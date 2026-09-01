package org.oewntk.model

import java.io.Serializable
import java.util.Locale
import kotlin.text.matches

/**
 * Lemma implementation
 */
@kotlinx.serialization.Serializable
@JvmInline
value class LemmaImpl(val form: String) : Comparable<LemmaImpl>, Serializable {
    init {
        require(lemmaRegex.matches(form)) { "Invalid lemma: '$form'" }
    }

    override fun toString(): String = form
    override fun compareTo(other: LemmaImpl): Int = form.compareTo(other.form)
    val lowercased: String
        get() = form.lowercase(Locale.ENGLISH)
    val lCLemma: Lemma
        get() = Lemma(lowercased)

    companion object {

        const val BASE_LEMMA_CHARS_RE = "a-zA-Z\\u00C0-\\u00D6\\u00D8-\\u00F60-9\\-+.,:!/'"
        const val LEMMA_CHARS_RE = "$BASE_LEMMA_CHARS_RE "
        const val ESC_LEMMA_CHARS_RE = "${BASE_LEMMA_CHARS_RE}_"

        const val LEMMA_RE = "[${LEMMA_CHARS_RE}]+"

        val lemmaRegex = "^$LEMMA_RE$".toRegex()

        fun String.isLemma(): Boolean = lemmaRegex.matches(this)
    }
}