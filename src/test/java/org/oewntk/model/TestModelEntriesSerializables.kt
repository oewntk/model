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

class TestModelEntriesSerializables {

    val yaml = ToYaml(options = compatDumperOptions)

    @Test
    fun test100RandomEntries() {
        val someEntries: Sequence<LexEntry> = TestSerializables.model.lexEntries
            .drop((1000..100000).random())
            .take(100)
        val yamlString = yaml.entriesToYaml(someEntries, TestSerializables.model.senseResolver)
        println(yamlString)
    }

    @Test
    fun testSomeEntries() {
        val someEntries: Sequence<LexEntry> = arrayOf("force", "lead", "row", "bow", "galore")
            .asSequence()
            .map { it to TestSerializables.model.lexResolver(it) }
            .map { AbstractMap.SimpleEntry(it.first, it.second) }
        val yamlString = yaml.entriesToYaml(someEntries, TestSerializables.model.senseResolver)
        println(yamlString)
    }

    @Test
    fun testSomePairEntries() {
        val someEntries: Sequence<LexEntry> = arrayOf("force", "lead", "row", "bow", "galore")
            .asSequence()
            .map { AbstractMap.SimpleEntry(it, TestSerializables.model.lexResolver(it)) }
        val yamlString = yaml.entriesToYaml(someEntries, TestSerializables.model.senseResolver)
        println(yamlString)
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
