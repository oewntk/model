/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.junit.Assert.*
import org.junit.Test
import java.io.PrintStream
import kotlin.test.assertContentEquals

class TestKeys {

    @Test
    fun testRow() {
        val wordRow = "row"
        val pRowOu = Pronunciation("ɹəʊ", null)
        val pRowAu = Pronunciation("ɹaʊ", null)

        val lexRowOu = Lex(wordRow, "n-1").apply { pronunciations = setOf(pRowOu) }
        val lexRowAu = Lex(wordRow, "n-2").apply { pronunciations = setOf(pRowAu) }
        val lexRowOuN = Lex(wordRow, "n").apply { pronunciations = setOf(pRowOu) }
        val lexRowAuN = Lex(wordRow, "n").apply { pronunciations = setOf(pRowAu) }

        assertNotEquals(Key.UsingPronunciation.of(lexRowOu), Key.UsingPronunciation.of(lexRowAu))
        assertNotEquals(Key.UsingDiscriminant.of(lexRowOu), Key.UsingDiscriminant.of(lexRowAu)) // because discriminant is different
        assertEquals(Key.UsingDiscriminant.of(lexRowOuN), Key.UsingDiscriminant.of(lexRowAuN)) // because discriminant is same
        assertEquals(
            Key.Base.of(lexRowOu, Lex::lemma) { it.type.toCategory() },
            Key.Base.of(lexRowAu, Lex::lemma) { it.type.toCategory() }
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

        val lexMobile0 = Lex(wordMobile, "n")
        val lexMobile1 = Lex(wordMobile, "n").apply { pronunciations = paMobile1.toSet() }
        val lexMobile2 = Lex(wordMobile, "n").apply { pronunciations = paMobile2.toSet() }
        ps.println("lex0 = $lexMobile0")
        ps.println("lex1 = $lexMobile1")
        ps.println("lex2 = $lexMobile2")

        val k0 = Key.UsingPronunciation.of(lexMobile0)
        val k1 = Key.UsingPronunciation.of(lexMobile1)
        val k2 = Key.UsingPronunciation.of(lexMobile2)
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

        val lexMobile0 = Lex(wordMobile, "n").apply { pronunciations = null }
        val lexMobile1 = Lex(wordMobile, "n").apply { pronunciations = paMobile1.toSet() }
        val lexMobile2 = Lex(wordMobile, "n").apply { pronunciations = paMobile2.toSet() }

        assertEquals(Key.UsingPronunciation.of(lexMobile1), Key.UsingPronunciation.of(lexMobile2))
        assertEquals(Key.UsingDiscriminant.of(lexMobile1), Key.UsingDiscriminant.of(lexMobile2))
        assertEquals(Key.Base.of(lexMobile1), Key.Base.of(lexMobile2))

        assertNotEquals(Key.UsingPronunciation.of(lexMobile1), Key.UsingPronunciation.of(lexMobile0))
        assertEquals(Key.UsingDiscriminant.of(lexMobile1), Key.UsingDiscriminant.of(lexMobile0)) // because discriminant is both null
        assertEquals(Key.Base.of(lexMobile1), Key.Base.of(lexMobile0))
    }

    @Test
    fun testCritical() {
        val wordCritical = "critical"
        val lexCriticalA = Lex(wordCritical, "a")
        val lexCriticalS = Lex(wordCritical, "s")

        assertNotEquals(Key.UsingPronunciation.of(lexCriticalA), Key.UsingPronunciation.of(lexCriticalS))
        assertNotEquals(Key.UsingDiscriminant.of(lexCriticalA), Key.UsingDiscriminant.of(lexCriticalS))
        assertEquals(Key.Base.ofUsingPartOfSpeech(lexCriticalA), Key.Base.ofUsingPartOfSpeech(lexCriticalS))
        assertEquals(Key.UsingPronunciation.ofUsingPartOfSpeech(lexCriticalA), Key.UsingPronunciation.ofUsingPartOfSpeech(lexCriticalS)) // A and S are merged
    }

    @Test
    fun testCapitalisation() {
        val wordEarthL = "earth"
        val wordEarthU = "Earth"
        val lexEarthL = Lex(wordEarthL, "n")
        val lexEarthU = Lex(wordEarthU, "n")

        assertNotEquals(Key.UsingPronunciation.of(lexEarthL), Key.UsingPronunciation.of(lexEarthU))
        assertNotEquals(Key.UsingDiscriminant.of(lexEarthL), Key.UsingDiscriminant.of(lexEarthU))
        assertEquals(Key.Base.ofIgnoringCase(lexEarthL), Key.Base.ofIgnoringCase(lexEarthU))
        assertEquals(Key.UsingPronunciation.ofIgnoringCase(lexEarthL), Key.UsingPronunciation.ofIgnoringCase(lexEarthU))
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

        private val silent = if (System.getProperties().containsKey("VERBOSE")) false
        else if (System.getProperties().containsKey("SILENT")) true
        else true

        private val ps: PrintStream = if (!silent) Tracing.psInfo else Tracing.psNull
    }
}
