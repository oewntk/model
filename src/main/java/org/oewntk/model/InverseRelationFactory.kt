/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

/**
 * Generator of inverse synset relations
 */
object InverseRelationFactory {

    private const val LOG_ALREADY_PRESENT = false

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

    val INVERSE_SENSE_RELATIONS = mapOf(
        "exemplifies" to "is_exemplified_by",
        "domain_topic" to "has_domain_topic",
        "domain_region" to "has_domain_region",
     )

    /**
     * Generate inverse synset relations
     *
     * @param synsetsById synsets mapped by id
     * @return count
     */
    fun makeInverseSynsetRelations(synsetsById: Map<SynsetId, Synset>): Int {
        return makeInverseSynsetRelations(INVERSE_SYNSET_RELATIONS, synsetsById)
    }

    /**
     * Generate inverse synset relations as per map
     *
     * @param toInverse relation mapped to its inverse
     * @param synsetsById synsets mapped by id
     * @return count
     */
    fun makeInverseSynsetRelations(toInverse: Map<Relation, Relation>, synsetsById: Map<SynsetId, Synset>): Int {
        var count = 0
        for ((sourceSynsetId, sourceSynset) in synsetsById) {
            if (!sourceSynset.relations.isNullOrEmpty()) {
                toInverse.keys.forEach {
                    val targetSynsetIds = sourceSynset.relations!![it]
                    if (!targetSynsetIds.isNullOrEmpty()) {
                        val inverseType = toInverse[it]
                        for (targetSynsetId in targetSynsetIds) {
                            val targetSynset = checkNotNull(synsetsById[targetSynsetId])
                            try {
                                targetSynset.addInverseRelation(inverseType!!, sourceSynsetId)
                                count++
                            } catch (e: IllegalArgumentException) {
                                if (LOG_ALREADY_PRESENT) {
                                    Tracing.psErr.println("[W] ${e.message}\n")
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
    fun makeInverseSenseRelations(sensesById: Map<SenseKey, Sense>): Int {
        return makeInverseSenseRelations(INVERSE_SENSE_RELATIONS, sensesById)
    }

    /**
     * Generate inverse sense relations
     *
     * @param toInverse relation mapped to its inverse
     * @param sensesById senses mapped by id
     * @return count
     */
    fun makeInverseSenseRelations(toInverse: Map<Relation, Relation>, sensesById: Map<SenseKey, Sense>): Int {
        var count = 0
        for ((sourceSenseId, sourceSense) in sensesById) {
            if (!sourceSense.relations.isNullOrEmpty()) {
                toInverse.keys.forEach {
                    val targetSenseIds = sourceSense.relations!![it]
                    if (!targetSenseIds.isNullOrEmpty()) {
                        val inverseType = toInverse[it]!!
                        for (targetSenseId in targetSenseIds) {
                            val targetSense = checkNotNull(sensesById[targetSenseId])
                            try {
                                targetSense.addInverseRelation(inverseType, sourceSenseId)
                                count++
                            } catch (e: IllegalArgumentException) {
                                if (LOG_ALREADY_PRESENT) {
                                    Tracing.psErr.println("[W] ${e.message}\n")
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
