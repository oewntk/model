package org.oewntk.model

import java.io.Serializable

/**
 * Relation
 */
@kotlinx.serialization.Serializable
@JvmInline
value class Relation(val id: String) : Comparable<Relation>, Serializable {
    init {
        require(relationRegex.matches(id)) { "Invalid Relation: '$id'" }
    }

    override fun toString(): String = id
    override fun compareTo(other: Relation): Int = id.compareTo(other.id)

    companion object {

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

        val INVERSE_SYNSET_RELATIONS = mapOf(
            "hypernym" to "hyponym",
            "instance_hypernym" to "instance_hyponym",
            "mero_part" to "holo_part",
            "mero_member" to "holo_member",
            "mero_substance" to "holo_substance",
            "causes" to "is_caused_by",
            "entails" to "is_entailed_by",
            "exemplifies" to "is_exemplified_by",
            "domain_topic" to "has_domain_topic",
            "domain_region" to "has_domain_region",
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

        /**
         * Keys are present in the source file(s)
         * Values are inverse relations and can be built in the word net
         */
        val INVERSE_SENSE_RELATIONS = mapOf(
            "exemplifies" to "is_exemplified_by",
            "domain_topic" to "has_domain_topic",
            "domain_region" to "has_domain_region",
        )


        val RELATION_RE = (SYNSET_RELATIONS + SENSE_RELATIONS)
            .distinct()
            .joinToString(separator = "|", prefix = "(", postfix = ")")

        val relationRegex = "^$RELATION_RE$".toRegex()

        fun String.isRelation(): Boolean = relationRegex.matches(this)

    }
}