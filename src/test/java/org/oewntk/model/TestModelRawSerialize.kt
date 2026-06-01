/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.LibModelSubset.lexSubset
import org.oewntk.model.LibModelSubset.senseSubset
import org.oewntk.model.LibModelSubset.synsetSubset
import org.oewntk.ser.`in`.LibTestsSerCommon.model
import org.oewntk.ser.`in`.LibTestsSerCommon.ps
import org.oewntk.yaml.out.ToYaml
import org.oewntk.yaml.out.YamlDump.Companion.compatDumperOptions
import java.io.File
import kotlin.test.assertEquals

class TestModelRawSerialize {

    val yaml = ToYaml(options = compatDumperOptions)

    @Test
    fun testRawLexesForce() {
        val y = model.lexResolver("force").asSequence().lexesAsDataSerialize()
        ps.println(y)
    }

    @Test
    fun testRawLexesLead() {
        val y = model.lexResolver("lead").asSequence().lexesAsDataSerialize()
        ps.println(y)
    }

    @Test
    fun testRawLexesBow() {
        val y = model.lexResolver("bow").asSequence().lexesAsDataSerialize()
        ps.println(y)
    }

    @Test
    fun testRawLexesRow() {
        val y = model.lexResolver("row").asSequence().lexesAsDataSerialize()
        ps.println(y)
    }

    @Test
    fun testRawSensesForce() {
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
            .sensesAsDataSerialize()
        ps.println(y)
    }

    @Test
    fun testRawLexes() {
        val y = model.lexSubset(howMany = 5).lexesAsDataSerialize()
        ps.println(y)
    }

    @Test
    fun testRawSynsets() {
        val y = model.synsetSubset(howMany = 5).synsetsAsDataSerialize()
        ps.println(y)
    }

    @Test
    fun testRawSenses() {
        val y = model.senseSubset(howMany = 5).sensesAsDataSerialize()
        ps.println(y)
    }

    @Test
    fun testRawModel() {
        val y = model.dataSerialize(
            whichLexes = model.lexSubset(howMany = 5),
            whichSynsets = model.synsetSubset(howMany = 5),
            whichSenses = model.senseSubset(howMany = 5)
        )
        ps.println(y)
    }

    @Test
    fun testOrig() {
        val orig: String = System.getProperty("INFO")!!
        val origInfo = File(orig).readText()
        val info = model.info()
        val counts = ModelInfo.counts(model)
        val modelInfo = "$info\n$counts"
        ps.println(modelInfo)
        assertEquals(origInfo, modelInfo)
    }

    companion object {

        @JvmStatic
        @BeforeClass
        fun init() {
            model
        }
    }
}
