/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

object LibModelSubset {

    fun <T> subsetOf(items: Sequence<T>, from: Int = (1000..100000).random(), howMany: Int = 100): Sequence<T> = items
        .drop(from)
        .take(howMany)

    fun <T> subsetOf(items: Collection<T>, from: Int = (1000..100000).random(), howMany: Int = 100): Sequence<T> = subsetOf(items.asSequence(), from = from, howMany = howMany)

    fun CoreModel.lexSubset(from: Int = (1000..100000).random(), howMany: Int = 100): Sequence<Lex> = subsetOf(lexes, from = from, howMany = howMany)

    fun CoreModel.synsetSubset(from: Int = (1000..100000).random(), howMany: Int = 100): Sequence<Synset> = subsetOf(synsets, from = from, howMany = howMany)

    fun CoreModel.senseSubset(from: Int = (1000..100000).random(), howMany: Int = 100): Sequence<Sense> = subsetOf(senses, from = from, howMany = howMany)

    fun CoreModel.subset(from: Int = (1000..100000).random(), howMany: Int = 100): Triple<Sequence<Lex>, Sequence<Synset>, Sequence<Sense>> = Triple(
        lexSubset( from = from, howMany = howMany),
        synsetSubset( from = from, howMany = howMany),
        senseSubset( from = from, howMany = howMany),
    )
}