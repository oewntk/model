/*
 * Copyright (c) 2024-2024. Bernard Bou.
 */

package org.oewntk.model

import org.oewntk.model.SenseGroupings.sensesForLCLemma
import org.oewntk.model.SenseGroupings.sensesForLCLemmaAndPos
import org.oewntk.model.TestUtils.sensesToStringByDecreasingTagCount
import java.io.PrintStream

object LibTestModelSenseGroups {

    fun testCISensesGroupingByLCLemmaAndPos(model: CoreModel, word: Lemma, category: Category, ps: PrintStream) {
        ps.printf("ci '%s' %s%n", word, category)
        ps.println(testCISensesGroupingByLCLemmaAndPosString(model, word, category))
    }

    fun testCISensesGroupingByLCLemma(model: CoreModel, word: Lemma, ps: PrintStream) {
        ps.printf("ci '%s'%n", word)
        ps.println(testCISensesGroupingByLCLemmaString(model, word))
    }

    private fun testCISensesGroupingByLCLemmaString(model: CoreModel, lemma: Lemma): String {
        val senses = sensesForLCLemma(model.senses, lemma)
        return sensesToStringByDecreasingTagCount(senses)
    }

    private fun testCISensesGroupingByLCLemmaAndPosString(model: CoreModel, lemma: Lemma, category: Category): String {
        val senses = sensesForLCLemmaAndPos(model.senses, lemma, category)
        return sensesToStringByDecreasingTagCount(senses)
    }
}
