/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import java.io.Serializable
import java.util.*

/**
 * Pronunciation
 *
 * @property value   value in IPA
 * @property variety variety
 */
@kotlinx.serialization.Serializable
data class Pronunciation(
    val value: PronunciationValue,
    val variety: PronunciationVariety?,
) : Comparable<Pronunciation>, Serializable {

    // identify

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as Pronunciation
        return value == that.value && variety == that.variety
    }

    override fun hashCode(): Int {
        return Objects.hash(value, variety)
    }

    // stringify

    override fun toString(): String {
        return if (variety != null) "[$variety] /$value/" else "/$value/"
    }

    // order

    override fun compareTo(other: Pronunciation): Int {
        return comparator.compare(this, other)
    }

    companion object {

        @Transient
        val comparator: Comparator<Pronunciation> = compareBy(Pronunciation::variety).thenBy(Pronunciation::value)

        @Suppress("unused")
        @Transient
        val comparatorNull: Comparator<Pronunciation?> = nullsFirst(comparator)

        fun ipa(value: PronunciationValue): Pronunciation {
            return Pronunciation(value, null)
        }

        fun ipa(value: PronunciationValue, variety: PronunciationVariety?): Pronunciation {
            return Pronunciation(value, variety)
        }
    }
}
