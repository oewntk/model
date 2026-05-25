/*
 * Copyright (c) 2024-2024. Bernard Bou.
 */

package org.oewntk.model

typealias Lemma = String
typealias SenseKey = String
typealias SynsetId = String
typealias Relation = String
typealias Discriminant = String
typealias Key2 = String
typealias Domain = String
typealias Morph = String
typealias PronunciationValue = String
typealias PronunciationVariety = String
typealias VerbFrameId = String
typealias VerbTemplateId = Int
typealias AdjPosition = String

typealias LexEntry = Map.Entry<Lemma, Collection<Lex>>

/**
 * Either SynsetType or Category
 */
typealias Category = CategoryImpl

/**
 * [n,v,a,r]
 */
typealias SynsetType = SynsetTypeImpl

/**
 * [n,v,a,r,s]
 */
typealias PartOfSpeech = PartOfSpeechImpl

/**
 * Pos key used as a pos key
 */
enum class CategoryImpl(val value: Char) {
    N('n') {
        override fun toPartOfSpeech(): PartOfSpeechImpl = PartOfSpeechImpl.N
    },
    V('v') {
        override fun toPartOfSpeech(): PartOfSpeechImpl = PartOfSpeechImpl.V
    },
    A('a') {
        override fun toPartOfSpeech(): PartOfSpeechImpl = PartOfSpeechImpl.A
    },
    R('r') {
        override fun toPartOfSpeech(): PartOfSpeechImpl = PartOfSpeechImpl.R
    },
    S('s') {
        override fun toPartOfSpeech(): PartOfSpeechImpl = PartOfSpeechImpl.A
    };

    abstract fun toPartOfSpeech(): PartOfSpeechImpl
}

/**
 * Synset type
 *
 * [n,v,a,r]
 */
enum class SynsetTypeImpl(val value: Char) {
    N('n') {
        override fun toPartOfSpeech(): PartOfSpeechImpl = PartOfSpeechImpl.N
        override fun toCategory(): CategoryImpl = CategoryImpl.N
    },
    V('v') {
        override fun toPartOfSpeech(): PartOfSpeechImpl = PartOfSpeechImpl.V
        override fun toCategory(): CategoryImpl = CategoryImpl.V
    },
    A('a') {
        override fun toPartOfSpeech(): PartOfSpeechImpl = PartOfSpeechImpl.A
        override fun toCategory(): CategoryImpl = CategoryImpl.A
    },
    R('r') {
        override fun toPartOfSpeech(): PartOfSpeechImpl = PartOfSpeechImpl.R
        override fun toCategory(): CategoryImpl = CategoryImpl.R
    },
    S('s') {
        override fun toPartOfSpeech(): PartOfSpeechImpl = PartOfSpeechImpl.A
        override fun toCategory(): CategoryImpl = CategoryImpl.S
    };

    abstract fun toPartOfSpeech(): PartOfSpeechImpl
    abstract fun toCategory(): CategoryImpl
    override fun toString(): String { throw IllegalAccessException("Illegal: use .value ${this.value}") }

    companion object {
        fun fromCharOrNull(c: Char): SynsetTypeImpl? {
            return when (c) {
                'n' -> N
                'v' -> V
                'a' -> A
                'r' -> R
                's' -> S
                else -> null
            }
        }

        fun fromChar(c: Char): SynsetTypeImpl = fromCharOrNull(c) ?: throw IllegalArgumentException("Illegal SynsetType: $c")
    }
}

/**
 * Part-of-Speech
 *
 * [n,v,a,r,s]
 */
enum class PartOfSpeechImpl(val value: Char, val fullName: String) {
    N('n', "noun") {
        override fun toCategory(): CategoryImpl = CategoryImpl.N
    },
    V('v', "verb") {
        override fun toCategory(): CategoryImpl = CategoryImpl.V
    },
    A('a', "adj") {
        override fun toCategory(): CategoryImpl = CategoryImpl.A
    },
    R('r', "adv") {
        override fun toCategory(): CategoryImpl = CategoryImpl.R
    };

    abstract fun toCategory(): CategoryImpl
    override fun toString(): String { throw IllegalAccessException("Illegal: use .value ${this.value}") }

    companion object {
        fun fromCharOrNull(c: Char): PartOfSpeechImpl? {
            return when (c) {
                'n' -> N
                'v' -> V
                'a' -> A
                'r' -> R
                else -> null
            }
        }

        fun fromFullNameOrNull(fullName: String): PartOfSpeechImpl? = entries.firstOrNull { it.fullName == fullName }

        fun fromChar(c: Char): PartOfSpeechImpl = fromCharOrNull(c) ?: throw IllegalArgumentException("Illegal PartOfSpeech: $c")

        fun fromFullName(fullName: String): PartOfSpeechImpl = fromFullNameOrNull(fullName) ?: throw IllegalArgumentException("Illegal PartOfSpeech: $fullName")
    }
}
