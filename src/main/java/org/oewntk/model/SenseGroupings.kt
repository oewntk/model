/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import java.util.*

/**
 * Sense grouping
 */
object SenseGroupings {

    // S E N S E   M A P S

    /**
     * Senses grouped and mapped by lower-cased lemma
     *
     * @param senses senses
     * @return collections of senses grouped by and mapped by lower-cased lemma
     */
    fun sensesByLCLemma(senses: Collection<Sense>): Map<Lemma, Collection<Sense>> {
        return senses
            .groupBy(Sense::lCLemma)
            .mapValues { it.value.toSet() }
    }

    /**
     * Senses grouped and mapped by lower-cased lemma and part-of-speech
     *
     * @param senses senses
     * @return collections of senses grouped by and mapped by lower-cased lemma and part-of-speech
     */
    fun sensesByLCLemmaAndPos(senses: Collection<Sense>): Map<KeyLCLemmaAndPos, Collection<Sense>> {
        return senses
            .groupBy { KeyLCLemmaAndPos(it) }
            .mapValues { it.value.toSet() }
    }

    // S E N S E S  F O R

    // for debug as it makes a fresh map every time
    /**
     * Find senses matching key built from sense
     *
     * @param senses           senses
     * @param groupingFunction map sense to key
     * @param k                key
     * @param K                type of key
     * @return senses matching this key
     */
    private fun <K> sensesFor(
        senses: Collection<Sense>,
        groupingFunction: (Sense) -> K,
        k: K,
    ): Collection<Sense> {
        return senses.groupBy(groupingFunction)[k]!!
    }

    /**
     * Find senses for target lower-cased lemma and part-of-speech
     *
     * @param senses  senses
     * @param lcLemma target lower-cased lemma
     * @param category     target part-of-speech
     * @return collection of senses for this target lower-cased lemma and part-of-speech
     */
    fun sensesForLCLemmaAndPos(senses: Collection<Sense>, lcLemma: Lemma, category: Category): Collection<Sense> {
        return sensesFor(senses, { sense: Sense -> KeyLCLemmaAndPos(sense) }, KeyLCLemmaAndPos.of(lcLemma, category))
    }

    /**
     * Find senses for target lower-cased lemma
     *
     * @param senses  senses
     * @param lcLemma target lower-cased lemma
     * @return collection of senses for this target lower-cased lemma
     */
    fun sensesForLCLemma(senses: Collection<Sense>, lcLemma: Lemma): Collection<Sense> {
        return sensesFor(senses, Sense::lCLemma, lcLemma)
    }

    // C O M P A R A T O R

    /**
     * Order senses by decreasing frequency order, does not define a total order, must be chained with thenComparing to define a total order, returns 0 if tag counts cannot make one more or less frequent
     */
    val BY_DECREASING_TAGCOUNT: Comparator<Sense> = Comparator { s1: Sense, s2: Sense ->

        // tag count
        val c1 = s1.intTagCount
        val c2 = s2.intTagCount
        val cmp = c1.compareTo(c2)
        if (cmp != 0) {
            // tag counts differ, more frequent (larger count) first
            return@Comparator -cmp
        }
        0
    }

    // L O W E R - C A S E   A N D   P O S   K E Y   ( P W N )

    /**
     * Key that matches how indexes are built in PWN (index.sense and index.(noun|verb|adj|adv)
     */
    class KeyLCLemmaAndPos(lemma: Lemma, val category: Category) : Comparable<KeyLCLemmaAndPos> {

        val lcLemma: Lemma = lemma.lowercase()

        constructor(sense: Sense) : this(sense.lemma, sense.partOfSpeech)

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other == null || javaClass != other.javaClass) {
                return false
            }
            val that = other as KeyLCLemmaAndPos
            return category == that.category && lcLemma == that.lcLemma
        }

        override fun hashCode(): Int {
            return Objects.hash(lcLemma, category)
        }

        override fun compareTo(other: KeyLCLemmaAndPos): Int {
            val cmp = lcLemma.compareTo(other.lcLemma)
            if (cmp != 0) {
                return cmp
            }
            return category.compareTo(other.category)
        }

        override fun toString(): String {
            return "'$lcLemma'-$category"
        }

        companion object {

            fun of(lcLemma: Lemma, category: Category): KeyLCLemmaAndPos {
                return KeyLCLemmaAndPos(lcLemma, category)
            }

            fun of(sense: Sense): KeyLCLemmaAndPos {
                return KeyLCLemmaAndPos(sense)
            }
        }
    }
}
