/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import java.io.Serializable
import java.util.*

/**
 * Keys are to be interpreted here in the relation database sense:
 * A tuple that uniquely identifies an entry (Mono) or a set of entries (Multi).
 * The functional extension KeyF just produces these entries.
 */
interface Key {

    @kotlinx.serialization.Serializable
    sealed class BaseKey : Key, Serializable {

        abstract val lemma: Lemma

        abstract val category: Category

        abstract fun toLongString(): String
    }

    /**
     * (Lemma, Category)
     *
     * @property lemma    lemma or LC lemma
     * @property category category: part-of-speech or type (C for category)
     */
    @kotlinx.serialization.Serializable
    open class KeyLC(
        override val lemma: Lemma,
        override val category: Category,
    ) : BaseKey(), Comparable<KeyLC>, Serializable {

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other == null || javaClass != other.javaClass) {
                return false
            }
            val that = other as KeyLC
            if (lemma != that.lemma) {
                return false
            }
            return this.category == that.category
        }

        override fun hashCode(): Int {
            return Objects.hash(lemma, category)
        }

        override fun compareTo(other: KeyLC): Int {
            if (this == other) {
                return 0
            }
            return wpComparator.compare(this, other)
        }

        override fun toString(): String {
            return "($lemma,$category)"
        }

        override fun toLongString(): String {
            return "KEY LC ${javaClass.simpleName} $this"
        }

        companion object {

            fun of(
                lex: Lex,
                lemmaExtractor: (Lex) -> Lemma,
                categoryExtractor: (Lex) -> Category,
            ): KeyLC {
                return KeyLC(lemmaExtractor(lex), categoryExtractor(lex))
            }

            fun of_t(lex: Lex): KeyLC {
                return of(lex, Lex::lemma, Lex::type)
            }

            fun of_p(lex: Lex): KeyLC {
                return of(lex, Lex::lemma, Lex::partOfSpeech)
            }

            fun of_lc_t(lex: Lex): KeyLC {
                return of(lex, Lex::lCLemma, Lex::type)
            }

            fun of_lc_p(lex: Lex): KeyLC {
                return of(lex, Lex::lCLemma, Lex::partOfSpeech)
            }

            fun from(lemma: Lemma, category: Category): KeyLC {
                return KeyLC(lemma, category)
            }

            val wpComparator: Comparator<KeyLC> = Comparator
                .comparing { k: KeyLC -> k.lemma }
                .thenComparing { k: KeyLC -> k.category }

        }
    }

    /**
     * (Lemma, Category, Pronunciations)
     *
     * @param lemma             lemma or LC lemma (L)
     * @param category          category: part-of-speech or type (C)
     * @property pronunciations pronunciations (P)
     */
    @kotlinx.serialization.Serializable
    open class KeyLCP(
        override var lemma: Lemma,
        override var category: Category,
        val pronunciations: Set<Pronunciation>?,
    ) : BaseKey(), Comparable<KeyLCP> {

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other == null || javaClass != other.javaClass) {
                return false
            }
            val that = other as KeyLCP
            if (lemma != that.lemma) {
                return false
            }
            if (this.category != that.category) {
                return false
            }
            val p1 = pronunciations ?: emptySet()
            val p2 = that.pronunciations ?: emptySet()
            return p1 == p2
        }

        override fun hashCode(): Int {
            return Objects.hash(lemma, category, pronunciations)
        }

        override fun compareTo(other: KeyLCP): Int {
            if (this == other) {
                return 0
            }
            return wpaComparator.compare(this, other)
        }

        override fun toString(): String {
            return "($lemma,$category,$pronunciations)"
        }

        override fun toLongString(): String {
            return "KEY LCP ${javaClass.simpleName} $this"
        }

        companion object {

            fun of(
                lex: Lex,
                lemmaExtractor: (Lex) -> Lemma,
                categoryExtractor: (Lex) -> Category,
            ): KeyLCP {
                return KeyLCP(lemmaExtractor(lex), categoryExtractor(lex), lex.pronunciations)
            }

            fun of_t(lex: Lex): KeyLCP {
                return of(lex, Lex::lemma, Lex::type)
            }

            fun of_p(lex: Lex): KeyLCP {
                return of(lex, Lex::lemma, Lex::partOfSpeech)
            }

            fun of_lc_t(lex: Lex): KeyLCP {
                return of(lex, Lex::lCLemma, Lex::type)
            }

            fun of_lc_p(lex: Lex): KeyLCP {
                return of(lex, Lex::lCLemma, Lex::partOfSpeech)
            }

            fun from(lemma: Lemma, type: Category, pronunciations: Set<Pronunciation>): KeyLCP {
                return KeyLCP(lemma, type, pronunciations)
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

            val wpaComparator = compareBy<KeyLCP> { it.lemma }
                .thenBy { it.category }
                .thenBy(nullsFirst(pronunciationsComparator)) { it.pronunciations }
        }
    }

    /**
     * (Lemma, Category, Discriminant) - Shallow key
     *
     * @param lemma           lemma or LC lemma
     * @param category        category: part-of-speech or type (C for category)
     * @property discriminant discriminant (D for discriminant)
     */
    @kotlinx.serialization.Serializable
    open class KeyLCD(
        override val lemma: Lemma,
        override val category: Category,
        val discriminant: Discriminant?,
    ) : BaseKey(), Comparable<KeyLCD> {

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other == null || javaClass != other.javaClass) {
                return false
            }
            val that = other as KeyLCD
            if (lemma != that.lemma) {
                return false
            }
            if (this.category != that.category) {
                return false
            }
            return this.discriminant == that.discriminant
        }

        override fun hashCode(): Int {
            return Objects.hash(lemma, category, discriminant)
        }

        override fun compareTo(other: KeyLCD): Int {
            if (this == other) {
                return 0
            }
            return wpdComparator.compare(this, other)
        }

        override fun toString(): String {
            return "($lemma,$category,$discriminant)"
        }

        override fun toLongString(): String {
            return "KEY LCD ${javaClass.simpleName} $this"
        }

        companion object {

            fun of(
                lex: Lex,
                lemmaExtractor: (Lex) -> Lemma,
                categoryExtractor: (Lex) -> Category,
            ): KeyLCD {
                return KeyLCD(lemmaExtractor(lex), categoryExtractor(lex), lex.discriminant)
            }

            fun of_t(lex: Lex): KeyLCD {
                return of(lex, Lex::lemma, Lex::type)
            }

            fun of_p(lex: Lex): KeyLCD {
                return of(lex, Lex::lemma, Lex::partOfSpeech)
            }

            fun of_lc_t(lex: Lex): KeyLCD {
                return of(lex, Lex::lCLemma, Lex::type)
            }

            fun of_lc_p(lex: Lex): KeyLCD {
                return of(lex, Lex::lCLemma, Lex::partOfSpeech)
            }

            fun from(lemma: Lemma, category: Category, discriminant: Discriminant?): KeyLCD {
                return KeyLCD(lemma, category, discriminant)
            }

            val wpdComparator: Comparator<KeyLCD> = compareBy<KeyLCD> { it.lemma }
                .thenBy { it.category }
                .thenBy(nullsFirst(Comparator.naturalOrder())) { it.discriminant }
        }
    }
}
