/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

/**
 * Generator of inverse synset relations
 */
object InverseRelationFactory {

    private const val LOG_ALREADY_PRESENT = false

    private val INVERSE_SYNSET_RELATIONS = mapOf(
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

    private val INVERSE_SYNSET_RELATIONS_KEYS = INVERSE_SYNSET_RELATIONS.keys

    private val INVERSE_SENSE_RELATIONS = mapOf(
        "exemplifies" to "is_exemplified_by",
        "domain_topic" to "has_domain_topic",
        "domain_region" to "has_domain_region",
    )

    private val INVERSE_SENSE_RELATIONS_KEYS: Set<String> = INVERSE_SENSE_RELATIONS.keys

    /**
     * Generate inverse synset relations
     *
     * @param synsetsById synsets mapped by id
     * @return count
     */
    fun makeSynsetRelations(synsetsById: Map<String, Synset>): Int {
        var count = 0
        for ((sourceSynsetId, sourceSynset) in synsetsById) {
            val relations: Map<String, Set<String>>? = sourceSynset.relations
            if (!relations.isNullOrEmpty()) {
                for (type in INVERSE_SYNSET_RELATIONS_KEYS) {
                    val targetSynsetIds: Collection<String>? = relations[type]
                    if (!targetSynsetIds.isNullOrEmpty()) {
                        val inverseType = INVERSE_SYNSET_RELATIONS[type]
                        for (targetSynsetId in targetSynsetIds) {
                            val targetSynset = checkNotNull(synsetsById[targetSynsetId])
                            try {
                                targetSynset.addInverseRelation(inverseType!!, sourceSynsetId)
                                count++
                            } catch (e: IllegalArgumentException) {
                                if (LOG_ALREADY_PRESENT) {
                                    Tracing.psErr.printf("[W] %s%n", e.message)
                                }
                            }
                        }
                    }
                }
            }
        }
        return count
    }

    /**
     * Generate inverse sense relations
     *
     * @param sensesById senses mapped by id
     * @return count
     */
    fun makeSenseRelations(sensesById: Map<String, Sense>): Int {
        var count = 0
        for ((sourceSenseId, sourceSense) in sensesById) {
            val relations: Map<String, Set<String>>? = sourceSense.relations
            if (!relations.isNullOrEmpty()) {
                for (type in INVERSE_SENSE_RELATIONS_KEYS) {
                    val targetSenseIds: Collection<String>? = relations[type]
                    if (!targetSenseIds.isNullOrEmpty()) {
                        val inverseType = INVERSE_SENSE_RELATIONS[type]
                        for (targetSenseId in targetSenseIds) {
                            val targetSense = checkNotNull(sensesById[targetSenseId])
                            try {
                                targetSense.addInverseRelation(inverseType!!, sourceSenseId)
                                count++
                            } catch (e: IllegalArgumentException) {
                                if (LOG_ALREADY_PRESENT) {
                                    Tracing.psErr.printf("[W] %s%n", e.message)
                                }
                            }
                        }
                    }
                }
            }
        }
        return count
    }
}
