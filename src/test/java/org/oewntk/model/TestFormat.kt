/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.Formatter.joinToString
import kotlin.test.assertEquals

class TestFormat {

    private val map = mapOf(
        1 to listOf("a", 'b', "c", "d"),
        2 to listOf("e", 'f', "g", "h"),
        3 to listOf("i", 'j', "k", "l"),
        4 to listOf("m", 'n', "o", "p"),
        5 to listOf("q", 'r', "s", "t"),
        6 to listOf("u", 'v', "w", "x"),
        7 to listOf("y", 'z'),
    )

    @Test
    fun testMap() {
        val r = map.joinToString()
        ps.println(r)
        assertEquals("[1]=a,b,c,d [2]=e,f,g,h [3]=i,j,k,l [4]=m,n,o,p [5]=q,r,s,t [6]=u,v,w,x [7]=y,z", r)
    }

    @Test
    fun testMapWithNewLine() {
        val r = map.joinToString(entrySeparator="\n")
        ps.println(r)
        assertEquals("[1]=a,b,c,d\n[2]=e,f,g,h\n[3]=i,j,k,l\n[4]=m,n,o,p\n[5]=q,r,s,t\n[6]=u,v,w,x\n[7]=y,z", r)
    }

    @Test
    fun testMapWithValueSeparator() {
        val r = map.joinToString(entrySeparator="\n", valueSeparator="#")
        ps.println(r)
        assertEquals("[1]=a#b#c#d\n[2]=e#f#g#h\n[3]=i#j#k#l\n[4]=m#n#o#p\n[5]=q#r#s#t\n[6]=u#v#w#x\n[7]=y#z", r)
    }

    @Test
    fun testMapWithValueSeparatorAndPrefixAndPostFix() {
        val r = map.joinToString(entrySeparator="\n", valueSeparator=" ", valuePrefix = "{", valuePostfix ="}")
        ps.println(r)
        assertEquals("[1]={a b c d}\n[2]={e f g h}\n[3]={i j k l}\n[4]={m n o p}\n[5]={q r s t}\n[6]={u v w x}\n[7]={y z}", r)
    }

    companion object {

        private val ps = if (!System.getProperties().containsKey("SILENT")) Tracing.psInfo else Tracing.psNull

        @Suppress("EmptyMethod")
        @JvmStatic
        @BeforeClass
        fun init() {
        }
    }
}
