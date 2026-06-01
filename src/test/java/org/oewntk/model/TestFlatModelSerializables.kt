/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.LibModelSubset.lexSubset
import org.oewntk.model.LibModelSubset.lexEntrySubset
import org.oewntk.model.LibModelSubset.synsetSubset
import org.oewntk.ser.`in`.LibTestsSerCommon.model
import org.oewntk.ser.`in`.LibTestsSerCommon.ps
import org.oewntk.yaml.out.ToYaml
import org.oewntk.yaml.out.YamlDump.Companion.compatDumperOptions
import java.io.File
import java.util.*
import kotlin.test.assertEquals

class TestFlatModelSerializables {

    val yaml = ToYaml(options = compatDumperOptions)

    @Test
    fun testFlatSerializationOfLexes() {
        val y = model.toFlatSerializableOfLexes(
            whichLexes = model.lexSubset(),
            whichSynsets = model.synsetSubset()
        )
        ps.println(y)
    }

    @Test
    fun testFlatSerializationOfEntries() {
        val y = model.toFlatSerializableOfLexEntries(
            whichEntries = model.lexEntrySubset(howMany = 20),
            whichSynsets = model.synsetSubset(howMany = 2)
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
