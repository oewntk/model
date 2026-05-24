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
 * Either SynsetType or Category
 */
enum class CategoryImpl(val value: Char, name:String) {
    N('n', "noun") {
        override fun toSynsetType(): SynsetTypeImpl = SynsetTypeImpl.N
        override fun toPartOfSpeech(): PartOfSpeechImpl = PartOfSpeechImpl.N
    },
    V('v',"verb") {
        override fun toSynsetType(): SynsetTypeImpl = SynsetTypeImpl.V
        override fun toPartOfSpeech(): PartOfSpeechImpl = PartOfSpeechImpl.V
    },
    A('a',"adj") {
        override fun toSynsetType(): SynsetTypeImpl = SynsetTypeImpl.A
        override fun toPartOfSpeech(): PartOfSpeechImpl = PartOfSpeechImpl.A
    },
    R('r',"adv") {
        override fun toSynsetType(): SynsetTypeImpl = SynsetTypeImpl.R
        override fun toPartOfSpeech(): PartOfSpeechImpl = PartOfSpeechImpl.R
    },
    S('s',"sat_adj") {
        override fun toSynsetType(): SynsetTypeImpl = SynsetTypeImpl.A
        override fun toPartOfSpeech(): PartOfSpeechImpl = PartOfSpeechImpl.A
    };

    abstract fun toSynsetType(): SynsetTypeImpl

    abstract fun toPartOfSpeech(): PartOfSpeechImpl

    companion object {
        fun fromCharOrNull(c: Char): CategoryImpl? {
            return when (c) {
                'n' -> N
                'v' -> V
                'a' -> A
                'r' -> R
                's' -> S
                else -> null
            }
        }

        fun fromChar(c: Char): CategoryImpl = fromCharOrNull(c) ?: throw IllegalArgumentException("Illegal PartOfSpeech: $c")
    }
}

/**
 * Synset type
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

        fun fromChar(c: Char): SynsetTypeImpl = fromCharOrNull(c) ?: throw IllegalArgumentException("Illegal PartOfSpeech: $c")
    }
}

/**
 * [n,v,a,r,s]
 */
enum class PartOfSpeechImpl(val value: Char) {
    N('n') {
        override fun toSynsetType(): SynsetType = SynsetType.N
        override fun toCategory(): CategoryImpl = CategoryImpl.N
    },
    V('v') {
        override fun toSynsetType(): SynsetType = SynsetType.V
        override fun toCategory(): CategoryImpl = CategoryImpl.V
    },
    A('a') {
        override fun toSynsetType(): SynsetType = SynsetType.A
        override fun toCategory(): CategoryImpl = CategoryImpl.A
    },
    R('r') {
        override fun toSynsetType(): SynsetType = SynsetType.R
        override fun toCategory(): CategoryImpl = CategoryImpl.R
    };

    abstract fun toSynsetType(): SynsetType
    abstract fun toCategory(): CategoryImpl

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

        fun fromChar(c: Char): PartOfSpeechImpl = fromCharOrNull(c) ?: throw IllegalArgumentException("Illegal PartOfSpeech: $c")
    }
}
