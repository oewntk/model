/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TestEqualsJVM {

    @Test
    fun testPairs() {
        val obj1 = "jest" to "joke"
        val obj2 = "jest" to "joke"
        assertTrue(Objects.equals(obj1, obj2))
        assertEquals(obj1, obj2)
    }

    @Test
    fun testTriples() {
        val obj1 = Triple("jest", "joke", "jester")
        val obj2 = Triple("jest", "joke", "jester")
        assertTrue(Objects.equals(obj1, obj2))
        assertEquals(obj1, obj2)
    }

    @Test
    fun testArrays() {
        val obj1 = arrayOf("jest", "joke")
        val obj2 = arrayOf("jest", "joke")
        val obj3 = arrayOf("joke", "jest")
        assertFalse(Objects.equals(obj1, obj2))
        assertNotEquals(obj1, obj2)
        @Suppress("ReplaceJavaStaticMethodWithKotlinAnalog")
        assertTrue(Arrays.equals(obj1, obj2))
        assertTrue(obj1.contentEquals(obj2))
        assertNotEquals(obj1, obj2)
        assertFalse(obj1.contentEquals(obj3))
    }

    @Test
    fun testLists() {
        val obj1 = listOf("jest", "joke")
        val obj2 = listOf("jest", "joke")
        val obj3 = listOf("joke", "jest")
        assertTrue(Objects.equals(obj1, obj2))
        assertEquals(obj1, obj2)
        assertFalse(Objects.equals(obj1, obj3))
        assertNotEquals(obj1, obj3)
    }

    @Test
    fun testSets() {
        val obj1 = setOf("jest", "joke")
        val obj2 = setOf("joke", "jest")
        assertTrue(Objects.equals(obj1, obj2))
        assertEquals(obj1, obj2)
    }

    @Test
    fun testMutableUnMutableSets() {
        val obj1 = mutableSetOf("jest", "joke")
        val obj2 = setOf("joke", "jest")
        assertTrue(Objects.equals(obj1, obj2))
        sertEquals(obj2, obj1)
    }

    @Test
    fun testModifiableUnmodifiableSets() {
        val obj1 = mutableSetOf("jest", "joke")
        val obj2 = Collections.unmodifiableSet(setOf("joke", "jest"))
        assertTrue(Objects.equals(obj1, obj2))
        assertEquals(obj2, obj1)
    }

    @Test
    fun testUnmodifiableSets() {
        val obj1 = setOf("jest", "joke")
        val obj2 = Collections.unmodifiableSet(setOf("jest", "joke"))
        assertTrue(Objects.equals(obj1, obj2))
        assertEquals(obj1, obj2)
    }

    //companion object {
    //    @JvmStatic
    //    @BeforeClass
    //    fun init() {
    //    }
    //}
}
