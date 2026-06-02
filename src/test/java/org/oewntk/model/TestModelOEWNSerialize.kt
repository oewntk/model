/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.LibTestGen.genModelSerializables
import org.oewntk.ser.`in`.LibTestsSerCommon.checkOrig
import org.oewntk.ser.`in`.LibTestsSerCommon.model
import org.oewntk.ser.`in`.LibTestsSerCommon.ps

class TestModelOEWNSerialize {

    @Test
    fun testModelSerialization() {
        val serialized: Sequence<Pair<Map<String, Any>, Filename>> = genModelSerializables(model)
        serialized.forEach { (data: Map<String, Any>, _: Filename) ->
             ps.println(data)
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
