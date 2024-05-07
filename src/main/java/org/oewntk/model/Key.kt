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

        abstract val word: LemmaType

        abstract val pos: PosId
    }

    /**
     * (Word, PosOrType)
     *
     * @property word    word: lemma or LC lemma
     * @property pos pos type: part-of-speech or type (P for pos)
     */
    @kotlinx.serialization.Serializable
    open class W_P(
        override val word: LemmaType,
        override val pos: PosId,
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
            return this.pos == that.pos
        }

        override fun hashCode(): Int {
            return Objects.hash(word, pos)
        }

        override fun compareTo(other: W_P): Int {
            if (this == other) {
                return 0
            }
            return wpComparator.compare(this, other)
        }

        override fun toString(): String {
            return "($word,$pos)"
        }

        override fun toLongString(): String {
            return "KEY WP ${javaClass.simpleName} $this"
        }

        companion object {

            fun of(
                lex: Lex,
                wordExtractor: (Lex) -> LemmaType,
                posExtractor: (Lex) -> PosId,
            ): W_P {
                return W_P(wordExtractor(lex), posExtractor(lex))
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

            fun from(word: LemmaType, pos: PosId): W_P {
                return W_P(word, pos)
            }

            val wpComparator: Comparator<W_P> = Comparator.comparing { obj: W_P -> obj.word }
                .thenComparing { obj: W_P -> obj.pos }

        }
    }

    /**
     * (Word, PosOrType, Pronunciations)
     *
     * @param word    word: lemma or LC lemma
     * @param pos pos type: part-of-speech or type (P)
     * @property pronunciations pronunciations (A for audio)
     */
    @kotlinx.serialization.Serializable
    open class W_P_A(
        override var word: LemmaType,
        override var pos: PosId,
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
            if (this.pos != that.pos) {
                return false
            }
            val p1 = pronunciations ?: emptySet()
            val p2 = that.pronunciations ?: emptySet()
            return p1 == p2
        }

        override fun hashCode(): Int {
            return Objects.hash(word, pos, pronunciations)
        }

        override fun compareTo(other: W_P_A): Int {
            if (this == other) {
                return 0
            }
            return wpaComparator.compare(this, other)
        }

        override fun toString(): String {
            return "($word,$pos,$pronunciations)"
        }

        override fun toLongString(): String {
            return "KEY WPA ${javaClass.simpleName} $this"
        }

        companion object {

            fun of(
                lex: Lex,
                wordExtractor: (Lex) -> LemmaType,
                posExtractor: (Lex) -> PosId,
            ): W_P_A {
                return W_P_A(wordExtractor(lex), posExtractor(lex), lex.pronunciations)
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

            fun from(lemma: LemmaType, type: PosId, pronunciations: Set<Pronunciation>): W_P_A {
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
                .thenBy { it.pos }
                .thenBy(nullsFirst(pronunciationsComparator)) { it.pronunciations }
        }
    }

    /**
     * (Word, PosOrType, Discriminant) - Shallow key
     *
     * @param word    word: lemma or LC lemma
     * @param pos pos type: part-of-speech or type (P for pos)
     * @property discriminant discriminant (D for discriminant)
     */
    @kotlinx.serialization.Serializable
    open class W_P_D(
        override val word: LemmaType,
        override val pos: PosId,
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
            if (this.pos != that.pos) {
                return false
            }
            return this.discriminant == that.discriminant
        }

        override fun hashCode(): Int {
            return Objects.hash(word, pos, discriminant)
        }

        override fun compareTo(other: W_P_D): Int {
            if (this == other) {
                return 0
            }
            return wpdComparator.compare(this, other)
        }

        override fun toString(): String {
            return "($word,$pos,$discriminant)"
        }

        override fun toLongString(): String {
            return "KEY WPD ${javaClass.simpleName} $this"
        }

        companion object {

            fun of(
                lex: Lex,
                wordExtractor: (Lex) -> LemmaType,
                posExtractor: (Lex) -> PosId,
            ): W_P_D {
                return W_P_D(wordExtractor(lex), posExtractor(lex), lex.discriminant)
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
                .thenBy { it.pos }
                .thenBy(nullsFirst(Comparator.naturalOrder())) { it.discriminant }
        }
    }
}
