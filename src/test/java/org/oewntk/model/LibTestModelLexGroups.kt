/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.junit.Assert
import org.oewntk.model.LexGroupings.cSLemmasByLCLemma
import org.oewntk.model.LexGroupings.cSLemmasByLCLemmaHavingMultipleCount
import org.oewntk.model.LexGroupings.countsByLCLemma
import org.oewntk.model.LexGroupings.multipleCountsByICLemma
import org.oewntk.model.SenseGroupings.sensesForLCLemma
import org.oewntk.model.SenseGroupings.sensesForLCLemmaAndPos
import org.oewntk.model.TestUtils.lexHypermapForLemmaToString
import org.oewntk.model.TestUtils.lexesToString
import org.oewntk.model.TestUtils.sensesToString
import java.io.PrintStream

object LibTestModelLexGroups {

    fun testCIMultipleAll(model: CoreModel, ps: PrintStream) {
        cSLemmasByLCLemmaHavingMultipleCount(model)
            .forEach { (u: String?, cs: Set<String>) ->
                ps.println("$u ${cs.joinToString(separator = ",", prefix = "{", postfix = "}")}")
            }
    }

    fun testCILemmas(model: CoreModel, lemma: Lemma, ps: PrintStream) {
        val lemmas = cSLemmasByLCLemma(model)[lemma]
        ps.println("$lemma ${lemmas?.joinToString(separator = ",", prefix = "{", postfix = "}")}")
    }

    fun testCICounts(model: CoreModel, lemma: Lemma, ps: PrintStream) {
        val count = countsByLCLemma(model)[lemma]
        ps.printf("%s %d%n", lemma, count)
    }

    fun testCICountsFromMap(model: CoreModel, lemma: Lemma, ps: PrintStream) {
        val count = multipleCountsByICLemma(model)[lemma]
        ps.printf("%s %d%n", lemma, count)
    }

    fun testCILexesFor3(model: CoreModel, lemma: Lemma, ps: PrintStream) {
        var lemma2 = lemma
        val s1 = testCILexesString(model, lemma2)
        ps.printf("ci '%s'%n", lemma2)
        ps.println(s1)

        lemma2 = lemma2.lowercase()
        val s2 = testCILexesString(model, lemma2)
        ps.printf("ci '%s'%n", lemma2)
        ps.println(s2)

        lemma2 = lemma2.uppercase()
        val s3 = testCILexesString(model, lemma2)
        ps.printf("ci '%s'%n", lemma2)
        ps.println(s3)

        Assert.assertEquals(s1, s2)
        Assert.assertEquals(s2, s3)
    }

    fun testCILexes(model: CoreModel, lemma: Lemma, ps: PrintStream) {
        ps.printf("ci '%s'%n", lemma)
        ps.println(testCILexesString(model, lemma))
    }

    fun testCILexesFor(model: CoreModel, lemma: Lemma, ps: PrintStream) {
        ps.printf("ci '%s'%n", lemma)
        ps.println(testCILexesForWordString(model, lemma))
    }

    private fun testCILexesString(model: CoreModel, lemma: Lemma): String {
        val map = checkNotNull(model.lexesByLCLemma)
        val lexes = checkNotNull(map[lemma.lowercase()])
        return lexesToString(lexes)
    }

    private fun testCILexesForWordString(model: CoreModel, lemma: Lemma): String {
        val lexes = checkNotNull(model.lexesByLCLemma!![lemma])
        return lexesToString(lexes)
    }

    fun testCISensesGroupingByLCLemmaAndPos(model: CoreModel, lemma: Lemma, category: Category, ps: PrintStream) {
        ps.printf("ci '%s' %s%n", lemma, category)
        ps.println(testCISensesGroupingByLCLemmaAndPosString(model, lemma, category))
    }

    fun testCISensesGroupingByLCLemma(model: CoreModel, lemma: Lemma, ps: PrintStream) {
        ps.printf("ci '%s'%n", lemma)
        ps.println(testCISensesGroupingByLCLemmaString(model, lemma))
    }

    private fun testCISensesGroupingByLCLemmaString(model: CoreModel, lemma: Lemma): String {
        val senses = sensesForLCLemma(model.senses, lemma)
        return sensesToString(senses)
    }

    private fun testCISensesGroupingByLCLemmaAndPosString(model: CoreModel, lemma: Lemma, category: Category): String {
        val senses = sensesForLCLemmaAndPos(model.senses, lemma, category)
        return sensesToString(senses)
    }

    fun testCIHypermap(lexHyperMap: Map<String, Map<String, Collection<Lex>>>, lemma: Lemma, ps: PrintStream) {
        ps.printf("ci '%s'%n", lemma)
        ps.println(testCIHypermapString(lexHyperMap, lemma))
    }

    fun testCIHypermap3(lexHyperMap: Map<String, Map<String, Collection<Lex>>>, lemma: Lemma, ps: PrintStream) {
        var lemma2 = lemma
        val s1 = testCIHypermapString(lexHyperMap, lemma2)
        ps.printf("ci '%s'%n", lemma2)
        ps.println(s1)

        lemma2 = lemma2.lowercase()
        val s2 = testCIHypermapString(lexHyperMap, lemma2)
        ps.printf("ci '%s'%n", lemma2)
        ps.println(s2)

        lemma2 = lemma2.uppercase()
        val s3 = testCIHypermapString(lexHyperMap, lemma2)
        ps.printf("ci '%s'%n", lemma2)
        ps.println(s3)

        Assert.assertEquals(s1, s2)
        Assert.assertEquals(s2, s3)
    }

    private fun testCIHypermapString(lexHyperMap: Map<Lemma, Map<Lemma, Collection<Lex>>>, lemma: Lemma): String {
        return lexHypermapForLemmaToString(lexHyperMap, lemma)
    }
}
