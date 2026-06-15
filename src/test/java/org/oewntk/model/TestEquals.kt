/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertArrayEquals
import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.LibDummyNanoModel.lex1
import org.oewntk.model.LibDummyNanoModel.lexes1
import org.oewntk.model.LibDummyNanoModel.lexes2
import org.oewntk.model.LibDummyNanoModel.model1
import org.oewntk.model.LibDummyNanoModel.model2
import org.oewntk.model.LibDummyNanoModel.model3
import org.oewntk.model.LibDummyNanoModel.pronunciation1
import org.oewntk.model.LibDummyNanoModel.pronunciation21
import org.oewntk.model.LibDummyNanoModel.pronunciation22
import org.oewntk.model.LibDummyNanoModel.pronunciation3
import org.oewntk.model.LibDummyNanoModel.sense11
import org.oewntk.model.LibDummyNanoModel.senses1
import org.oewntk.model.LibDummyNanoModel.senses2
import org.oewntk.model.LibDummyNanoModel.synset1
import org.oewntk.model.LibDummyNanoModel.synsets1
import org.oewntk.model.LibDummyNanoModel.synsets2
import org.oewntk.model.LibDummyNanoModelGenerator.genLeDiffWithDiscriminant
import org.oewntk.model.LibDummyNanoModelGenerator.genLeDiffWithKey2
import org.oewntk.model.LibDummyNanoModelGenerator.genLeDiffWithLemma
import org.oewntk.model.LibDummyNanoModelGenerator.genLeDiffWithPronunciations
import org.oewntk.model.LibDummyNanoModelGenerator.genLeDiffWithSenseKeys
import org.oewntk.model.LibDummyNanoModelGenerator.genLeDiffWithType
import org.oewntk.model.LibDummyNanoModelGenerator.genLexEqual
import org.oewntk.model.LibDummyNanoModelGenerator.genSenseDiffWithAdjPosition
import org.oewntk.model.LibDummyNanoModelGenerator.genSenseDiffWithId
import org.oewntk.model.LibDummyNanoModelGenerator.genSenseDiffWithIndexInLex
import org.oewntk.model.LibDummyNanoModelGenerator.genSenseDiffWithLexId
import org.oewntk.model.LibDummyNanoModelGenerator.genSenseDiffWithSynsetId
import org.oewntk.model.LibDummyNanoModelGenerator.genSenseDiffWithVerbFrames
import org.oewntk.model.LibDummyNanoModelGenerator.genSenseDiffWithVerbTemplates
import org.oewntk.model.LibDummyNanoModelGenerator.genSenseEqual
import org.oewntk.model.LibDummyNanoModelGenerator.genSynsetDiffWithDefinition
import org.oewntk.model.LibDummyNanoModelGenerator.genSynsetDiffWithDomain
import org.oewntk.model.LibDummyNanoModelGenerator.genSynsetDiffWithExamples
import org.oewntk.model.LibDummyNanoModelGenerator.genSynsetDiffWithId
import org.oewntk.model.LibDummyNanoModelGenerator.genSynsetDiffWithMembers
import org.oewntk.model.LibDummyNanoModelGenerator.genSynsetDiffWithType
import org.oewntk.model.LibDummyNanoModelGenerator.genSynsetEqual
import java.io.PrintStream
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TestEquals {

    @Test
    fun testLexId() {
        val obj1 = LexId("jest", SynsetType.V, null)
        val obj2 = LexId("jest", SynsetType.V, null)
        assertTrue(Objects.equals(obj1, obj2))
        assertEquals(obj1, obj2)
    }

    @Test
    fun testPronunciation() {
        assertNotEquals(pronunciation1, pronunciation3)
        assertNotEquals(pronunciation21, pronunciation22)
    }

    @Test
    fun testSynset() {
        assertEquals(synset1, synset1)

        genSynsetEqual().let {
            val (it1, it2) = it
            assertEquals(it1.key, it2.key)
            assertEquals(it1.type, it2.type)
            assertEquals(it1.domain, it2.domain)
            assertEquals(it1.members, it2.members)
            assertEquals(it1.definitions, it2.definitions)
            assertEquals(it1.examples, it2.examples)
            assertEquals(it1.usages, it2.usages)
            assertEquals(it1.relations, it2.relations)
            assertEquals(it1.ili, it2.ili)
            assertEquals(it1.wikidata, it2.wikidata)
            assertEquals(it1.source, it2.source)
            assertArrayEquals(it1.value, it2.value)
            assertEquals(it1, it2)
        }

        genSynsetDiffWithId().let { assertNotEquals(it.first, it.second) }
        genSynsetDiffWithType().let { assertNotEquals(it.first, it.second) }
        genSynsetDiffWithDomain().let { assertNotEquals(it.first, it.second) }
        genSynsetDiffWithMembers().let { assertNotEquals(it.first, it.second) }
        genSynsetDiffWithDefinition().let { assertNotEquals(it.first, it.second) }
        genSynsetDiffWithExamples().let { assertNotEquals(it.first, it.second) }
    }

    @Test
    fun testSense() {
        assertEquals(sense11, sense11)

        genSenseEqual().let {
            val (it1, it2) = it
            assertEquals(it1.key, it2.key)
            assertEquals(it1.value, it2.value)
            assertEquals(it1.type, it2.type)
            assertEquals(it1.indexInLex, it2.indexInLex)
            assertEquals(it1.verbFrames, it2.verbFrames)
            assertEquals(it1.verbTemplates, it2.verbTemplates)
            assertEquals(it1.adjPosition, it2.adjPosition)
            assertEquals(it1.examples, it2.examples)
            assertEquals(it1.tagCount, it2.tagCount)
            assertEquals(it1.relations, it2.relations)
            assertArrayEquals(it1.properties, it2.properties)
            assertEquals(it1, it2)
        }

        genSenseDiffWithId().let { assertNotEquals(it.first, it.second) }
        genSenseDiffWithLexId().let { assertNotEquals(it.first, it.second) }
        genSenseDiffWithSynsetId().let { assertNotEquals(it.first, it.second) }
        genSenseDiffWithIndexInLex().let { assertNotEquals(it.first, it.second) }
        genSenseDiffWithAdjPosition().let { assertNotEquals(it.first, it.second) }
        genSenseDiffWithVerbFrames().let { assertNotEquals(it.first, it.second) }
        genSenseDiffWithVerbTemplates().let { assertNotEquals(it.first, it.second) }
    }

    @Test
    fun testLex() {
        assertEquals(lex1, lex1)

        genLexEqual().let {
            val (it1, it2) = it
            assertEquals(it1.key, it2.key)
            assertEquals(it1.value, it2.value)
            assertEquals(it1.type, it2.type)
            assertEquals(it1.senseKeys, it2.senseKeys)
            assertEquals(it1.forms, it2.forms)
            assertEquals(it1.pronunciations, it2.pronunciations)
            assertArrayEquals(it1.properties, it2.properties)
            assertEquals(it1, it2)
        }

        genLeDiffWithLemma().let { assertNotEquals(it.first, it.second) }
        genLeDiffWithType().let { assertNotEquals(it.first, it.second) }
        genLeDiffWithDiscriminant().let { assertNotEquals(it.first, it.second) }
        genLeDiffWithKey2().let { assertNotEquals(it.first, it.second) }
        genLeDiffWithSenseKeys().let { assertNotEquals(it.first, it.second) }
        genLeDiffWithPronunciations().let { assertNotEquals(it.first, it.second) }
    }

    @Test
    fun testLexes() {
        assertTrue(lexes1 == lexes2)
        assertTrue(lexes1.toSet() == lexes2.toSet())
    }

    @Test
    fun testSenses() {
        assertTrue(senses1 == senses2)
        assertTrue(senses1.toSet() == senses2.toSet())
    }

    @Test
    fun testSynsets() {
        assertTrue(synsets1 == synsets2)
        assertTrue(synsets1.toSet() == synsets2.toSet())
    }

    @Test
    fun testModels() {
        assertFalse(model1 === model2)
        //assertTrue(model1 == model2)
        //assertEquals(model1, model2)

        assertFalse(model1 === model3)
        //assertNotEquals(model1, model3)
        //assertFalse(model1 == model3)
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
