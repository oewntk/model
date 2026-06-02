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

class TestSerializeModel {

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
             ps.println(sdata)
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
