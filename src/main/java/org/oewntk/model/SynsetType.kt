package org.oewntk.model

import java.util.Comparator

/**
 * Synset type
 *
 * [n,v,a,r,s]
 */
enum class SynsetType(val value: Char) {
    N('n') {
        override fun toPartOfSpeech(): PartOfSpeech = PartOfSpeech.N
        override fun toCategory(): Category = Category.N
    },
    V('v') {
        override fun toPartOfSpeech(): PartOfSpeech = PartOfSpeech.V
        override fun toCategory(): Category = Category.V
    },
    A('a') {
        override fun toPartOfSpeech(): PartOfSpeech = PartOfSpeech.A
        override fun toCategory(): Category = Category.A
    },
    R('r') {
        override fun toPartOfSpeech(): PartOfSpeech = PartOfSpeech.R
        override fun toCategory(): Category = Category.R
    },
    S('s') {
        override fun toPartOfSpeech(): PartOfSpeech = PartOfSpeech.A
        override fun toCategory(): Category = Category.S
    };

    abstract fun toPartOfSpeech(): PartOfSpeech
    abstract fun toCategory(): Category

    override fun toString(): String {
        throw IllegalAccessException("Illegal: use .value ${this.value}")
    }

    companion object {
        fun fromCharOrNull(c: Char): SynsetType? {
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