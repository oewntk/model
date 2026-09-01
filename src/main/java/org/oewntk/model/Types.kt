/*
 * Copyright (c) 2024-2024. Bernard Bou.
 */

package org.oewntk.model

import java.io.Serializable
import java.util.Comparator
import java.util.Locale
import java.util.Objects

typealias Lemma = LemmaImpl
typealias Key2 = Key2Impl
typealias Discriminant = DiscriminantImpl
typealias LexId = LexIdImpl
typealias SenseKey = SenseKeyImpl
typealias SynsetId = SynsetIdImpl

typealias Relation = RelationImpl

typealias Example = ExampleImpl
typealias PronunciationValue = PronunciationValueImpl

typealias PronunciationVariety = String
typealias Domain = String
typealias AdjPosition = String
typealias Morph = String
typealias VerbFrameId = String
typealias VerbTemplateId = Int

typealias HyperMap = Map<Lemma, Map<Key2, Collection<Lex>>>
typealias HyperMap1 = Map<Lemma, Map<Key2, Lex>>

/**
 * Either SynsetType or Category
 */
typealias Category = CategoryImpl

/**
 * [n,v,a,r,s]
 */
typealias SynsetType = SynsetTypeImpl

/**
 * [n,v,a,r]
 */
typealias PartOfSpeech = PartOfSpeechImpl

/**
 * SenseKey implementation
 */
@kotlinx.serialization.Serializable
@JvmInline
value class SenseKeyImpl(val id: String) : Comparable<SenseKeyImpl>, Serializable {
    init {
        require(senseKeyRegex.matches(id)) { "Invalid sense key: '$id'" }
    }

    override fun toString(): String = id
    override fun compareTo(other: SenseKeyImpl): Int = id.compareTo(other.id)
}

/**
 * Synset Id implementation
 */
@kotlinx.serialization.Serializable
@JvmInline
value class SynsetIdImpl(val id: String) : Comparable<SynsetIdImpl>, Serializable {
    init {
        require(synsetIdRegex.matches(id)) { "Invalid synset id: '$id'" }
    }

    override fun toString(): String = id
    override fun compareTo(other: SynsetIdImpl): Int = id.compareTo(other.id)
}

/**
 * Lemma implementation
 */
@kotlinx.serialization.Serializable
@JvmInline
value class LemmaImpl(val form: String) : Comparable<LemmaImpl>, Serializable {
    init {
        require(lemmaRegex.matches(form)) { "Invalid lemma: '$form'" }
    }

    override fun toString(): String = form
    override fun compareTo(other: LemmaImpl): Int = form.compareTo(other.form)
    val lowercased: String
        get() = form.lowercase(Locale.ENGLISH)
    val lCLemma: Lemma
        get() = Lemma(lowercased)
}

/**
 * LexId
 *
 * @property lemma lemma
 * @property partOfSpeech part of speech
 * @property discriminant discriminant (nullable)
 */
@kotlinx.serialization.Serializable
data class LexIdImpl(val lemma: Lemma, val partOfSpeech: PartOfSpeech, val discriminant: Discriminant? = null) : Serializable {

    override fun equals(other: Any?): Boolean {
        return this === other || other is LexId && (
                Objects.equals(lemma, other.lemma)
                        && Objects.equals(partOfSpeech, other.partOfSpeech)
                        && Objects.equals(discriminant, other.discriminant))
    }

    override fun hashCode(): Int = Objects.hash(lemma, partOfSpeech, discriminant)

    override fun toString() = "$lemma-${partOfSpeech.value}" + if (discriminant != null) "-$discriminant" else ""
}

/**
 * Key2 implementation
 */
@kotlinx.serialization.Serializable
@JvmInline
value class Key2Impl(val id: String) : Comparable<Key2Impl>, Serializable {
    init {
        require(key2Regex.matches(id)) { "Invalid key2: '$id'" }
    }

    override fun toString(): String = id
    override fun compareTo(other: Key2Impl): Int = id.compareTo(other.id)
}

/**
 * Discriminant implementation
 */
@kotlinx.serialization.Serializable
@JvmInline
value class DiscriminantImpl(val id: String) : Comparable<DiscriminantImpl>, Serializable {
    init {
        require(discriminantRegex.matches(id)) { "Invalid discriminant: '$id'" }
    }

    override fun toString(): String = id
    override fun compareTo(other: DiscriminantImpl): Int = id.compareTo(other.id)
}

/**
 * Synset type
 *
 * [n,v,a,r,s]
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

        fun fromChar(c: Char): SynsetType = fromCharOrNull(c) ?: throw IllegalArgumentException("Illegal SynsetType: $c")

        fun discriminantFromKey2(key2: Key2): Discriminant? {
            return if (key2.id.length > 1) Discriminant(key2.id.substring(1)) else null
        }

        val synsetTypeComparator: Comparator<SynsetType> = compareBy(SynsetType::value)
    }
}

/**
 * Part-of-Speech
 *
 * [n,v,a,r]
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

        fun fromKey2(key2: Key2): PartOfSpeech {
            if (key2.id.isEmpty()) throw IllegalArgumentException("Illegal SynsetType: $key2")
            return fromChar(key2.id[0])
        }

        fun discriminantFromKey2(key2: Key2): Discriminant? {
            return if (key2.id.length > 1) Discriminant(key2.id.substring(1)) else null
        }

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

/**
 * Example
 */
@kotlinx.serialization.Serializable
data class ExampleImpl(val text: String, val source: String? = null) : Serializable

/**
 * Relation implementation
 */
@kotlinx.serialization.Serializable
@JvmInline
value class RelationImpl(val id: String) : Comparable<RelationImpl>, Serializable {
    init {
        require(relationRegex.matches(id)) { "Invalid Relation: '$id'" }
    }

    override fun toString(): String = id
    override fun compareTo(other: RelationImpl): Int = id.compareTo(other.id)
}

/**
 * Pronunciation value implementation
 */
@kotlinx.serialization.Serializable
@JvmInline
value class PronunciationValueImpl(val ipa: String) : Comparable<PronunciationValueImpl>, Serializable {
    init {
        require(ipaRegex.matches(ipa)) { "Invalid IPA: '$ipa'" }
    }

    override fun toString(): String = ipa
    override fun compareTo(other: PronunciationValueImpl): Int = ipa.compareTo(other.ipa)
}