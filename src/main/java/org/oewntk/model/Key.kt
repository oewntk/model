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
            if (this === other) return true
            if (other !is FromLemmaCategory) return false
            return if (lemma != other.lemma) false else this.category == other.category
        }

        override fun hashCode(): Int = Objects.hash(lemma, category)

        override fun compareTo(other: FromLemmaCategory): Int = if (this == other) 0 else wpComparator.compare(this, other)

        override fun toString(): String = "($lemma,$category)"

        override fun toLongString(): String = "KEY LC ${javaClass.simpleName} $this"

        companion object {

            fun of(
                lex: Lex,
                lemmaExtractor: (Lex) -> Lemma = Lex::lemma,
                categoryExtractor: (Lex) -> Category = { it.type.toCategory() },
            ): FromLemmaCategory {
                return FromLemmaCategory(lemmaExtractor(lex), categoryExtractor(lex))
            }

            fun of_t(lex: Lex): FromLemmaCategory = of(lex)

            fun of_p(lex: Lex): FromLemmaCategory = of(lex) { it.partOfSpeech.toCategory() }

            fun of_lc_t(lex: Lex): FromLemmaCategory = of(lex, Lex::lCLemma)

            fun of_lc_p(lex: Lex): FromLemmaCategory = of(lex) { it.partOfSpeech.toCategory() }

            fun from(lemma: Lemma, category: Category): FromLemmaCategory {
                return FromLemmaCategory(lemma, category)
            }

            val wpComparator: Comparator<FromLemmaCategory> = Comparator
                .comparing { k: FromLemmaCategory -> k.lemma }
                .thenComparing { k: FromLemmaCategory -> k.category }

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
    open class UsingDiscriminant(
        override val lemma: Lemma,
        override val category: Category,
        val discriminant: Discriminant?,
    ) : Base(), Comparable<UsingDiscriminant> {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is UsingDiscriminant) return false
            if (lemma != other.lemma)
                return false
            if (this.category != other.category)
                return false
            return this.discriminant == other.discriminant
        }

        override fun hashCode(): Int = Objects.hash(lemma, category, discriminant)

        override fun compareTo(other: UsingDiscriminant): Int = if (this == other) 0 else wpdComparator.compare(this, other)

        override fun toString(): String = "($lemma,$category,$discriminant)"

        override fun toLongString(): String = "KEY LCD ${javaClass.simpleName} $this"

        companion object {

            fun of(
                lex: Lex,
                lemmaExtractor: (Lex) -> Lemma = Lex::lemma,
                categoryExtractor: (Lex) -> Category = { it.type.toCategory() },
            ): UsingDiscriminant {
                return UsingDiscriminant(lemmaExtractor(lex), categoryExtractor(lex), lex.discriminant)
            }

            fun of_t(lex: Lex): UsingDiscriminant = of(lex)

            fun of_p(lex: Lex): UsingDiscriminant  = of(lex, Lex::lemma) { it.partOfSpeech.toCategory() }

            fun of_lc_t(lex: Lex): UsingDiscriminant  = of(lex, Lex::lCLemma) { it.type.toCategory() }

            fun of_lc_p(lex: Lex): UsingDiscriminant = of(lex, Lex::lCLemma) { it.partOfSpeech.toCategory() }

            fun from(lemma: Lemma, category: Category, discriminant: Discriminant?): UsingDiscriminant {
                return UsingDiscriminant(lemma, category, discriminant)
            }

            val wpdComparator: Comparator<UsingDiscriminant> = compareBy<UsingDiscriminant> { it.lemma }
                .thenBy { it.category }
                .thenBy(nullsFirst(Comparator.naturalOrder())) { it.discriminant }
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
    open class UsingPronunciation(
        override var lemma: Lemma,
        override var category: Category,
        val pronunciations: Set<Pronunciation>?,
    ) : Key.Base(), Comparable<UsingPronunciation> {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is UsingPronunciation) return false
            if (lemma != other.lemma)
                return false
            if (this.category != other.category)
                return false
            return (pronunciations ?: emptySet()) == (other.pronunciations ?: emptySet<Set<Pronunciation>>())
        }

        override fun hashCode(): Int = Objects.hash(lemma, category, pronunciations)

        override fun compareTo(other: UsingPronunciation): Int = if (this == other) 0 else wpaComparator.compare(this, other)

        override fun toString(): String = "($lemma,$category,$pronunciations)"

        override fun toLongString(): String = "KEY LCP ${javaClass.simpleName} $this"

        companion object {

            fun of(
                lex: Lex,
                lemmaExtractor: (Lex) -> Lemma,
                categoryExtractor: (Lex) -> Category,
            ): UsingPronunciation {
                return UsingPronunciation(lemmaExtractor(lex), categoryExtractor(lex), lex.pronunciations)
            }

            fun of_t(lex: Lex): UsingPronunciation {
                return of(lex, Lex::lemma) { it.type.toCategory() }
            }

            fun of_p(lex: Lex): UsingPronunciation {
                return of(lex, Lex::lemma) { it.partOfSpeech.toCategory() }
            }

            fun of_lc_t(lex: Lex): UsingPronunciation {
                return of(lex, Lex::lCLemma) { it.type.toCategory() }
            }

            fun from(lemma: Lemma, category: Category, pronunciations: Set<Pronunciation>): UsingPronunciation {
                return UsingPronunciation(lemma, category, pronunciations)
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

            val wpaComparator = compareBy<UsingPronunciation> { it.lemma }
                .thenBy { it.category }
                .thenBy(nullsFirst(pronunciationsComparator)) { it.pronunciations }
        }
    }
}