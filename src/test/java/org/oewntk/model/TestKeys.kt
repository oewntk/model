/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.junit.Assert.*
import org.junit.Test
import kotlin.test.assertContentEquals

class TestKeys {

    @Test
    fun testRow() {
        val wordRow = "row"
        val pRowOu = Pronunciation("ɹəʊ", null)
        val pRowAu = Pronunciation("ɹaʊ", null)

        val lexRowOu = Lex(wordRow, "n-1", "source1").apply { pronunciations = setOf(pRowOu) }
        val lexRowAu = Lex(wordRow, "n-2", "source2").apply { pronunciations = setOf(pRowAu) }
        val lexRowOuN = Lex(wordRow, "n", "source1").apply { pronunciations = setOf(pRowOu) }
        val lexRowAuN = Lex(wordRow, "n", "source2").apply { pronunciations = setOf(pRowAu) }

        assertNotEquals(Key.W_P_A.of_t(lexRowOu), Key.W_P_A.of_t(lexRowAu))
        assertNotEquals(Key.W_P_D.of_t(lexRowOu), Key.W_P_D.of_t(lexRowAu)) // because discriminant is different
        assertEquals(Key.W_P_D.of_t(lexRowOuN), Key.W_P_D.of_t(lexRowAuN)) // because discriminant is same
        assertEquals(
            Key.W_P.of(lexRowOu, Lex::lemma, Lex::type),
            Key.W_P.of(lexRowAu, Lex::lemma, Lex::type)
        )
    }

    @Test
    fun testEquals() {
        val wordMobile = "mobile"
        val pMobile = Pronunciation("ˈməʊbaɪl", null)
        val pMobileGB = Pronunciation("ˈməʊbaɪl", "GB")
        val pMobileUS = Pronunciation("ˈmoʊbil", "US")
        val paMobile1 = arrayOf(pMobile, pMobileGB, pMobileUS)
        val paMobile2 = arrayOf(pMobile, pMobileUS, pMobileGB)
        ps.println("pronunciations1 = ${paMobile1.withIndex().joinToString(separator = " ") { "#${it.index} ${it.value}" }}")
        ps.println("pronunciations2 = ${paMobile2.withIndex().joinToString(separator = " ") { "#${it.index} ${it.value}" }}")
        assertFalse(paMobile1.contentEquals(paMobile2))
        assertFalse(paMobile1.contentEquals(paMobile2))
        assertEquals(paMobile1.toSet(), paMobile2.toSet())

        val lexMobile0 = Lex(wordMobile, "n", "source1")
        val lexMobile1 = Lex(wordMobile, "n", "source1").apply { pronunciations = paMobile1.toSet() }
        val lexMobile2 = Lex(wordMobile, "n", "source2").apply { pronunciations = paMobile2.toSet() }
        ps.println("lex0 = $lexMobile0")
        ps.println("lex1 = $lexMobile1")
        ps.println("lex2 = $lexMobile2")

        val k0 = Key.W_P_A.of_t(lexMobile0)
        val k1 = Key.W_P_A.of_t(lexMobile1)
        val k2 = Key.W_P_A.of_t(lexMobile2)
        ps.println("key0 = $k0")
        ps.println("key1 = $k1")
        ps.println("key2 = $k2")

        val p0 = k0.pronunciations ?: emptySet()
        val p1 = k1.pronunciations ?: emptySet()
        val p2 = k2.pronunciations ?: emptySet()
        assertEquals(p1, p2)
        assertNotEquals(p1, p0)

        assertEquals(k1, k2)
        assertNotEquals(k1, k0)
    }

    @Test
    fun testMobile() {
        val wordMobile = "mobile"
        val pMobile = Pronunciation("ˈməʊbaɪl", null)
        val pMobileGB = Pronunciation("ˈməʊbaɪl", "GB")
        val pMobileUS = Pronunciation("ˈmoʊbil", "US")
        val paMobile1 = arrayOf(pMobile, pMobileGB, pMobileUS)
        val paMobile2 = arrayOf(pMobile, pMobileGB, pMobileUS)

        val lexMobile0 = Lex(wordMobile, "n", "source1").apply { pronunciations = null }
        val lexMobile1 = Lex(wordMobile, "n", "source1").apply { pronunciations = paMobile1.toSet() }
        val lexMobile2 = Lex(wordMobile, "n", "source2").apply { pronunciations = paMobile2.toSet() }

        assertEquals(Key.W_P_A.of_t(lexMobile1), Key.W_P_A.of_t(lexMobile2))
        assertEquals(Key.W_P_D.of_t(lexMobile1), Key.W_P_D.of_t(lexMobile2))
        assertEquals(Key.W_P.of_t(lexMobile1), Key.W_P.of_t(lexMobile2))

        assertNotEquals(Key.W_P_A.of_t(lexMobile1), Key.W_P_A.of_t(lexMobile0))
        assertEquals(Key.W_P_D.of_t(lexMobile1), Key.W_P_D.of_t(lexMobile0)) // because discriminant is both null
        assertEquals(Key.W_P.of_t(lexMobile1), Key.W_P.of_t(lexMobile0))
    }

    @Test
    fun testCritical() {
        val wordCritical = "critical"
        val lexCriticalA = Lex(wordCritical, "a", "source1")
        val lexCriticalS = Lex(wordCritical, "s", "source2")

        assertNotEquals(Key.W_P_A.of_t(lexCriticalA), Key.W_P_A.of_t(lexCriticalS))
        assertNotEquals(Key.W_P_D.of_t(lexCriticalA), Key.W_P_D.of_t(lexCriticalS))
        assertEquals(Key.W_P.of_p(lexCriticalA), Key.W_P.of_p(lexCriticalS))
        assertEquals(Key.W_P_A.of_p(lexCriticalA), Key.W_P_A.of_p(lexCriticalS)) // A and S are merged
    }

    @Test
    fun testCapitalisation() {
        val wordEarthL = "earth"
        val wordEarthU = "Earth"
        val lexEarthL = Lex(wordEarthL, "n", "source1")
        val lexEarthU = Lex(wordEarthU, "n", "source2")

        assertNotEquals(Key.W_P_A.of_t(lexEarthL), Key.W_P_A.of_t(lexEarthU))
        assertNotEquals(Key.W_P_D.of_t(lexEarthL), Key.W_P_D.of_t(lexEarthU))
        assertEquals(Key.W_P.of_lc_t(lexEarthL), Key.W_P.of_lc_t(lexEarthU))
        assertEquals(Key.W_P_A.of_lc_t(lexEarthL), Key.W_P_A.of_lc_t(lexEarthU))
    }

    @Test
    fun testPronunciations() {
        val pMobile = Pronunciation("ˈməʊbaɪl", null)
        val pMobileGB = Pronunciation("ˈməʊbaɪl", "GB")
        val pMobileUS = Pronunciation("ˈmoʊbil", "US")
        val paMobile1 = arrayOf(pMobile, pMobileGB, pMobileUS)
        val paMobile2 = arrayOf(pMobile, pMobileGB, pMobileUS)
        val psMobile1 = setOf(*paMobile1)
        val psMobile2 = setOf(*paMobile2)

        assertEquals(pMobile, Pronunciation("ˈməʊbaɪl", null))
        assertEquals(pMobileGB, Pronunciation("ˈməʊbaɪl", "GB"))
        assertNotEquals(pMobile, null)
        assertNotEquals(pMobile, pMobileGB)
        assertNotEquals(pMobile, pMobileUS)
        assertNotEquals(pMobileGB, pMobileUS)

        assertNotEquals(paMobile1, null)
        assertNotEquals(paMobile1, arrayOf<Pronunciation>())
        assertNotEquals(paMobile1, arrayOf(pMobileGB, pMobile, pMobileUS))
        assertNotEquals(paMobile2, arrayOf(pMobileGB, pMobileUS, pMobile))
        assertContentEquals(paMobile1, arrayOf(pMobile, pMobileGB, pMobileUS))
        assertContentEquals(paMobile2, arrayOf(pMobile, pMobileGB, pMobileUS))

        assertEquals(psMobile1, setOf(*paMobile1))
        assertEquals(psMobile2, setOf(*paMobile2))
        assertEquals(psMobile1, setOf(pMobileGB, pMobile, pMobileUS))
        assertEquals(psMobile2, setOf(pMobile, pMobileGB, pMobileUS))
        assertEquals(
            psMobile1,
            setOf(Pronunciation("ˈməʊbaɪl", "GB"), Pronunciation("ˈməʊbaɪl", null), Pronunciation("ˈmoʊbil", "US"))
        )
        assertEquals(
            psMobile2,
            setOf(Pronunciation("ˈməʊbaɪl", null), Pronunciation("ˈməʊbaɪl", "GB"), Pronunciation("ˈmoʊbil", "US"))
        )

        assertNotEquals(paMobile1, paMobile2)
        assertEquals(psMobile1, psMobile2)

        val pRowOu = Pronunciation("ɹəʊ", null)
        val pRowAu = Pronunciation("ɹaʊ", null)
        val paRow1 = arrayOf(pRowOu, pRowAu)
        val paRow2 = arrayOf(pRowAu, pRowOu)
        val psRow1 = setOf(*paRow1)
        val psRow2 = setOf(*paRow2)

        assertNotEquals(pRowOu, pRowAu)
        assertNotEquals(paRow1, paRow2)
        assertEquals(psRow1, psRow2)
    }

    companion object {

        private val ps = if (!System.getProperties().containsKey("SILENT")) Tracing.psInfo else Tracing.psNull
    }
}
