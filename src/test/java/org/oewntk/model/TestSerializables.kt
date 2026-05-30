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
    fun testSense() {
        val sense = model.senseResolver("jest%1:10:00::")
        val yamlString = yaml.toYaml(sense)
        println(yamlString)
    }

    @Test
    fun testSenses() {
        val someSenses = arrayOf("force%1:07:00::", "force%1:07:01::", "force%1:19:00::")
            .map(model.senseResolver)
            .asSequence()
        val yamlString = yaml.sensesToYaml(someSenses)
        println(yamlString)
    }

    @Test
    fun testSynsets() {
        val someSynsets = arrayOf("05042508-n", "05201846-n", "11479041-n")
            .map(model.synsetResolver)
            .asSequence()
        val yamlString = yaml.synsetsToYaml(someSynsets)
        println(yamlString)
    }

    @Test
    fun testSomeLexesAsValues() {
        val someLexes = arrayOf("force", "lead", "row", "bow", "galore")
            .flatMap(model.lexResolver)
            .asSequence()
        val yamlString = yaml.lexValuesToYaml(someLexes, model.senseResolver)
        println(yamlString)
    }

    @Test
    fun testSomeLexesAsEntries() {
        val someLexes = arrayOf("force", "lead", "row", "bow", "galore")
            .flatMap(model.lexResolver)
            .asSequence()
        val yamlString = yaml.lexesToYaml(someLexes)
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
    fun test100RandomEntries() {
        val someEntries: Sequence<LexEntry> = model.lexEntries
            .drop((1000..100000).random())
            .take(100)
        val yamlString = yaml.entriesToYaml(someEntries, model.senseResolver)
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
    fun testModelResolution() {
        val l1 = model.lexFinder1("Californian", "n")
        println(l1)
        val l2 = model.lexFinder1("Californian", "a")
        println(l2)
    }

    @Test
    fun testFlatSerializationOfLexes() {
        val y = model.toFlatSerializableOfLexes(
            whichLexes = model.lexes.asSequence().drop((1000..100000).random()).take(2),
            whichSynsets = model.synsets.asSequence().drop((1000..100000).random()).take(2)
        )
        println(y)
    }

    @Test
    fun testFlatSerializationOfEntries() {
        val y = model.toFlatSerializableOfLexEntries(
            whichEntries = model.lexEntries.drop((1000..100000).random()).take(2),
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
