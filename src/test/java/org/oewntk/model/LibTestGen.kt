/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.LibModelSubset.lexSubset
import org.oewntk.model.LibModelSubset.synsetSubset

object LibTestGen {

    fun genDummyMap() = mutableMapOf(
        "a" to "r",
        "b" to mutableListOf("s", "t"),
        "c" to mutableMapOf(
            "m" to "u",
            "n" to "v"
        ),
        "d" to mutableMapOf(
            "m" to "u",
            "n" to mutableListOf("v", "w"),
            "p" to mutableMapOf(
                "m" to "u",
                "n" to mutableListOf("v", "w"),
            )
        ),
    )

    fun genSenses(i: Int, j: Int, n: Int = 3) = Array(n) {
        mutableMapOf<String, Any>(
            "id" to "sk-$i-$j-$it",
            "synset" to "sy-$i-$j-$it",
        ).apply {
            genRelations(n = 2).forEach { (rel, targets) ->
                this[rel] = targets.map { t: Int -> "sk-$t" }.toList()
            }
        }
    }.toList()

    fun genLexes(i: Int, n: Int = 3) = Array(n) { j ->
        mapOf(
            "pronunciation" to "a:ha:",
            "sense" to genSenses(i, j, n = 3)
        )
    }

    fun genLexEntries() = (1 until 5).associate { i ->
        "lemma-$i" to mapOf(
            "pos-1" to genLexes(i),
            "pos-2" to genLexes(i + 1, n = 1),
            "pos-3" to genLexes(i + 3, n = 2),
        )
    }

    fun genMembers(i: Int, n: Int = 3) = Array(n) { j ->
        "member-$i-$j"
    }.toList()

    fun genTargets(n: Int = 3) = Array(n) {
        (0..100).random()
    }.toList()

    fun genRelations(n: Int = 3) = Array(n) { j ->
        "relation-$j" to genTargets(n = 2)
    }.toList()

    fun genSynsets(n: Int = 3) = Array(n) { i ->
        mutableMapOf(
            "id" to "sy-$i",
            "definition" to "definition",
            "member" to genMembers(i, n = 3),
        ).apply {
            genRelations(n = 2).forEach { (rel, targets) ->
                this[rel] = targets.map { "sy-$it" }.toList()
            }
        }
    }

    fun genSynsetEntries() = genSynsets(n = 5).associateBy { it["id"] }

    fun genModelSerializables(model: CoreModel): Sequence<Pair<Map<String, Any>, Filename>> {
        return sequence {
            val someSerializedLexes = model
                .lexSubset()
                .asSequence()
                .toOEWNData(model.senseResolver)
            yield(someSerializedLexes to "entries-some") // yield content with source file base

            val someSerializedSynsets = model
                .synsetSubset()
                .asSequence()
                .toOEWNData()
            yield(someSerializedSynsets to "data-some")  // yield content with source file base
        }
    }
}
