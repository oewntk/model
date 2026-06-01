/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.LibModelSubset.lexSubset
import org.oewntk.model.LibModelSubset.synsetSubset
import org.oewntk.ser.`in`.LibTestsSerCommon.checkOrig
import org.oewntk.ser.`in`.LibTestsSerCommon.model
import org.oewntk.ser.`in`.LibTestsSerCommon.ps
import org.oewntk.yaml.out.ToYaml
import org.oewntk.yaml.out.YamlDump.Companion.compatDumperOptions
import java.io.File
import kotlin.test.assertEquals

class TestModelSerializables {

    val yaml = ToYaml(options = compatDumperOptions)

    private fun genSmallSerializable(model: CoreModel): Sequence<Pair<SData, Filename>> {

        return sequence {
            val someSerializedLexes = model.lexSubset()
                .toSerializable(model.senseResolver)
            yield(someSerializedLexes to "entries-some") // yield content with source file base

            val someSerializedSynsets = model.synsetSubset()
                .toSerializable()
            yield(someSerializedSynsets to "data-some")  // yield content with source file base
        }
    }

    @Test
    fun testModelOneSerialization() {
        val serialized: Sequence<Pair<SData, Filename>> = genSmallSerializable(model)
        serialized.forEach { (sdata: SData, _: Filename) ->
            val yamlString = yaml.dump(sdata)
            ps.println(yamlString)
        }
    }

    @Test
    fun testOrig() {
        checkOrig()
    }

    companion object {

        @JvmStatic
        @BeforeClass
        fun init() {
            model //eager
        }
    }
}
