/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.junit.Assert
import org.oewntk.model.LibLexGroupings.lemmasByLCLemmaHavingMultipleCount
import org.oewntk.model.LibLexGroupings.countsByLCLemma
import org.oewntk.model.LibLexGroupings.findLemmasFor
import org.oewntk.model.LibLexGroupings.multipleCountsByICLemma
import org.oewntk.model.SenseGroupings.sensesForLCLemma
import org.oewntk.model.SenseGroupings.sensesForLCLemmaAndPos
import org.oewntk.model.TestUtils.lexHypermapForLemmaToString
import org.oewntk.model.TestUtils.lexesToString
import org.oewntk.model.TestUtils.sensesToString
import java.io.PrintStream
import java.util.Locale

object LibTestModelLexGroups {

    fun testCIMultipleAll(model: CoreModel, ps: PrintStream) {
        model.lexes.lemmasByLCLemmaHavingMultipleCount()
            .forEach { (u: String?, cs: Set<String>) ->
                ps.println("$u ${cs.joinToString(separator = ",", prefix = "{", postfix = "}")}")
            }
    }

    fun testCILemmas(model: CoreModel, lemma: Lemma, ps: PrintStream) {
        val lemmas = model.lexes.findLemmasFor(lemma)
        ps.println("$lemma ${lemmas.joinToString(separator = ",", prefix = "{", postfix = "}")}")
    }

    fun testCICounts(model: CoreModel, lemma: Lemma, ps: PrintStream) {
        val count = model.lexes.countsByLCLemma()[lemma]
        ps.printf("%s %d%n", lemma, count)
    }

    fun testCICountsFromMap(model: CoreModel, lemma: Lemma, ps: PrintStream) {
        val count = model.lexes.multipleCountsByICLemma()[lemma]
        ps.printf("%s %d%n", lemma, count)
    }

    fun testCILexesFor3(model: CoreModel, lemma: Lemma, ps: PrintStream) {
        var lemma2 = lemma
        val s1 = testCILexesFor(model, lemma2)
        ps.printf("ci '%s'%n", lemma2)
        ps.println(s1)

        lemma2 = lemma2.lowercase(Locale.ENGLISH)
        val s2 = testCILexesFor(model, lemma2)
        ps.printf("ci '%s'%n", lemma2)
        ps.println(s2)

        lemma2 = lemma2.uppercase()
        val s3 = testCILexesFor(model, lemma2)
        ps.printf("ci '%s'%n", lemma2)
        ps.println(s3)

        Assert.assertEquals(s1, s2)
        Assert.assertEquals(s2, s3)
    }

    fun testCILexesFor(model: CoreModel, lemma: Lemma, ps: PrintStream) {
        ps.printf("ci '%s'%n", lemma)
        ps.println(testCILexesFor(model, lemma))
    }

    private fun testCILexesFor(model: CoreModel, lemma: Lemma): String {
        val lexes = checkNotNull(model.lexIgnoreCaseFinder(lemma))
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

        lemma2 = lemma2.lowercase(Locale.ENGLISH)
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
