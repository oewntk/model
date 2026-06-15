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
    sealed class Proto : Key, Serializable {

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
    open class Base(
        override val lemma: Lemma,
        override val category: Category,
    ) : Proto(), Comparable<Base>, Serializable {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Base) return false
            return if (lemma != other.lemma) false else this.category == other.category
        }

        override fun hashCode(): Int = Objects.hash(lemma, category)

        override fun compareTo(other: Base): Int = if (this == other) 0 else wpComparator.compare(this, other)

        override fun toString(): String = "($lemma,$category)"

        override fun toLongString(): String = "KEY LC ${javaClass.simpleName} $this"

        companion object {

            fun of(
                lex: Lex,
                lemmaExtractor: (Lex) -> Lemma = Lex::lemma,
                categoryExtractor: (Lex) -> Category = { it.type.toCategory() },
            ): Base = Base(lemmaExtractor(lex), categoryExtractor(lex))

            fun ofIgnoringCase(lex: Lex): Base = of(lex, Lex::lCLemma)

            fun ofIgnoringCaseUsingPartOfSpeech(lex: Lex): Base = of(lex) { it.partOfSpeech.toCategory() }

            fun ofUsingPartOfSpeech(lex: Lex): Base = of(lex) { it.partOfSpeech.toCategory() }

            fun from(lemma: Lemma, category: Category): Base = Base(lemma, category)

            val wpComparator: Comparator<Base> = Comparator
                .comparing { k: Base -> k.lemma }
                .thenComparing { k: Base -> k.category }

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
    ) : Proto(), Comparable<UsingDiscriminant> {

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
            ): UsingDiscriminant = UsingDiscriminant(lemmaExtractor(lex), categoryExtractor(lex), lex.discriminant)

            fun ofIgnoringCase(lex: Lex): UsingDiscriminant = of(lex, Lex::lCLemma)

            fun ofUsingPartOfSpeech(lex: Lex): UsingDiscriminant = of(lex) { it.partOfSpeech.toCategory() }

            fun from(lemma: Lemma, category: Category, discriminant: Discriminant?): UsingDiscriminant = UsingDiscriminant(lemma, category, discriminant)

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
    ) : Proto(), Comparable<UsingPronunciation> {

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
                lemmaExtractor: (Lex) -> Lemma = Lex::lemma,
                categoryExtractor: (Lex) -> Category = { it.type.toCategory() },
            ): UsingPronunciation = UsingPronunciation(lemmaExtractor(lex), categoryExtractor(lex), lex.pronunciations?.toSet())

            fun ofUsingPartOfSpeech(lex: Lex): UsingPronunciation = of(lex) { it.partOfSpeech.toCategory() }

            fun ofIgnoringCase(lex: Lex): UsingPronunciation = of(lex, Lex::lCLemma)

            fun from(lemma: Lemma, category: Category, pronunciations: Set<Pronunciation>): UsingPronunciation = UsingPronunciation(lemma, category, pronunciations)

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