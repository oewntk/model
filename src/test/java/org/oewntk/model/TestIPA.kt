/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.oewntk.model.PronunciationValueImpl.Companion.ipaRegex
import org.oewntk.model.PronunciationValueImpl.Companion.isIPA

class TestIPA {

    private val list = listOf(
        "ˈkæbɪdʒ"
    )

    @Test
    fun testIPAMatch0000() {
        assertTrue(ipaRegex.matches("kbdʒ"))
    }

    @Test
    fun testIPAMatch000() {
        assertTrue(ipaRegex.matches("æɪ"))
    }

    @Test
    fun testIPAMatch00() {
        assertTrue(ipaRegex.matches("kæbɪdʒ"))
    }

    @Test
    fun testIPAMatch0() {
        assertTrue(ipaRegex.matches("ˈkæbɪdʒ"))
    }

    @Test
    fun testIPAMatch1() {
        assertTrue("ˈkæbɪdʒ".isIPA())
    }

    @Test
    fun testIPAMatch() {
        list.forEach {
            assertTrue(it.isIPA())
        }
    }
}
