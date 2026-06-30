/*
 * Copyright (c) 2024-2024. Bernard Bou.
 */

package org.oewntk.model

import java.io.Serializable
import java.util.Comparator
import java.util.Objects

typealias Lemma = String
typealias Key2 = String
typealias Discriminant = String
typealias LexId = LexIdImpl
typealias SenseKey = String
typealias SynsetId = String

typealias LowerCasedLemma = String
typealias Relation = String

typealias Domain = String
typealias Morph = String
typealias PronunciationValue = String
typealias PronunciationVariety = String
typealias VerbFrameId = String
typealias VerbTemplateId = Int
typealias AdjPosition = String

typealias HyperMap = Map<Lemma, Map<Key2, Collection<Lex>>>
typealias HyperMap1 = Map<Lemma, Map<Key2, Lex>>

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
 * LexId
 *
 * @property lemma lemma
 * @property type type
 * @property discriminant discriminant (nullable)
 */
@kotlinx.serialization.Serializable
data class LexIdImpl(val lemma: Lemma, val type: SynsetType, val discriminant: Discriminant?) : Serializable {

    val partOfSpeech
        get() = type.toPartOfSpeech()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return if (other is LexId) Objects.equals(lemma, other.lemma)
                    && Objects.equals(partOfSpeech, other.partOfSpeech)
                    && Objects.equals(discriminant, other.discriminant) else false
    }

    override fun hashCode(): Int = Objects.hash(lemma, partOfSpeech, discriminant)

    override fun toString() = "$lemma-${type.value}" + if (discriminant != null) "-$discriminant" else ""
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

    override fun toString(): String {
        throw IllegalAccessException("Illegal: use .value ${this.value}")
    }

    companion object {
        fun fromCharOrNull(c: Char): SynsetTypeImpl? {
            return when (c) {
                'n', 'N' -> N
                'v', 'V' -> V
                'a', 'A' -> A
                'r', 'R' -> R
                's', 'S' -> S
                else -> null
            }
        }

        fun fromChar(c: Char): SynsetTypeImpl = fromCharOrNull(c) ?: throw IllegalArgumentException("Illegal SynsetType: $c")

        fun fromKey2(key2: Key2): SynsetTypeImpl {
            if (key2.isEmpty()) throw IllegalArgumentException("Illegal SynsetType: $key2")
            return fromChar(key2[0])
        }

        fun discriminantFromKey2(key2: Key2): Discriminant? {
            return if (key2.length > 1) key2.substring(1) else null
        }

        val synsetTypeComparator: Comparator<SynsetType> = compareBy(SynsetType::value)
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
    override fun toString(): String {
        throw IllegalAccessException("Illegal: use .value ${this.value}")
    }

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

        val partOfSpeechComparator: Comparator<PartOfSpeech> = compareBy(PartOfSpeech::value)
    }
}

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
