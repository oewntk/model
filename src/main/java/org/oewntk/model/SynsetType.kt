package org.oewntk.model

import java.util.Comparator

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