package org.oewntk.model

import java.util.Comparator

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