/*
 * Copyright (c) 2022-2024. Bernard Bou.
 */
package org.oewntk.model

import java.io.PrintStream

object LibDumpSenses {

    private fun dumpSensesByDecreasingTagCount(senses: List<Sense>, ps: PrintStream) {
        val senses2 = senses.sortedWith(SenseGroupings.BY_DECREASING_TAGCOUNT.thenComparing(Sense::senseKey))
        dumpSenses(senses2, ps)
    }

    fun <K> dumpSensesByDecreasingTagCount(sensesWithKey: Pair<K, List<Sense>>, ps: PrintStream) {
        val k = sensesWithKey.first
        val senses2 = sensesWithKey.second
        ps.printf("%s:%n", k)
        dumpSensesByDecreasingTagCount(senses2, ps)
        ps.println()
    }

    private fun dumpSenses(senses: List<Sense>, ps: PrintStream) {
        senses
            .withIndex()
            .forEach { (index, sense) ->
                ps.printf("\t[%d] %2d %s%n", index, sense.intTagCount, sense)
            }
        ps.println()
    }
}
