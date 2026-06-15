/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

object LibModelSubset {

    private fun <T> subsetOf(items: Sequence<T>, from: Int = (1000..100000).random(), howMany: Int = 100): Sequence<T> = items
        .drop(from)
        .take(howMany)

    private fun <T> subsetOf(items: Collection<T>, from: Int = (1000..100000).random(), howMany: Int = 100): Sequence<T> = subsetOf(items.asSequence(), from = from, howMany = howMany)

    fun CoreModel.lexSubset(from: Int = (1000..100000).random(), howMany: Int = 100): List<Lex> = subsetOf(lexes, from = from, howMany = howMany).toList()

    fun CoreModel.synsetSubset(from: Int = (1000..100000).random(), howMany: Int = 100): List<Synset> = subsetOf(synsets, from = from, howMany = howMany).toList()

    fun CoreModel.senseSubset(from: Int = (1000..100000).random(), howMany: Int = 100): List<Sense> = subsetOf(senses, from = from, howMany = howMany).toList()

    fun CoreModel.subset(from: Int = (1000..100000).random(), howMany: Int = 100): Triple<List<Lex>, List<Synset>, List<Sense>> = Triple(
        lexSubset( from = from, howMany = howMany),
        synsetSubset( from = from, howMany = howMany),
        senseSubset( from = from, howMany = howMany),
    )
}