/*
 * Copyright (c) 2024-2024. Bernard Bou.
 */

package org.oewntk.model

import org.oewntk.model.Finder.getLexes
import org.oewntk.model.Finder.getLexesHavingPos
import org.oewntk.model.Key.Base.Companion.ofIgnoringCaseUsingPartOfSpeech
import org.oewntk.model.Key.UsingPronunciation.Companion.ofUsingPartOfSpeech
import java.io.PrintStream

object LibTestModel {

    fun makeIndexMap(seq: Sequence<Key>): Map<Key, Int> {
        return seq
            .withIndex()
            .map { it.value to it.index }
            .groupBy { it.first }
            .mapValues { (_, values) ->
                values
                    .map { it.second }
                    .reduce { existing, replacement ->
                        require(existing != replacement) { "$existing,$replacement" }
                        existing
                    }
            }
    }

    fun makeSortedIndexMap(seq: Sequence<Key>): Map<Key, Int> {
        return makeIndexMap(seq)
            .toSortedMap(Comparator.comparing { it.toString() })
    }

    fun testScanLexesForTestWords(
        model: CoreModel,
        keyGetter: (Lex) -> Key,
        indexerByKey: (Sequence<Key>) -> Map<Key, Int>,
        testWords: Set<Lemma>,
        printTestWords: Boolean,
        ps: PrintStream,
    ) {
        // stream of lexes
        val lexKeySeq = model.lexes
            .asSequence()
            .onEach { lex: Lex ->
                if (testWords.contains(lex.lemma)) {
                    if (printTestWords) {
                        ps.println("@$lex")
                    }
                }
            }
            .map(keyGetter)

        // make lex-to-index map
        val lexKeyToIndex = indexerByKey.invoke(lexKeySeq)

        // test map
        ps.printf("%-12s %s%n", "index", "lex")
        for (word in testWords) {
            val lexes = model.lexResolver(word)
            for (lex in lexes) {
                ps.printf("%-12d %s%n", lexKeyToIndex[keyGetter.invoke(lex)], lex)
            }
        }
    }

    fun testWords(model: CoreModel, ps: PrintStream, vararg lemmas: Lemma) {
        for (lemma in lemmas) {
            val lexes = model.lexResolver(lemma)
            for (lex in lexes) {
                ps.println(lex)
                dumpKeys(lex, ps)
            }
        }
    }

    fun testWord(lemma: Lemma, model: CoreModel, ps: PrintStream) {
        val lexes = getLexes(model, lemma)
        for (lex in lexes) {
            ps.println(lex)
            dumpKeys(lex, ps)
        }
    }

    fun testWord(lemma: Lemma, posFilter: PartOfSpeech, model: CoreModel, ps: PrintStream) {
        val lexes = getLexesHavingPos(model, lemma, posFilter)!!
            .toList()
            .toTypedArray()
        for ((i, lex) in lexes.withIndex()) {
            ps.printf("[%d] %s%n", i + 1, lex)
            dumpKeys(lex, ps)
        }
        if (lexes.size > 1) {
            ps.println()
            ps.printf("comparing keys equals() for [%d] and [%d]%n", 1, 2)
            ps.println(lexes[0])
            ps.println(lexes[1])
            dumpKeyEquals(lexes[0], lexes[1], ps)
        }
    }

    private fun dumpKeyEquals(lex1: Lex, lex2: Lex, ps: PrintStream) {
        ps.printf("\t--- key = %s%n", Key.UsingPronunciation.of(lex1) == Key.UsingPronunciation.of(lex2))
        ps.printf("\tsha key = %s%n", Key.UsingDiscriminant.of(lex1) == Key.UsingDiscriminant.of(lex2))
        ps.printf("\tic  key = %s%n", Key.UsingDiscriminant.ofIgnoringCase(lex1) == Key.UsingDiscriminant.ofIgnoringCase(lex2))
        ps.printf("\tpos key = %s%n", ofUsingPartOfSpeech(lex1) == ofUsingPartOfSpeech(lex2))
        ps.printf("\tpwn key = %s%n", ofIgnoringCaseUsingPartOfSpeech(lex1) == ofIgnoringCaseUsingPartOfSpeech(lex2))
    }

    private fun dumpKeys(lex: Lex, ps: PrintStream) {
        ps.printf("\t--- key = %s%n", Key.UsingPronunciation.of(lex))
        ps.printf("\tsha key = %s%n", Key.UsingDiscriminant.of(lex))
        ps.printf("\tigc key = %s%n", Key.UsingPronunciation.ofIgnoringCase(lex))
        ps.printf("\tpos key = %s%n", ofUsingPartOfSpeech(lex))
        ps.printf("\tpwn key = %s%n", ofIgnoringCaseUsingPartOfSpeech(lex))
    }
}
