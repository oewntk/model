/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import junit.framework.TestCase.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.LibDummyNanoModel.LEMMA1
import org.oewntk.model.LibDummyNanoModel.LEMMA2
import org.oewntk.model.LibDummyNanoModel.model
import org.oewntk.model.LibDummyNanoModel.sense11
import org.oewntk.model.LibDummyNanoModel.sense12
import org.oewntk.model.LibDummyNanoModel.sense21
import org.oewntk.model.LibDummyNanoModel.sense22
import org.oewntk.model.LibDummyNanoModel.synset1
import org.oewntk.model.LibDummyNanoModel.synset2
import java.io.PrintStream

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

    @Test
    fun testFindSensesOfSynset() {
        val senses1 = synset1.findSenses(model.lexResolver,  model.senseResolver)
        ps.println("sensesOf $synset1 $senses1")
        assertEquals(setOf(sense11, sense21), senses1.toSet())

        val senses2 = synset2.findSenses(model.lexResolver,  model.senseResolver)
        assertEquals(setOf(sense12, sense22), senses2.toSet())
        ps.println("sensesOf $synset2 $senses2")
    }

    @Test
    fun testFindSensesOfLemmaInSynset() {
        val result11 = synset1.resolveSenseOf(LEMMA1, model.lexResolver,  model.senseResolver)
        ps.println("senseOf $LEMMA1 in $synset1 is $result11")
        assertEquals(sense11, result11)

        val result21 = synset1.resolveSenseOf(LEMMA2, model.lexResolver,  model.senseResolver)
        ps.println("senseOf $LEMMA2 in $synset1 is $result21")
        assertEquals(sense21, result21)

        val result12 = synset2.resolveSenseOf(LEMMA1, model.lexResolver,  model.senseResolver)
        ps.println("senseOf $LEMMA1 in $synset2 is $result12")
        assertEquals(sense12, result12)

        val result22 = synset2.resolveSenseOf(LEMMA2, model.lexResolver,  model.senseResolver)
        ps.println("senseOf $LEMMA2 in $synset2 is $result22")
        assertEquals(sense22, result22)
    }

    companion object {

        private val silent = if (System.getProperties().containsKey("VERBOSE")) false
        else if (System.getProperties().containsKey("SILENT")) true
        else true

        private val ps: PrintStream = if (!silent) Tracing.psInfo else Tracing.psNull

        @Suppress("EmptyMethod")
        @JvmStatic
        @BeforeClass
        fun init() {
        }
    }
}
