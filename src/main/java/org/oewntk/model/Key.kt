/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
@file:Suppress("FunctionName")

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
    sealed class Base : Key, Serializable {

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
    open class FromLemmaCategory(
        override val lemma: Lemma,
        override val category: Category,
    ) : Base(), Comparable<FromLemmaCategory>, Serializable {

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other == null || javaClass != other.javaClass) {
                return false
            }
            val that = other as FromLemmaCategory
            if (lemma != that.lemma) {
                return false
            }
            return this.category == that.category
        }

        override fun hashCode(): Int {
            return Objects.hash(lemma, category)
        }

        override fun compareTo(other: FromLemmaCategory): Int {
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
            ): FromLemmaCategory {
                return FromLemmaCategory(lemmaExtractor(lex), categoryExtractor(lex))
            }

            fun of_t(lex: Lex): FromLemmaCategory {
                return of(lex, Lex::lemma) { it.type.toCategory() }
            }

            fun of_p(lex: Lex): FromLemmaCategory {
                return of(lex, Lex::lemma) { it.partOfSpeech.toCategory() }
            }

            fun of_lc_t(lex: Lex): FromLemmaCategory {
                return of(lex, Lex::lCLemma) { it.type.toCategory() }
            }

            fun of_lc_p(lex: Lex): FromLemmaCategory {
                return of(lex, Lex::lCLemma) { it.partOfSpeech.toCategory() }
            }

            fun from(lemma: Lemma, category: Category): FromLemmaCategory {
                return FromLemmaCategory(lemma, category)
            }

            val wpComparator: Comparator<FromLemmaCategory> = Comparator
                .comparing { k: FromLemmaCategory -> k.lemma }
                .thenComparing { k: FromLemmaCategory -> k.category }

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
    open class FromLemmaCategoryPronunciation(
        override var lemma: Lemma,
        override var category: Category,
        val pronunciations: Set<Pronunciation>?,
    ) : Base(), Comparable<FromLemmaCategoryPronunciation> {

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other == null || javaClass != other.javaClass) {
                return false
            }
            val that = other as FromLemmaCategoryPronunciation
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

        override fun compareTo(other: FromLemmaCategoryPronunciation): Int {
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
            ): FromLemmaCategoryPronunciation {
                return FromLemmaCategoryPronunciation(lemmaExtractor(lex), categoryExtractor(lex), lex.pronunciations)
            }

            fun of_t(lex: Lex): FromLemmaCategoryPronunciation {
                return of(lex, Lex::lemma) { it.type.toCategory() }
            }

            fun of_p(lex: Lex): FromLemmaCategoryPronunciation {
                return of(lex, Lex::lemma) { it.partOfSpeech.toCategory() }
            }

            fun of_lc_t(lex: Lex): FromLemmaCategoryPronunciation {
                return of(lex, Lex::lCLemma) { it.type.toCategory() }
            }

            fun of_lc_p(lex: Lex): FromLemmaCategoryPronunciation {
                return of(lex, Lex::lCLemma) { it.partOfSpeech.toCategory() }
            }

            fun from(lemma: Lemma, category: Category, pronunciations: Set<Pronunciation>): FromLemmaCategoryPronunciation {
                return FromLemmaCategoryPronunciation(lemma, category, pronunciations)
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

            val wpaComparator = compareBy<FromLemmaCategoryPronunciation> { it.lemma }
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
    open class FromLemmaCategoryDiscriminant(
        override val lemma: Lemma,
        override val category: Category,
        val discriminant: Discriminant?,
    ) : Base(), Comparable<FromLemmaCategoryDiscriminant> {

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other == null || javaClass != other.javaClass) {
                return false
            }
            val that = other as FromLemmaCategoryDiscriminant
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

        override fun compareTo(other: FromLemmaCategoryDiscriminant): Int {
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
            ): FromLemmaCategoryDiscriminant {
                return FromLemmaCategoryDiscriminant(lemmaExtractor(lex), categoryExtractor(lex), lex.discriminant)
            }

            fun of_t(lex: Lex): FromLemmaCategoryDiscriminant {
                return of(lex, Lex::lemma) { it.type.toCategory() }
            }

            fun of_p(lex: Lex): FromLemmaCategoryDiscriminant {
                return of(lex, Lex::lemma) { it.partOfSpeech.toCategory() }
            }

            fun of_lc_t(lex: Lex): FromLemmaCategoryDiscriminant {
                return of(lex, Lex::lCLemma) { it.type.toCategory() }
            }

            fun of_lc_p(lex: Lex): FromLemmaCategoryDiscriminant {
                return of(lex, Lex::lCLemma) { it.partOfSpeech.toCategory() }
            }

            fun from(lemma: Lemma, category: Category, discriminant: Discriminant?): FromLemmaCategoryDiscriminant {
                return FromLemmaCategoryDiscriminant(lemma, category, discriminant)
            }

            val wpdComparator: Comparator<FromLemmaCategoryDiscriminant> = compareBy<FromLemmaCategoryDiscriminant> { it.lemma }
                .thenBy { it.category }
                .thenBy(nullsFirst(Comparator.naturalOrder())) { it.discriminant }
        }
    }
}
