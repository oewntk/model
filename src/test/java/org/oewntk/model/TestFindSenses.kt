/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import junit.framework.TestCase.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.LibNanoModel.lemma1
import org.oewntk.model.LibNanoModel.lemma2
import org.oewntk.model.LibNanoModel.model
import org.oewntk.model.LibNanoModel.sense11
import org.oewntk.model.LibNanoModel.sense12
import org.oewntk.model.LibNanoModel.sense21
import org.oewntk.model.LibNanoModel.sense22
import org.oewntk.model.LibNanoModel.synset1
import org.oewntk.model.LibNanoModel.synset2

class TestFindSenses {

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

    private val lemma2Lexes = { lemma: Lemma -> model.lexesByLemma!![lemma]!! }

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
