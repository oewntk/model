/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import java.io.StringWriter

object TestUtils {

    fun sensesToString(senses: Collection<Sense?>?): String {
        if (senses.isNullOrEmpty()) {
            return "\t<none>"
        }
        val sw = StringWriter()
        senses.forEach { sw.write("\t$it\n") }
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
                sw.write("\t${it.intTagCount} $it\n")
            }
        return sw.toString()
    }

    fun lexesToString(lexes: Collection<Lex>): String {
        if (lexes.isEmpty()) {
            return "\t<none>"
        }
        val sw = StringWriter()
        lexes.forEach { sw.write("\t$it\n") }
        return sw.toString()
    }

    fun lexHypermapForLemmaToString(lexHypermap: Map<LemmaType, Map<LemmaType, Collection<Lex>>>, lemma: LemmaType): String {
        val map = lexHypermap[lemma.lowercase()]!!
        val sw = StringWriter()
        map.keys.forEach { cs ->
            sw.write("\tcs '$cs'\n")
            map[cs]?.forEach { sw.write("\t\t$it\n") }
        }
        return sw.toString()
    }
}
