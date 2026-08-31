package org.oewntk.model

import java.util.*

object LibLexGroupings {

    /**
     * Lemmas grouped by LCLemma
     * ```
     * baroque -&gt; (Baroque, baroque)
     *```
     * @receiver lexes
     * @return lemmas by LC lemmas
     */
    private val Collection<Lex>.lemmasByLCLemma: Map<String, Set<Lemma>>
        get() = this
            .map(Lex::lemma)
            .groupBy { it.lowercased }
            .mapValues { it.value.toSortedSet() }

    /**
     * Lemmas for LCLemma
     *
     * @receiver lexes
     * @param target string for lemma
     * @return lemmas for given target
     */
    fun Collection<Lex>.findLemmasFor(target: Lemma): Set<Lemma> {
        return lemmasByLCLemma[target.lowercased]!!
    }

    // hyper maps

    /**
     * Lemmas by LC lemmas, retain entries that have count &gt; 2
     *
     * @receiver lexes
     * @return lemmas by LC lemmas, with count &gt; 2
     */
    fun Collection<Lex>.lemmasByLCLemmaHavingMultipleCount(): Map<String, Set<Lemma>> {
        return Groupings.groupByHavingMultipleCount(this.map(Lex::lemma)) { it.lowercased }
    }

    // counts

    /**
     * Counts of lemmas by LC lemma
     *
     * @receiver lexes
     * @return counts of lemmas by LC lemmas
     */
    fun Collection<Lex>.countsByLCLemma(): Map<String, Long> {
        return this
            .map(Lex::lemma)
            .groupBy { it.lowercased }
            .toSortedMap(naturalOrder())
            .mapValues { it.value.toSet().size.toLong() }
    }

    /**
     * Counts of lemmas by LC lemma, same as above but retain entries that have count &gt; 2
     *
     * @receiver lexes
     * @return counts of lemmas by LC lemmas, with count &gt; 2
     */
    fun Collection<Lex>.multipleCountsByICLemma(): Map<String, Long> {
        return this
            .map(Lex::lemma)
            .groupBy { it.lowercased }
            .mapValues { it.value.toSet().size.toLong() }
            .toList()
            .filter { it.second > 1L }
            .toMap()
            .toSortedMap(naturalOrder())
    }
}