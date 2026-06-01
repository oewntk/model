/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.LibModelSubset.lexSubset
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
        val y = model.lexFinder("force")?.asSequence()?.lexAsDataSerialize()
        ps.println(y)
    }

    @Test
    fun testRawLexesLead() {
        val y = model.lexFinder("lead")?.asSequence()?.lexAsDataSerialize()
        ps.println(y)
    }

    @Test
    fun testRawLexesBow() {
        val y = model.lexFinder("bow")?.asSequence()?.lexAsDataSerialize()
        ps.println(y)
    }

    @Test
    fun testRawLexesRow() {
        val y = model.lexFinder("row")?.asSequence()?.lexAsDataSerialize()
        ps.println(y)
    }

    @Test
    fun testRawLexes() {
        val y = model.lexSubset(howMany = 5).lexAsDataSerialize()
        ps.println(y)
    }

    @Test
    fun testRawLSynsets() {
        val y = model.synsetSubset(howMany = 5).dataSerialize()
        ps.println(y)
    }

    @Test
    fun testRawModel() {
        val y = model.dataSerialize(
            whichLexes = model.lexSubset(howMany = 5),
            whichSynsets = model.synsetSubset(howMany = 5)
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
