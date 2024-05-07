/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import junit.framework.TestCase.assertEquals
import org.junit.BeforeClass
import org.junit.Test

class TestFindSenses {

    private val domain1 = "communication"
    private val domain2 = "body"
    private val lexid1 = 0
    private val lexid2 = 0
    private val lemma1 = "jest"
    private val lemma2 = "joke"
    private val pos = 'v'
    private val ipa1 = "dʒɛst"
    private val pronunciation1 = Pronunciation(ipa1, null)
    private val ipa21 = "dʒəʊk"
    private val ipa22 = "dʒoʊk"
    private val pronunciation21 = Pronunciation(ipa21, "GB")
    private val pronunciation22 = Pronunciation(ipa22, "US")

    private val senseKey11 = "jest%2:32:00::"
    private val senseKey12 = "jest%2:29:00::"
    private val senseKey21 = "joke%2:32:00::"
    private val senseKey22 = "joke%2:29:00::"

    private val lex1 = Lex(lemma1, pos.toString(), null, mutableListOf(senseKey11, senseKey12)).apply { pronunciations = setOf(pronunciation1) }
    private val lex2 = Lex(lemma2, pos.toString(), null, mutableListOf(senseKey21, senseKey22)).apply { pronunciations = setOf(pronunciation21, pronunciation22) }

    private val synsetId1 = "00855315-v"
    private val synsetId2 = "00105308-v"

    private val synset1 = Synset(
        synsetId1,
        pos,
        domain2,
        arrayOf(lemma1, lemma2),
        arrayOf("tell a joke", "speak humorously")
    )
    private val synset2 = Synset(
        synsetId2,
        pos,
        domain2,
        arrayOf(lemma1, lemma2),
        arrayOf("act in a funny teasing way")
    )
    private val sense11 = Sense(senseKey11, lex1, pos, lexid1, synsetId1)
    private val sense12 = Sense(senseKey12, lex1, pos, lexid2, synsetId2)
    private val sense21 = Sense(senseKey21, lex2, pos, lexid1, synsetId1)
    private val sense22 = Sense(senseKey22, lex2, pos, lexid2, synsetId2)

    private val lexes = listOf(lex1, lex2)
    private val senses = listOf(sense11, sense12, sense21, sense22)
    private val synsets = listOf(synset1, synset2)

    val model = CoreModel(lexes, senses, synsets)

    @Test
    fun testInfo() {
        ps.println(model.info())
    }

    @Test
    fun testToString() {
        ps.println("synset1=$synset1")
        ps.println("synset2=$synset2")

        ps.println("sense11=$sense11")
        ps.println("sense12=$sense12")
        ps.println("sense21=$sense21")
        ps.println("sense22=$sense22")
    }

    private val lemma2Lexes = { lemma: LemmaType -> model.lexesByLemma!![lemma]!! }

    private val sensekey2sense = { sk: SenseKey -> model.sensesById!![sk]!! }

    @Test
    fun testFindSensesOfSynset() {
        val senses1 = synset1.findSenses(lemma2Lexes, sensekey2sense)
        ps.println("sensesOf $synset1 $senses1")
        assertEquals(setOf(sense11, sense21), senses1.toSet())

        val senses2 = synset2.findSenses(lemma2Lexes, sensekey2sense)
        assertEquals(setOf(sense12, sense22), senses2.toSet())
        ps.println("sensesOf $synset2 $senses2")
    }

    @Test
    fun testFindSensesOfLemmaInSynset() {
        val result11 = synset1.findSenseOf(lemma1, lemma2Lexes, sensekey2sense)
        ps.println("senseOf $lemma1 in $synset1 is $result11")
        assertEquals(sense11, result11)

        val result21 = synset1.findSenseOf(lemma2, lemma2Lexes, sensekey2sense)
        ps.println("senseOf $lemma2 in $synset1 is $result21")
        assertEquals(sense21, result21)

        val result12 = synset2.findSenseOf(lemma1, lemma2Lexes, sensekey2sense)
        ps.println("senseOf $lemma1 in $synset2 is $result12")
        assertEquals(sense12, result12)

        val result22 = synset2.findSenseOf(lemma2, lemma2Lexes, sensekey2sense)
        ps.println("senseOf $lemma2 in $synset2 is $result22")
        assertEquals(sense22, result22)
    }

    companion object {

        private val ps = if (!System.getProperties().containsKey("SILENT")) Tracing.psInfo else Tracing.psNull

        @Suppress("EmptyMethod")
        @JvmStatic
        @BeforeClass
        fun init() {
        }
    }
}
