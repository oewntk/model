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

class TestModelSerializables {

    val yaml = ToYaml(options = compatDumperOptions)

    @Test
    fun testModel() {
        model.lexes.groupBy(Lex::lemma)
            .mapValues { (_: Lemma, lexes: Collection<Lex>) ->
                val group = lexes.groupBy(Lex::key2)
                group.values.forEach {
                    assertEquals(1, it.size, it.toString())
                }
                group
            }
    }

    @Test
    fun testModelResolution() {
        val l1 = model.lexFinder1("Californian", "n")
        println(l1)
        val l2 = model.lexFinder1("Californian", "a")
        println(l2)
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
