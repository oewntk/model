package org.oewntk.model

import java.io.Serializable

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

    companion object{

        val SYNSET_RELATIONS = arrayOf(
            "hypernym", "hyponym",
            "instance_hypernym", "instance_hyponym",
            "mero_part", "holo_part",
            "mero_member", "holo_member",
            "mero_substance", "holo_substance",
            "causes", "is_caused_by",
            "entails", "is_entailed_by",
            "exemplifies", "is_exemplified_by",
            "domain_topic", "has_domain_topic",
            "domain_region", "has_domain_region",
            "attribute",
            "similar",
            "also",
            "verb_group", // wn31 (now similar)
        )

        val SENSE_RELATIONS = arrayOf(
            "antonym",
            "similar",
            "exemplifies", "is_exemplified_by",
            "derivation",
            "pertainym",
            "participle",
            "also",
            "domain_region", "has_domain_region",
            "domain_topic", "has_domain_topic",
            "agent",
            "material",
            "event",
            "instrument",
            "location",
            "by_means_of",
            "undergoer",
            "property",
            "result",
            "state",
            "uses",
            "destination",
            "body_part",
            "vehicle",
            "collocation",
            "other"
        )

        val RELATION_RE = (SYNSET_RELATIONS + SENSE_RELATIONS)
            .distinct()
            .joinToString(separator = "|", prefix = "(", postfix = ")")

        val relationRegex = "^$RELATION_RE$".toRegex()

        fun String.isRelation(): Boolean = relationRegex.matches(this)

    }
}