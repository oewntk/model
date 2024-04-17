/*
 * Copyright (c) 2022. Bernard Bou.
 */
package org.oewntk.model

import java.io.PrintStream
import java.util.function.Consumer

object LibDumpSenses {

	fun dumpSensesByDecreasingTagCount(senses: List<Sense>, ps: PrintStream) {
		val senses2 = senses.sortedWith(SenseGroupings.BY_DECREASING_TAGCOUNT.thenComparing(Sense::senseKey))
		dumpSenses(senses2, ps)
	}

	fun <K> dumpSensesByDecreasingTagCount(sensesWithKey: Map.Entry<K, List<Sense>>, ps: PrintStream) {
		val k = sensesWithKey.key
		val senses2 = sensesWithKey.value
		ps.printf("%s:%n", k)
		dumpSensesByDecreasingTagCount(senses2, ps)
		ps.println()
	}

	fun dumpSenses(senses: List<Sense>, ps: PrintStream) {
		val i = intArrayOf(0)
		senses.forEach(Consumer { s: Sense -> ps.printf("\t[%d] %2d %s%n", i[0]++, s.intTagCount, s) })
		ps.println()
	}
}
