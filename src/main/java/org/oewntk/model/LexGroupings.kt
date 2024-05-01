/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import java.util.*

object LexGroupings {

    /**
     * Group lexes by case-sensitive lemma
     *
     * @param lexes lexes
     * @return lexes grouped by CS lemma
     */
    fun lexesByLemma(lexes: Collection<Lex>): Map<String, Collection<Lex>> {
        return lexes
            .groupBy(Lex::lemma)
            .mapValues { it.value.toSet() }
    }

    /**
     * Group lexes by lower-cased lemma
     *
     * @param lexes lexes
     * @return lexes grouped by LCS lemma
     */
    fun lexesByLCLemma(lexes: Collection<Lex>): Map<String, Collection<Lex>> {
        return lexes
            .groupBy { it.lCLemma }
            .mapValues { it.value.toSet() }
    }

    /**
     * CSLemmas grouped by LCLemma
     * ```
     * baroque -&gt; (Baroque, baroque)
     *```
     * @param model model
     * @return CS lemmas by LC lemmas
     */
    fun cSLemmasByLCLemma(model: CoreModel): Map<String, Set<String>> {
        return model.lexes
            .map(Lex::lemma)
            .groupBy { it.lowercase(Locale.ENGLISH) }
            .mapValues { it.value.toSortedSet() }
    }

    /**
     * CSLemmas for LCLemma
     *
     * @param model   model
     * @param lcLemma lower-cased lemma
     * @return CS lemmas for given LC lemma
     */
    fun cSLemmasForLCLemma(model: CoreModel, lcLemma: String): Set<String> {
        return cSLemmasByLCLemma(model)[lcLemma]!!
    }

    // hyper maps

    /**
     * Hypermap (LCLemma to CSLemma to lexes)
     *
     * @param model model
     * @return 2-tier hypermap
     * ```
     * (LCLemma -> CSLemma -> lexes)
     * ```
     */
    fun hyperMapByLCLemmaByLemma(model: CoreModel): Map<String, Map<String, Collection<Lex>>> {
        return model.lexesByLemma!!.entries // entries: setOf(lemma to lexes)
            .groupBy { entry -> entry.key.lowercase() } // groupBy: mapOf(lclemma to listOf(lemma to lexes)), entry: lemma to lexes
            .mapValues { values -> // values: lcLemma to listOf(lemma to lexes))
                values.value // value: listOf(lemma to lexes)
                    .associateBy { it.key } // mapOf(lemma to lexes)
                    .mapValues { it.value.value } // it: lemma to (lemma to lexes), it.value: lemma to lexes, it.value.value: lexes
            }
    }

    // counts

    /**
     * Counts of CS lemmas by LC lemma
     *
     * @param model model
     * @return counts of CS lemmas by LC lemmas
     */
    fun countsByLCLemma(model: CoreModel): Map<String, Long> {
        return model.lexes
            .map(Lex::lemma)
            .groupBy { it.lowercase(Locale.ENGLISH) }
            .toSortedMap(naturalOrder())
            .mapValues { it.value.toSet().size.toLong() }
    }

    /**
     * Counts of CS lemmas by LC lemma, same as above but retain entries that have count &gt; 2
     *
     * @param model model
     * @return counts of CS lemmas by LC lemmas, with count &gt; 2
     */
    fun multipleCountsByICLemma(model: CoreModel): Map<String, Long> {
        return model.lexes
            .map(Lex::lemma)
            .groupBy { it.lowercase(Locale.ENGLISH) }
            .mapValues { it.value.toSet().size.toLong() }
            .toList()
            .filter { it.second > 1L }
            .toMap()
            .toSortedMap(naturalOrder())
    }

    /**
     * CS lemmas by LC lemmas, retain entries that have count &gt; 2
     *
     * @param model model
     * @return CS lemmas by LC lemmas, with count &gt; 2
     */
    fun cSLemmasByLCLemmaHavingMultipleCount(model: CoreModel): Map<String, Set<String>> {
        return Groupings.groupByHavingMultipleCount(model.lexes.map(Lex::lemma)) { it.lowercase() }
    }
}
