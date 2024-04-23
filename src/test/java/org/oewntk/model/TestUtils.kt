/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.model

import java.io.StringWriter
import java.util.function.Consumer

object TestUtils {

	fun sensesToString(senses: Collection<Sense?>?): String {
		if (senses.isNullOrEmpty()) {
			return "\t<none>"
		}
		val sw = StringWriter()
		senses.forEach(Consumer { sense: Sense? -> sw.write(String.format("\t%s%n", sense)) })
		return sw.toString()
	}

	fun sensesToStringByDecreasingTagCount(senses: Collection<Sense>?): String {
		if (senses.isNullOrEmpty()) {
			return "\t<none>"
		}
		val sw = StringWriter()
		senses
			.sortedWith(SenseGroupings.BY_DECREASING_TAGCOUNT.thenComparing(Sense::senseKey))
			.forEach {
				sw.write(String.format("\t%d %s%n", it.intTagCount, it))
			}
		return sw.toString()
	}

	fun lexesToString(lexes: Collection<Lex>): String {
		if (lexes.isEmpty()) {
			return "\t<none>"
		}
		val sw = StringWriter()
		lexes.forEach(Consumer { lex: Lex? -> sw.write(String.format("\t%s%n", lex)) })
		return sw.toString()
	}

	fun lexHypermapForLemmaToString(lexHypermap: Map<String, Map<String, Collection<Lex>>>, lemma: String): String {
		val map = lexHypermap[lemma.lowercase()]!!
		val sw = StringWriter()
		map.keys.forEach(Consumer { cs: String? ->
			sw.write(String.format("\tcs '%s'%n", cs))
			map[cs]?.forEach(Consumer { s: Lex? -> sw.write(String.format("\t\t%s%n", s)) })
		})
		return sw.toString()
	}
}
