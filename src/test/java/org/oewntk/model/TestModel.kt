/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.ser.`in`.LibTestsSerCommon.model
import org.oewntk.ser.`in`.LibTestsSerCommon.ps
import java.io.File
import kotlin.test.assertEquals

class TestModel {

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
        ps.println(l1)
        val l2 = model.lexFinder1("Californian", "a")
        ps.println(l2)
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
