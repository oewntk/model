/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.junit.Assert.assertEquals
import org.junit.Test

class TestLexicographicOrder {

    @Test
    fun testRow() {
        val list = mutableListOf(
            "alpha",
            "Alpha",
            "beta",
            "Beta",
            "gamma",
            "Gamma",
            "Alpha",
            "alPha",
            "Beta",
            "beTa",
            "Gamma",
            "gaMma"
        )

        ps.printf("%50s %s%n", "original", list)

        list.sortWith { str: String, otherStr: String ->
            str.compareTo(otherStr)
        }
        ps.printf("%50s %s%n", "String::compareTo", list)
        assertEquals(
            "[Alpha, Alpha, Beta, Beta, Gamma, Gamma, alPha, alpha, beTa, beta, gaMma, gamma]",
            list.toString()
        )

        list.sortWith { str: String, otherStr: String -> str.compareTo(otherStr, ignoreCase = true) }
        ps.printf("%50s %s%n", "String::compareToIgnoreCase", list)
        assertEquals(
            "[Alpha, Alpha, alPha, alpha, Beta, Beta, beTa, beta, Gamma, Gamma, gaMma, gamma]",
            list.toString()
        )

        list.sortWith(LexicographicOrder.upperFirst)
        ps.printf("%50s %s%n", "upperFirst", list)
        assertEquals(
            "[Alpha, Alpha, alPha, alpha, Beta, Beta, beTa, beta, Gamma, Gamma, gaMma, gamma]",
            list.toString()
        )

        list.sortWith(LexicographicOrder.lowerFirst)
        ps.printf("%50s %s%n", "lowerFirst", list)
        assertEquals(
            "[alpha, alPha, Alpha, Alpha, beta, beTa, Beta, Beta, gamma, gaMma, Gamma, Gamma]",
            list.toString()
        )
    }

    companion object {

        private val ps = if (!System.getProperties().containsKey("SILENT")) Tracing.psInfo else Tracing.psNull
    }
}
