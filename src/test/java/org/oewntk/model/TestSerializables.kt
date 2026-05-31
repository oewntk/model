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

class TestSerializables {

    val yaml = ToYaml(options = compatDumperOptions)

    @Test
    fun testDummyLex() {
        val lex = Lex("jest", "n", listOf("jest%1:10:00::", "jest%1:04:00::"))
        val yamlString = yaml.toYaml(lex, model.senseResolver)
        println(yamlString)
    }

    @Test
    fun testDummySynset() {
        val synset = Synset(
            "77777777-n",
            SynsetType.N,
            "domain",
            arrayOf("member1", "member2"),
            arrayOf("definition", "definition2"),
        )
        val yamlString = yaml.toYaml(synset)
        println(yamlString)
    }

    @Test
    fun testLex() {
        val lex: Lex = model.lexResolver1("jest", "n")
        val yamlString = yaml.toYaml(lex, model.senseResolver)
        println(yamlString)
    }

    @Test
    fun testSense() {
        val sense: Sense = model.senseResolver("jest%1:10:00::")
        val yamlString = yaml.toYaml(sense)
        println(yamlString)
    }

    @Test
    fun testSenses() {
        val someSenses: Sequence<Sense> = arrayOf("force%1:07:00::", "force%1:07:01::", "force%1:19:00::")
            .map(model.senseResolver)
            .asSequence()
        val yamlString = yaml.sensesToYaml(someSenses)
        println(yamlString)
    }

    @Test
    fun testSynset() {
        val synset: Synset = model.synsetResolver("05042508-n")
        val yamlString = yaml.toYaml(synset)
        println(yamlString)
    }

    @Test
    fun testSynsets() {
        val someSynsets: Sequence<Synset> = arrayOf("05042508-n", "05201846-n", "11479041-n")
            .map(model.synsetResolver)
            .asSequence()
        val yamlString = yaml.synsetsToYaml(someSynsets)
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
