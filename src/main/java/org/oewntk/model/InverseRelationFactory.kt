/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.Sense.Companion.INVERSE_SENSE_RELATIONS
import org.oewntk.model.Synset.Companion.INVERSE_SYNSET_RELATIONS

/**
 * Generator of inverse synset relations
 */
object InverseRelationFactory {

    private const val LOG_ALREADY_PRESENT = false

    val INVERSE_SYNSET_RELATIONS_SET = INVERSE_SYNSET_RELATIONS.values.toSet()

    val INVERSE_SENSE_RELATIONS_SET = INVERSE_SENSE_RELATIONS.values.toSet()

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
                            val targetSynset =
                                checkNotNull(synsetsById[targetSynsetId]) { Tracing.psErr.println("[E] non-existing target $targetSynsetId of synset relation $it($sourceSynsetId)") }
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
                            val targetSense =
                                checkNotNull(sensesById[targetSenseId]) { Tracing.psErr.println("[E] non-existing target $targetSenseId of synset relation $it($sourceSenseId)") }
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
