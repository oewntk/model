/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.LibModelSubset.lexSubset
import org.oewntk.model.LibModelSubset.senseSubset
import org.oewntk.model.LibModelSubset.synsetSubset
import org.oewntk.ser.`in`.LibTestsSerCommon.checkOrig
import org.oewntk.ser.`in`.LibTestsSerCommon.model
import org.oewntk.ser.`in`.LibTestsSerCommon.ps

class TestDataSerializables {

    @Test
    fun testRawLexForce() {
        val lex = model.lexResolver1("force", "n")
        val y = lex.lexAsDataSerialize()
        ps.println(y)
    }

    @Test
    fun testRawSynsetForce() {
        val synset = model.synsetResolver("05201846-n")
        val y = synset.synsetAsDataSerialize()
        ps.println(y)
    }

    @Test
    fun testSenseForce() {
        val sense = model.senseResolver("force%1:07:01::")
        val y = sense.senseAsDataSerialize()
        ps.println(y)
    }

    @Test
    fun testLexesForce() {
        val y = model.lexResolver("force").asSequence().lexesDataSerialize()
        ps.println(y)
    }

    @Test
    fun testLexesLead() {
        val y = model.lexResolver("lead").asSequence().lexesDataSerialize()
        ps.println(y)
    }

    @Test
    fun testLexesBow() {
        val y = model.lexResolver("bow").asSequence().lexesDataSerialize()
        ps.println(y)
    }

    @Test
    fun testLexesRow() {
        val y = model.lexResolver("row").asSequence().lexesDataSerialize()
        ps.println(y)
    }

    @Test
    fun testSensesForce() {
        val y = sequenceOf(
            "force%1:07:01::",
            "force%1:19:00::",
            "force%1:07:00::",
            "force%1:14:00::",
            "force%1:14:01::",
            "force%1:04:01::",
            "force%1:18:00::",
            "force%1:14:02::",
            "force%1:07:02::",
            "force%1:04:00::"
        )
            .map { model.senseResolver(it) }
            .sensesDataSerialize()
        ps.println(y)
    }

    @Test
    fun testLexes() {
        val y = model.lexSubset(howMany = 5).lexesDataSerialize()
        ps.println(y)
    }

    @Test
    fun testSynsets() {
        val y = model.synsetSubset(howMany = 5).synsetsDataSerialize()
        ps.println(y)
    }

    @Test
    fun testSenses() {
        val y = model.senseSubset(howMany = 5).sensesDataSerialize()
        ps.println(y)
    }

    @Test
    fun testOrig() {
        checkOrig()
    }

    companion object {

        @JvmStatic
        @BeforeClass
        fun init() {
            model
        }
    }
}
