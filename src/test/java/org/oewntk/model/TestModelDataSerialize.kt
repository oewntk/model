/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.LibModelSubset.lexSubset
import org.oewntk.model.LibModelSubset.senseSubset
import org.oewntk.model.LibModelSubset.synsetSubset
import org.oewntk.ser.`in`.LibTestsSerCommon.checkOrig
import org.oewntk.ser.`in`.LibTestsSerCommon.model
import org.oewntk.ser.`in`.LibTestsSerCommon.ps

class TestModelDataSerialize {

    @Test
    fun testModelSerialization() {
        val y = model.dataSerialize(
            whichLexes = model.lexSubset(howMany = 5),
            whichSynsets = model.synsetSubset(howMany = 5),
            whichSenses = model.senseSubset(howMany = 5)
        )
        ps.println(y)
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
