package org.oewntk.model

import org.oewntk.model.SenseGroupings.sensesForLCLemma
import org.oewntk.model.SenseGroupings.sensesForLCLemmaAndPos
import org.oewntk.model.TestUtils.sensesToStringByDecreasingTagCount
import java.io.PrintStream

object LibTestModelSenseGroups {

	fun testCISensesGroupingByLCLemmaAndPos(model: CoreModel, word: String, pos: Char, ps: PrintStream) {
		ps.printf("ci '%s' %s%n", word, pos)
		ps.println(testCISensesGroupingByLCLemmaAndPosString(model, word, pos))
	}

	fun testCISensesGroupingByLCLemma(model: CoreModel, word: String, ps: PrintStream) {
		ps.printf("ci '%s'%n", word)
		ps.println(testCISensesGroupingByLCLemmaString(model, word))
	}

	private fun testCISensesGroupingByLCLemmaString(model: CoreModel, word: String): String {
		val senses = sensesForLCLemma(model.senses, word)
		return sensesToStringByDecreasingTagCount(senses)
	}

	private fun testCISensesGroupingByLCLemmaAndPosString(model: CoreModel, word: String, pos: Char): String {
		val senses = sensesForLCLemmaAndPos(model.senses, word, pos)
		return sensesToStringByDecreasingTagCount(senses)
	}
}
