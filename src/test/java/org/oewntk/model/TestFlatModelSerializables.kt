/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.ser.`in`.LibTestsSerCommon
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
            whichLexes = model.lexes.asSequence().drop((1000..100000).random()).take(20),
            whichSynsets = model.synsets.asSequence().drop((1000..100000).random()).take(2)
        )
        println(y)
    }

    @Test
    fun testFlatSerializationOfEntries() {
        val y = model.toFlatSerializableOfLexEntries(
            whichEntries = model.lexEntries.drop((1000..100000).random()).take(20),
            whichSynsets = model.synsets.asSequence().drop((1000..100000).random()).take(2)
        )
        println(y)
    }

    @Test
    fun testOrig() {
        assertEquals(origInfo, modelInfo)
    }

    companion object {

        lateinit var origInfo: String

        lateinit var modelInfo: String

        lateinit var model: CoreModel

        @JvmStatic
        @BeforeClass
        fun init() {
            val orig: String = System.getProperty("INFO")!!
            origInfo = File(orig).readText()

            model = checkNotNull(LibTestsSerCommon.model)

            val info = model.info()
            val counts = ModelInfo.counts(model)
            modelInfo = "$info\n$counts"
            ps.println(modelInfo)
            ps.println()
        }
    }
}
