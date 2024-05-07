/*
 * Copyright (c) 2024-2024. Bernard Bou.
 */

package org.oewntk.model

import org.oewntk.model.SenseGroupings.sensesForLCLemma
import org.oewntk.model.SenseGroupings.sensesForLCLemmaAndPos
import org.oewntk.model.TestUtils.sensesToStringByDecreasingTagCount
import java.io.PrintStream

object LibTestModelSenseGroups {

    fun testCISensesGroupingByLCLemmaAndPos(model: CoreModel, word: LemmaType, pos: PosType, ps: PrintStream) {
        ps.printf("ci '%s' %s%n", word, pos)
        ps.println(testCISensesGroupingByLCLemmaAndPosString(model, word, pos))
    }

    fun testCISensesGroupingByLCLemma(model: CoreModel, word: LemmaType, ps: PrintStream) {
        ps.printf("ci '%s'%n", word)
        ps.println(testCISensesGroupingByLCLemmaString(model, word))
    }

    private fun testCISensesGroupingByLCLemmaString(model: CoreModel, word: LemmaType): String {
        val senses = sensesForLCLemma(model.senses, word)
        return sensesToStringByDecreasingTagCount(senses)
    }

    private fun testCISensesGroupingByLCLemmaAndPosString(model: CoreModel, word: LemmaType, pos: PosType): String {
        val senses = sensesForLCLemmaAndPos(model.senses, word, pos)
        return sensesToStringByDecreasingTagCount(senses)
    }
}
