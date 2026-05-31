/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

object LibModelSubset {

    fun <T> CoreModel.subset(items: Collection<T>, from: Int = (1000..100000).random(), howMany: Int = 100): Sequence<T> = items
        .asSequence()
        .drop(from)
        .take(howMany)

    fun CoreModel.lexSubset(from: Int = (1000..100000).random(), howMany: Int = 100): Sequence<Lex> = subset(lexes)

    fun CoreModel.synsetSubset(from: Int = (1000..100000).random(), howMany: Int = 100): Sequence<Synset> = subset(synsets)
}