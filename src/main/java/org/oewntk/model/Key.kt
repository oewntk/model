/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import java.io.Serializable
import java.util.*

interface Key {

    fun toLongString(): String

    @kotlinx.serialization.Serializable
    sealed class BaseKey : Key /*, Comparable<W_P> */, Serializable {

        abstract val word: String

        abstract val posType: Char
    }

    /**
     * (Word, PosOrType)
     *
     * @property word    word: lemma or LC lemma
     * @property posType pos type: part-of-speech or type (P for pos)
     */
    @kotlinx.serialization.Serializable
    open class W_P(
        override val word: String,
        override val posType: Char,
    ) : BaseKey(), Comparable<W_P>, Serializable {

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other == null || javaClass != other.javaClass) {
                return false
            }
            val that = other as W_P
            if (word != that.word) {
                return false
            }
            return this.posType == that.posType
        }

        override fun hashCode(): Int {
            return Objects.hash(word, posType)
        }

        override fun compareTo(other: W_P): Int {
            if (this == other) {
                return 0
            }
            return wpComparator.compare(this, other)
        }

        override fun toString(): String {
            return "($word,$posType)"
        }

        override fun toLongString(): String {
            return "KEY WP ${javaClass.simpleName} $this"
        }

        companion object {

            fun of(
                lex: Lex,
                wordExtractor: (Lex) -> String,
                posTypeExtractor: (Lex) -> Char,
            ): W_P {
                return W_P(wordExtractor(lex), posTypeExtractor(lex))
            }

            fun of_t(lex: Lex): W_P {
                return of(lex, Lex::lemma, Lex::type)
            }

            fun of_p(lex: Lex): W_P {
                return of(lex, Lex::lemma, Lex::partOfSpeech)
            }

            fun of_lc_t(lex: Lex): W_P {
                return of(lex, Lex::lCLemma, Lex::type)
            }

            fun of_lc_p(lex: Lex): W_P {
                return of(lex, Lex::lCLemma, Lex::partOfSpeech)
            }

            fun from(word: String, posType: Char): W_P {
                return W_P(word, posType)
            }

            val wpComparator: Comparator<W_P> = Comparator.comparing { obj: W_P -> obj.word }
                .thenComparing { obj: W_P -> obj.posType }

        }
    }

    /**
     * (Word, PosOrType, Pronunciations)
     *
     * @param word    word: lemma or LC lemma
     * @param posType pos type: part-of-speech or type (P)
     * @property pronunciations pronunciations (A for audio)
     */
    @kotlinx.serialization.Serializable
    open class W_P_A(
        override var word: String,
        override var posType: Char,
        val pronunciations: Set<Pronunciation>?,
    ) : BaseKey(), Comparable<W_P_A> {

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other == null || javaClass != other.javaClass) {
                return false
            }
            val that = other as W_P_A
            if (word != that.word) {
                return false
            }
            if (this.posType != that.posType) {
                return false
            }
            val p1 = pronunciations ?: emptySet()
            val p2 = that.pronunciations ?: emptySet()
            return p1 == p2
        }

        override fun hashCode(): Int {
            return Objects.hash(word, posType, pronunciations)
        }

        override fun compareTo(other: W_P_A): Int {
            if (this == other) {
                return 0
            }
            return wpaComparator.compare(this, other)
        }

        override fun toString(): String {
            return "($word,$posType,$pronunciations)"
        }

        override fun toLongString(): String {
            return "KEY WPA ${javaClass.simpleName} $this"
        }

        companion object {

            fun of(
                lex: Lex,
                wordExtractor: (Lex) -> String,
                posTypeExtractor: (Lex) -> Char,
            ): W_P_A {
                return W_P_A(wordExtractor(lex), posTypeExtractor(lex), lex.pronunciations)
            }

            fun of_t(lex: Lex): W_P_A {
                return of(lex, Lex::lemma, Lex::type)
            }

            fun of_p(lex: Lex): W_P_A {
                return of(lex, Lex::lemma, Lex::partOfSpeech)
            }

            fun of_lc_t(lex: Lex): W_P_A {
                return of(lex, Lex::lCLemma, Lex::type)
            }

            fun of_lc_p(lex: Lex): W_P_A {
                return of(lex, Lex::lCLemma, Lex::partOfSpeech)
            }

            fun from(lemma: LemmaType, type: PosType, pronunciations: Set<Pronunciation>): W_P_A {
                return W_P_A(lemma, type, pronunciations)
            }

            private val pronunciationsComparator: Comparator<Set<Pronunciation>?> =
                Comparator { ps1: Set<Pronunciation>?, ps2: Set<Pronunciation>? ->
                    val pse1 = ps1 ?: emptySet()
                    val pse2 = ps2 ?: emptySet()
                    val c = pse1.size.compareTo(pse2.size)
                    if (c != 0)
                        c
                    else if (pse1 == pse2)
                        0
                    else
                        ps1.toString().compareTo(ps2.toString())
                }

            val wpaComparator = compareBy<W_P_A> { it.word }
                .thenBy { it.posType }
                .thenBy(nullsFirst(pronunciationsComparator)) { it.pronunciations }
        }
    }

    /**
     * (Word, PosOrType, Discriminant) - Shallow key
     *
     * @param word    word: lemma or LC lemma
     * @param posType pos type: part-of-speech or type (P for pos)
     * @property discriminant discriminant (D for discriminant)
     */
    @kotlinx.serialization.Serializable
    open class W_P_D(
        override val word: String,
        override val posType: Char,
        val discriminant: String?,
    ) : BaseKey(), Comparable<W_P_D> {

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other == null || javaClass != other.javaClass) {
                return false
            }
            val that = other as W_P_D
            if (word != that.word) {
                return false
            }
            if (this.posType != that.posType) {
                return false
            }
            return this.discriminant == that.discriminant
        }

        override fun hashCode(): Int {
            return Objects.hash(word, posType, discriminant)
        }

        override fun compareTo(other: W_P_D): Int {
            if (this == other) {
                return 0
            }
            return wpdComparator.compare(this, other)
        }

        override fun toString(): String {
            return "($word,$posType,$discriminant)"
        }

        override fun toLongString(): String {
            return "KEY WPD ${javaClass.simpleName} $this"
        }

        companion object {

            fun of(
                lex: Lex,
                wordExtractor: (Lex) -> String,
                posTypeExtractor: (Lex) -> Char,
            ): W_P_D {
                return W_P_D(wordExtractor(lex), posTypeExtractor(lex), lex.discriminant)
            }

            fun of_t(lex: Lex): W_P_D {
                return of(lex, Lex::lemma, Lex::type)
            }

            fun of_p(lex: Lex): W_P_D {
                return of(lex, Lex::lemma, Lex::partOfSpeech)
            }

            fun of_lc_t(lex: Lex): W_P_D {
                return of(lex, Lex::lCLemma, Lex::type)
            }

            fun of_lc_p(lex: Lex): W_P_D {
                return of(lex, Lex::lCLemma, Lex::partOfSpeech)
            }

            fun from(lemma: String, type: Char, discriminant: String?): W_P_D {
                return W_P_D(lemma, type, discriminant)
            }

            val wpdComparator: Comparator<W_P_D> = compareBy<W_P_D> { it.word }
                .thenBy { it.posType }
                .thenBy(nullsFirst(Comparator.naturalOrder())) { it.discriminant }
        }
    }
}
