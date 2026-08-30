/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import junit.framework.TestCase.assertTrue
import org.junit.Test

class TestIPA {

    private val list = listOf(
        "ˈkæbɪdʒ"
    )

    //private val IPA_CHARS_RE = """\u0250-\u02AF\u1D00-\u1D7F\u1D80-\u1DFC\u0300-\u036F\u0061-\u007A\u0041-\u005A"""
    private val IPA_CHARS_RE = """\u0250-\u02AF\u02B0-\u02FF\u1D00-\u1D7F\u1D80-\u1DFC\u0300-\u036F\u00C0-\u00FF\u0061-\u007A\u0041-\u005A"""

    private val IPA_RE = "[$IPA_CHARS_RE]+"

    private val ipaRegex = "^$IPA_RE$".toRegex()


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
