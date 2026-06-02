/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.Lex.Groups.lexByLemmaThenByKey2
import org.oewntk.model.LibModelSubset.lexSubset
import org.oewntk.ser.`in`.LibTestsSerCommon.checkOrig
import org.oewntk.ser.`in`.LibTestsSerCommon.model
import org.oewntk.ser.`in`.LibTestsSerCommon.ps

class TestSerializableLexes {

    @Test
    fun testRandomLexes() {
        val someLexes: Sequence<Lex> = model.lexSubset()
        val serializables = someLexes.map { it.toSerializable(model.senseResolver) }
        ps.println(serializables.joinToString(separator = "\n\n"))
    }

    @Test
    fun testSomeLexes() {
        val someLexes: Sequence<Lex> = arrayOf("force", "lead", "row", "bow", "galore")
            .flatMap(model.lexResolver)
            .asSequence()
        val serializables = someLexes.map { it.toSerializable(model.senseResolver) }
        ps.println(serializables.joinToString(separator = "\n\n"))
    }

    @Test
    fun testSomeLexes2() {
        val someLexes: Sequence<Lex> = arrayOf("force", "lead", "row", "bow", "galore")
            .flatMap(model.lexResolver)
            .asSequence()
        val serializables = someLexes.map { it.toSerializable(model.senseResolver) }
        ps.println(serializables.joinToString(separator = "\n\n"))
    }

    @Test
    fun testSomeLexesByLemmaThenByKey2() {
        val someLexes: Sequence<Lex> = model.lexSubset(howMany = 5)
        val map: HyperMap1 = someLexes.lexByLemmaThenByKey2()
        val serializedMap: Map<Lemma, Any> = map.toSerializable(model.senseResolver)
        ps.println(serializedMap)
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
