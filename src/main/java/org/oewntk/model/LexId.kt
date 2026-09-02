package org.oewntk.model

import org.oewntk.model.Lemma.Companion.LEMMA_RE
import java.io.Serializable
import java.util.Objects

/**
 * Lex Id
 *
 * @property lemma lemma
 * @property partOfSpeech part of speech
 * @property discriminant discriminant (nullable)
 */
@kotlinx.serialization.Serializable
data class LexId(val lemma: Lemma, val partOfSpeech: PartOfSpeech, val discriminant: Discriminant? = null) : Serializable {

    override fun equals(other: Any?): Boolean {
        return this === other || other is LexId && (
                Objects.equals(lemma, other.lemma)
                        && Objects.equals(partOfSpeech, other.partOfSpeech)
                        && Objects.equals(discriminant, other.discriminant))
    }

    override fun hashCode(): Int = Objects.hash(lemma, partOfSpeech, discriminant)

    override fun toString() = "$lemma-${partOfSpeech.value}" + if (discriminant != null) "-$discriminant" else ""

    companion object {

        val LEXID_RE = "$LEMMA_RE,[nvar]-?[\\d]?"

        val lexIdRegex = "^$LEXID_RE$".toRegex()

        fun String.isLexId(): Boolean = lexIdRegex.matches(this)

    }
}