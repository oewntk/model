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

    fun CoreModel.synsetSubset(from: Int = (1000..100000).random(), howMany: Int = 100): Sequence<Synset> = subsetOf(synsets, howMany = howMany)
}