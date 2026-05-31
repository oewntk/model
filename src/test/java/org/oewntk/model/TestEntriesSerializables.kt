/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.ser.`in`.LibTestsSerCommon.model
import org.oewntk.ser.`in`.LibTestsSerCommon.ps
import org.oewntk.yaml.out.ToYaml
import org.oewntk.yaml.out.YamlDump.Companion.compatDumperOptions
import java.io.File
import java.util.*
import kotlin.test.assertEquals

class TestEntriesSerializables {

    val yaml = ToYaml(options = compatDumperOptions)

    @Test
    fun test100RandomEntries() {
        val someEntries: Sequence<LexEntry> = model.lexEntries
            .drop((1000..100000).random())
            .take(100)
        val yamlString = yaml.entriesToYaml(someEntries, model.senseResolver)
        println(yamlString)
    }

    @Test
    fun testSomeEntries() {
        val someEntries: Sequence<LexEntry> = arrayOf("force", "lead", "row", "bow", "galore")
            .asSequence()
            .map { it to model.lexResolver(it) }
            .map { AbstractMap.SimpleEntry(it.first, it.second) }
        val yamlString = yaml.entriesToYaml(someEntries, model.senseResolver)
        println(yamlString)
    }

    @Test
    fun testSomePairEntries() {
        val someEntries: Sequence<LexEntry> = arrayOf("force", "lead", "row", "bow", "galore")
            .asSequence()
            .map { AbstractMap.SimpleEntry(it, model.lexResolver(it)) }
        val yamlString = yaml.entriesToYaml(someEntries, model.senseResolver)
        println(yamlString)
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
