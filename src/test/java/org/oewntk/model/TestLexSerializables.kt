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
import kotlin.test.assertEquals

class TestLexSerializables {

    val yaml = ToYaml(options = compatDumperOptions)

    @Test
    fun test100RandomEntries() {
        val someLexes: Sequence<Lex> = TestSerializables.model.lexes.asSequence()
            .drop((1000..100000).random())
            .take(100)
        val yamlString = yaml.lexesToYaml(someLexes)
        println(yamlString)
    }

    @Test
    fun testModel() {
        TestModelSerializables.model.lexes.groupBy(Lex::lemma)
            .mapValues { (_: Lemma, lexes: Collection<Lex>) ->
                val group = lexes.groupBy(Lex::key2)
                group.values.forEach {
                    assertEquals(1, it.size, it.toString())
                }
                group
            }
    }

    @Test
    fun testSomeLexesAsValues() {
        val someLexes: Sequence<Lex> = arrayOf("force", "lead", "row", "bow", "galore")
            .flatMap(TestSerializables.model.lexResolver)
            .asSequence()
        val yamlString = yaml.lexesToYaml(someLexes)
        println(yamlString)
    }

    @Test
    fun testSomeLexesAsEntries() {
        val someLexes: Sequence<Lex> = arrayOf("force", "lead", "row", "bow", "galore")
            .flatMap(TestSerializables.model.lexResolver)
            .asSequence()
        val yamlString = yaml.lexesToYaml(someLexes)
        println(yamlString)
    }


    @Test
    fun testSerializationOfLexes() {
        val someLexes: Sequence<Lex> = model.lexes.asSequence().drop((1000..100000).random()).take(20)
        val yamlString = yaml.lexesToYaml(someLexes)
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
