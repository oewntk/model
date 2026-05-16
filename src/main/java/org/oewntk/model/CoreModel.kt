/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.MapFactory.sensesById
import org.oewntk.model.MapFactory.synsetsById
import java.io.Serializable

/**
 * Sealed Base language model
 *
 * @property lexes          lexical items (unmodifiable)
 * @property senses         senses (unmodifiable)
 * @property synsets        synsets (unmodifiable)
 */
@kotlinx.serialization.Serializable
sealed class BaseModel : Serializable {

    abstract val lexes: Collection<Lex>
    abstract val senses: Collection<Sense>
    abstract val synsets: Collection<Synset>
}

@kotlinx.serialization.Serializable
data class DataCoreModel(
    override val lexes: Collection<Lex>,
    override val senses: Collection<Sense>,
    override val synsets: Collection<Synset>,

    ) : BaseModel(), Serializable {

    constructor (
        model: CoreModel,
    ) : this(model.lexes, model.senses, model.synsets)
}

/**
 * Base language model
 *
 * @property lexes          lexical items (unmodifiable)
 * @property senses         senses (unmodifiable)
 * @property synsets        synsets (unmodifiable)
 * @property source         source, typically input directory
 * @property lexesByLemma   transient lexical items mapped by lemma written form
 * @property lexesByLCLemma transient lexical items mapped by lower-cased lemma written form
 * @property sensesById     transient senses mapped by sense id (sensekey)
 * @property synsetsById    transient synsets mapped by synset id
 */
@kotlinx.serialization.Serializable
open class CoreModel(
    override val lexes: Collection<Lex>,
    override val senses: Collection<Sense>,
    override val synsets: Collection<Synset>,
    var source: String? = null,

    ) : BaseModel(), Serializable {

    /**
     * Constructor from data
     *
     * @param data data core model
     */
    constructor (
        data: DataCoreModel,
    ) : this(data.lexes, data.senses, data.synsets)

    /**
     * Cached
     * Lexical units mapped by lemma written form.
     * A multimap: each value is an array of lexes for the lemma.
     * Emulates (@Transient not allowed for properties with delegates) :
     * @Transient
     * val lexesByLemma: Map<LemmaType, Collection<Lex>> by lazy { LexGroupings.lexesByLemma(lexes) }
     */
    @Transient
    var lexesByLemma: Map<Lemma, Collection<Lex>>? = null
        get() {
            if (field == null) {
                field = LexGroupings.lexesByLemma(lexes)
            }
            return field
        }
        private set

    /**
     * Cached
     * Lexical units mapped by lemma lower-cased written form.
     * A multimap: each value is an array of lexes for the lemma.
     * Emulates (@Transient not allowed for properties with delegates) :
     * @Transient
     * val lexesByLCLemma: Map<LemmaType, Collection<Lex>> by lazy { LexGroupings.lexesByLCLemma(lexes) }
     */
    @Transient
    var lexesByLCLemma: Map<Lemma, Collection<Lex>>? = null
        get() {
            if (field == null) {
                field = LexGroupings.lexesByLCLemma(lexes)
            }
            return field
        }
        private set

    /**
     * Cached
     * Senses mapped by id (sensekey)
     * Emulates (@Transient not allowed for properties with delegates) :
     * @Transient
     * val sensesById2: Map<SenseKey, Sense> by lazy { sensesById(senses) }
     */
    @Transient
    var sensesById: Map<SenseKey, Sense>? = null
        get() {
            if (field == null) {
                field = sensesById(senses)
            }
            return field
        }
        private set

    /**
     * Cached
     * Synsets mapped by id (synset id)
     * Emulates (@Transient not allowed for properties with delegates) :
     * @Transient
     * val synsetsById: Map<SynsetId, Synset> by lazy { synsetsById(synsets) }
     */
    @Transient
    var synsetsById: Map<SynsetId, Synset>? = null
        get() {
            if (field == null) {
                field = synsetsById(synsets)
            }
            return field
        }
        private set

    // val lexesByLCLemma: Map<LemmaType, Collection<Lex>> by lazy { LexGroupings.lexesByLCLemma(lexes) }
    // val lexesByLemma: Map<LemmaType, Collection<Lex>> by lazy { LexGroupings.lexesByLemma(lexes) }
    // val sensesById: Map<SenseKey, Sense> by lazy { sensesById(senses) }
    // val synsetsById: Map<SynsetId, Synset> by lazy { synsetsById(synsets) }

    /**
     * Generate inverse relations
     *
     * @return this model
     */
    fun generateInverseRelations(): CoreModel {
        InverseRelationFactory.makeInverseSynsetRelations(synsetsById!!)
        InverseRelationFactory.makeInverseSenseRelations(sensesById!!)
        return this
    }

    /**
     * Generate inverse relations
     * @param toSynsetRelationInverse synset relation mapped to its inverse
     * @param toSenseRelationInverse sense relation mapped to its inverse
     *
     * @return this model
     */
    fun generateInverseRelations(
        toSynsetRelationInverse: Map<Relation, Relation>,
        toSenseRelationInverse: Map<Relation, Relation>
    ): CoreModel {
        InverseRelationFactory.makeInverseSynsetRelations(toSynsetRelationInverse, synsetsById!!)
        InverseRelationFactory.makeInverseSenseRelations(toSenseRelationInverse, sensesById!!)
        return this
    }

    /**
     * Info about this model
     *
     * @return info
     */
    open fun info(): String? {
        return "lexes: ${lexes.size}, senses: ${senses.size}, synsets: ${synsets.size}"
    }

    /**
     * Check model
     */
    fun check(): CoreModel {
        checkMembers()
        checkSynsetRelationTargets()
        checkSenseRelationTargets()
        return this
    }

    /**
     * Check synset relation targets
     */
    fun checkSynsetRelationTargets(): CoreModel {
        if (synsetsById != null) {
            for ((sourceSynsetId, sourceSynset) in synsetsById) {
                if (!sourceSynset.relations.isNullOrEmpty()) {
                    sourceSynset.relations!!.forEach { (rel, targetSynsetIds) ->
                        if (targetSynsetIds.isNotEmpty()) {
                            for (targetSynsetId in targetSynsetIds) {
                                if (targetSynsetId[0] != 'Q' && synsetsById!![targetSynsetId] == null) Tracing.psErr.println("[E] non-existing target $targetSynsetId of synset relation $rel($sourceSynsetId)")
                            }
                        } else Tracing.psErr.println("[E] no target of synset relation $rel($sourceSynsetId)")
                    }
                }
            }
        }
        return this
    }

    /**
     * Check sense relation targets
     */
    fun checkSenseRelationTargets(): CoreModel {
        if (sensesById != null) {
            for ((sourceSenseId, sourceSense) in sensesById) {
                if (!sourceSense.relations.isNullOrEmpty()) {
                    sourceSense.relations!!.forEach { (rel, targetSynsetIds) ->
                        if (targetSynsetIds.isNotEmpty()) {
                            for (targetSynsetId in targetSynsetIds) {
                                if (sensesById!![targetSynsetId] == null) Tracing.psErr.println("[E] non-existing target $targetSynsetId of sense relation $rel($sourceSenseId)")
                            }
                        } else Tracing.psErr.println("[E] no target of sense relation $rel($sourceSenseId)")
                    }
                }
            }
        }
        return this
    }

    /**
     * Check synset members
     */
    fun checkMembers(verbose: Boolean = true): CoreModel {
        checkMembersDuplicates(verbose)
        checkMembersReference(verbose)
        return this
    }

    /**
     * Check synset members
     */
    fun checkMembersDuplicates(verbose: Boolean = true): CoreModel {
        var count = 0
        for (synset in synsets) {
            val duplicates = synset.members.groupBy { it }
                .filter { it.value.size > 1 }
                .keys
            if (duplicates.isNotEmpty()) {
                count++
                if (verbose) Tracing.psErr.println("[E] duplicate synset ${synset.synsetId} members: $duplicates {${synset.members.joinToString()}}")
            }
        }
        Tracing.psErr.println("[${if (count == 0) "I" else "E"}] $count synsets have member duplicate(s)")
        return this
    }

    /**
     * Check synset members
     */
    fun checkMembersReference(verbose: Boolean = true): CoreModel {
        var count = 0
        for (synset in synsets) {
            val orphans = synset.members.filter { lexesByLemma!![it] == null }.toList()
            if (orphans.isNotEmpty()) {
                count++
                if (verbose) Tracing.psErr.println("[E] members of synset ${synset.synsetId} with members {${synset.members.joinToString()}} have no entry: $orphans")
            }
        }
        Tracing.psErr.println("[${if (count == 0) "I" else "E"}] $count synsets have member(s) without entries")
        return this
    }
}
