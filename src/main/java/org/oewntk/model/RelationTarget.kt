package org.oewntk.model

import org.oewntk.model.SenseKey.Companion.isSenseKey
import org.oewntk.model.SynsetId.Companion.isSynsetId
import java.io.Serializable


/**
 * SenseKey
 */
@kotlinx.serialization.Serializable
data class RelationTarget(val id: String) : Comparable<RelationTarget>, Serializable {

    val targetsSynset: Boolean by lazy { id.isSynsetId() }

    init {
        require(targetsSynset || id.isSenseKey()) { "Invalid relation target: '$id'" }
    }

    override fun toString(): String = id
    override fun compareTo(other: RelationTarget): Int = id.compareTo(other.id)

    val synsetId
        get() = SynsetId(id)

    val senseKey
        get() = SenseKey(id)

    val isSynsetId
        get() = id.isSynsetId()

    val isSenseKey
        get() = id.isSenseKey()

    companion object {

        fun String.isRelationTarget(): Boolean = isSynsetId() || isSenseKey()

    }
}