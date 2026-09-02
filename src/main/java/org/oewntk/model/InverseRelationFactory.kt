/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.Relation.Companion.INVERSE_SENSE_RELATIONS
import org.oewntk.model.Relation.Companion.INVERSE_SYNSET_RELATIONS

/**
 * Generator of inverse synset relations
 */
object InverseRelationFactory {

    private const val LOG_ALREADY_PRESENT = false

    private const val THROW_NON_EXISTING_TARGET = false

    val INVERSE_SYNSET_RELATIONS_MAP = INVERSE_SYNSET_RELATIONS.mapKeys { (k, _) -> Relation(k) }.mapValues { (_, v) -> Relation(v) }

    val INVERSE_SENSE_RELATIONS_MAP = INVERSE_SENSE_RELATIONS.mapKeys { (k, _) -> Relation(k) }.mapValues { (_, v) -> Relation(v) }

    val INVERSE_SYNSET_RELATIONS_SET = INVERSE_SYNSET_RELATIONS_MAP.values

    val INVERSE_SENSE_RELATIONS_SET = INVERSE_SENSE_RELATIONS_MAP.values

    /**
     * Generate inverse synset relations
     *
     * @param synsetsById synsets mapped by id
     * @param synsetsById synsets mapped by id
     * @return count
     */
    fun makeInverseSynsetRelations(synsetsById: Map<SynsetId, Synset>, sensesById: Map<SenseKey, Sense>): Int = makeInverseSynsetRelations(INVERSE_SYNSET_RELATIONS_MAP, synsetsById, sensesById)

    /**
     * Generate inverse synset relations as per map
     *
     * @param toInverse relation mapped to its inverse
     * @param synsetsById synsets mapped by id
     * @param sensesById senses mapped by id
     * @return count
     */
    fun makeInverseSynsetRelations(toInverse: Map<Relation, Relation>, synsetsById: Map<SynsetId, Synset>, sensesById: Map<SenseKey, Sense>): Int {
        var count = 0
        for ((sourceSynsetId, sourceSynset) in synsetsById) {
            if (!sourceSynset.relations.isNullOrEmpty()) {
                toInverse.keys.forEach {
                    val targetIds = sourceSynset.relations!![it]
                    if (!targetIds.isNullOrEmpty()) {
                        val inverseType = toInverse[it]
                        for (targetId in targetIds) {
                            if (targetId.targetsSynset) {
                                val targetSynset = synsetsById[targetId.synsetId]
                                if (targetSynset == null) {
                                    val message = "[E] non-existing target $targetId of synset relation $it($sourceSynsetId)"
                                    if (THROW_NON_EXISTING_TARGET) throw IllegalArgumentException(message) else {
                                        Tracing.psErr.println(message)
                                        continue
                                    }
                                }
                                try {
                                    targetSynset.addInverseRelation(inverseType!!, RelationTarget(sourceSynsetId.id))
                                    count++
                                } catch (e: IllegalArgumentException) {
                                    if (LOG_ALREADY_PRESENT) {
                                        Tracing.psErr.println("[W] ${e.message}\n")
                                    }
                                }
                            } else /* if (targetId.isSenseKey) */ {
                                val targetSense = sensesById[targetId.senseKey]
                                if (targetSense == null) {
                                    val message = "[E] non-existing target $targetId of synset relation $it($sourceSynsetId)"
                                    if (THROW_NON_EXISTING_TARGET) throw IllegalArgumentException(message) else {
                                        Tracing.psErr.println(message)
                                        continue
                                    }
                                }
                                try {
                                    targetSense.addInverseRelation(inverseType!!, RelationTarget(sourceSynsetId.id))
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
        }
        return count
    }

    /**
     * Generate inverse sense relations
     *
     * @param sensesById senses mapped by id
     * @param synsetsById synsets mapped by id
     * @return count
     */
    fun makeInverseSenseRelations(sensesById: Map<SenseKey, Sense>, synsetsById: Map<SynsetId, Synset>): Int = makeInverseSenseRelations(INVERSE_SENSE_RELATIONS_MAP, sensesById, synsetsById)

    /**
     * Generate inverse sense relations
     *
     * @param toInverse relation mapped to its inverse
     * @param sensesById senses mapped by id
     * @param synsetsById synsets mapped by id
     * @return count
     */
    fun makeInverseSenseRelations(toInverse: Map<Relation, Relation>, sensesById: Map<SenseKey, Sense>, synsetsById: Map<SynsetId, Synset>): Int {
        var count = 0
        for ((sourceSenseId, sourceSense) in sensesById) {
            if (!sourceSense.relations.isNullOrEmpty()) {
                toInverse.keys.forEach {
                    val targetIds = sourceSense.relations!![it]
                    if (!targetIds.isNullOrEmpty()) {
                        val inverseType = toInverse[it]!!
                        for (targetId in targetIds) {
                            if (targetId.targetsSynset) {
                                val targetSynset = synsetsById[targetId.synsetId]
                                if (targetSynset == null) {
                                    val message = "[E] non-existing target $targetId of sense relation $it($sourceSenseId)"
                                    if (THROW_NON_EXISTING_TARGET) throw IllegalArgumentException(message) else {
                                        Tracing.psErr.println(message)
                                        continue
                                    }
                                }
                                try {
                                    targetSynset.addInverseRelation(inverseType, RelationTarget(sourceSenseId.id))
                                    count++
                                } catch (e: IllegalArgumentException) {
                                    if (LOG_ALREADY_PRESENT) {
                                        Tracing.psErr.println("[W] ${e.message}\n")
                                    }
                                }
                            } else /* if (targetId.isSenseKey) */ {
                                val targetSense = sensesById[targetId.senseKey]
                                if (targetSense == null) {
                                    val message = "[E] non-existing target $targetId of sense relation $it($sourceSenseId)"
                                    if (THROW_NON_EXISTING_TARGET) throw IllegalArgumentException(message) else {
                                        Tracing.psErr.println(message)
                                        continue
                                    }
                                }
                                try {
                                    targetSense.addInverseRelation(inverseType, RelationTarget(sourceSenseId.id))
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
        }
        return count
    }
}
