/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.LibModelSubset.lexSubset
import org.oewntk.ser.`in`.LibTestsSerCommon.checkOrig
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
        val yamlString = yaml.lexesToYaml(someLexes, model.senseResolver).joinToString(separator = "\n\n")
        ps.println(yamlString)
    }

    @Test
    fun testSomeLexes() {
        val someLexes: Sequence<Lex> = arrayOf("force", "lead", "row", "bow", "galore")
            .flatMap(model.lexResolver)
            .asSequence()
        val yamlString = yaml.lexesToYaml(someLexes, model.senseResolver).joinToString(separator = "\n\n")
        ps.println(yamlString)
    }

    @Test
    fun testSerializationOfLexes() {
        val someLexes: Sequence<Lex> = model.lexSubset()
        val yamlString = yaml.lexesToYaml(someLexes, model.senseResolver).joinToString(separator = "\n\n")
        ps.println(yamlString)
    }

    @Test
    fun testOrig() {
        checkOrig()
    }

    companion object {

        @JvmStatic
        @BeforeClass
        fun init() {
            model
        }
    }
}
