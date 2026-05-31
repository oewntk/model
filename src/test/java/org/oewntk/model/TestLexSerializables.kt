/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.LibModelSubset.lexSubset
import org.oewntk.ser.`in`.LibTestsSerCommon.model
import org.oewntk.ser.`in`.LibTestsSerCommon.ps
import org.oewntk.yaml.out.ToYaml
import org.oewntk.yaml.out.YamlDump.Companion.compatDumperOptions
import java.io.File
import kotlin.test.assertEquals

class TestLexSerializables {

    val yaml = ToYaml(options = compatDumperOptions)

    @Test
    fun testRandomLexes() {
        val someLexes: Sequence<Lex> = model.lexSubset()
        val yamlString = yaml.lexesToYaml(someLexes)
        println(yamlString)
    }

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
    fun testSomeLexesAsValues() {
        val someLexes: Sequence<Lex> = arrayOf("force", "lead", "row", "bow", "galore")
            .flatMap(model.lexResolver)
            .asSequence()
        val yamlString = yaml.lexesToYaml(someLexes)
        println(yamlString)
    }

    @Test
    fun testSomeLexesAsEntries() {
        val someLexes: Sequence<Lex> = arrayOf("force", "lead", "row", "bow", "galore")
            .flatMap(model.lexResolver)
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
