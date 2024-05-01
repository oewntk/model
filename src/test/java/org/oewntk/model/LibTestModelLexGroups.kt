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
            .forEach { (u: String?, cs: Set<String>?) ->
                ps.println("$u ${cs.joinToString(separator = ",", prefix = "{", postfix = "}")}")
            }
    }

    fun testCILemmas(model: CoreModel, word: String, ps: PrintStream) {
        val lemmas = cSLemmasByLCLemma(model)[word]
        ps.println("$word ${lemmas?.joinToString(separator = ",", prefix = "{", postfix = "}")}")
    }

    fun testCICounts(model: CoreModel, word: String, ps: PrintStream) {
        val count = countsByLCLemma(model)[word]
        ps.printf("%s %d%n", word, count)
    }

    fun testCICountsFromMap(model: CoreModel, word: String, ps: PrintStream) {
        val count = multipleCountsByICLemma(model)[word]
        ps.printf("%s %d%n", word, count)
    }

    fun testCILexesFor3(model: CoreModel, word: String, ps: PrintStream) {
        var word2 = word
        val s1 = testCILexesString(model, word2)
        ps.printf("ci '%s'%n", word2)
        ps.println(s1)

        word2 = word2.lowercase()
        val s2 = testCILexesString(model, word2)
        ps.printf("ci '%s'%n", word2)
        ps.println(s2)

        word2 = word2.uppercase()
        val s3 = testCILexesString(model, word2)
        ps.printf("ci '%s'%n", word2)
        ps.println(s3)

        Assert.assertEquals(s1, s2)
        Assert.assertEquals(s2, s3)
    }

    fun testCILexes(model: CoreModel, word: String, ps: PrintStream) {
        ps.printf("ci '%s'%n", word)
        ps.println(testCILexesString(model, word))
    }

    fun testCILexesFor(model: CoreModel, word: String, ps: PrintStream) {
        ps.printf("ci '%s'%n", word)
        ps.println(testCILexesForWordString(model, word))
    }

    private fun testCILexesString(model: CoreModel, word: String): String {
        val map = checkNotNull(model.lexesByLCLemma)
        val lexes = checkNotNull(map[word.lowercase()])
        return lexesToString(lexes)
    }

    private fun testCILexesForWordString(model: CoreModel, word: String): String {
        val lexes = checkNotNull(model.lexesByLCLemma!![word])
        return lexesToString(lexes)
    }

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
        return sensesToString(senses)
    }

    private fun testCISensesGroupingByLCLemmaAndPosString(model: CoreModel, word: String, pos: Char): String {
        val senses = sensesForLCLemmaAndPos(model.senses, word, pos)
        return sensesToString(senses)
    }

    fun testCIHypermap(lexHyperMap: Map<String, Map<String, Collection<Lex>>>, word: String, ps: PrintStream) {
        ps.printf("ci '%s'%n", word)
        ps.println(testCIHypermapString(lexHyperMap, word))
    }

    fun testCIHypermap3(lexHyperMap: Map<String, Map<String, Collection<Lex>>>, word: String, ps: PrintStream) {
        var word2 = word
        val s1 = testCIHypermapString(lexHyperMap, word2)
        ps.printf("ci '%s'%n", word2)
        ps.println(s1)

        word2 = word2.lowercase()
        val s2 = testCIHypermapString(lexHyperMap, word2)
        ps.printf("ci '%s'%n", word2)
        ps.println(s2)

        word2 = word2.uppercase()
        val s3 = testCIHypermapString(lexHyperMap, word2)
        ps.printf("ci '%s'%n", word2)
        ps.println(s3)

        Assert.assertEquals(s1, s2)
        Assert.assertEquals(s2, s3)
    }

    private fun testCIHypermapString(lexHyperMap: Map<String, Map<String, Collection<Lex>>>, lemma: String): String {
        return lexHypermapForLemmaToString(lexHyperMap, lemma)
    }
}
