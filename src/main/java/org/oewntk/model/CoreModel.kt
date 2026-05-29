/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.Lex.Groups.groupByLemma
import org.oewntk.model.Lex.Groups.groupByLCLemma
import org.oewntk.model.Lex.Groups.lexByLemmaThenByKey2
import org.oewntk.model.MapFactory.sensesById
import org.oewntk.model.MapFactory.synsetsById
import java.io.Serializable
import java.util.*

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
 * @property lexesByLemma   transient lexical items mapped by (case-sensitive) lemma written form
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
     * Lex entries
     */
    val lexEntries: Sequence<LexEntry>
        get() = lexesByLemma!!.asSequence()

    // B Y   C A C H E S

    /**
     * Cached
     * Lexical units mapped by lemma written form.
     * A multimap: each value is an array of lexes for the lemma.
     * Emulates (@Transient not allowed for properties with delegates) :
     * @Transient
     */
    @Transient
    var lexesByLemma: Map<Lemma, Collection<Lex>>? = null
        get() {
            if (field == null) {
                field = lexes.asSequence().groupByLemma()
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
     */
    @Transient
    var lexesByLCLemma: Map<LowerCasedLemma, Collection<Lex>>? = null
        get() {
            if (field == null) {
                field = lexes.asSequence().groupByLCLemma()
            }
            return field
        }
        private set

    /**
     * Cached
     * Senses mapped by id (sensekey)
     * Emulates (@Transient not allowed for properties with delegates) :
     * @Transient
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

    // L E X

    val hyperMap
        get() = lexes.asSequence().lexByLemmaThenByKey2()

    /**
     * Lex finder (nullable result)
     * Resolution may yield null
     */
    val lexFinder: (Lemma) -> Collection<Lex>?
        get() = { lexesByLemma!![it] }

    /**
     * Lex finder ignoring lemma case (nullable result)
     * Resolution may yield null
     */
    val lexIgnoreCaseFinder: (Lemma) -> Collection<Lex>?
        get() = { lexesByLCLemma!![it.lowercase(Locale.ENGLISH)] }

    /**
     * Lex resolver
     * Assumes non-null resolution, otherwise an exception is thrown
     */
    val lexResolver: (Lemma) -> Collection<Lex>
        get() = { lexFinder(it)!! }

    /**
     * Lex resolver ignoring lemma case
     * Assumes non-null resolution, otherwise an exception is thrown
     */
    val lexIgnoreCaseResolver: (Lemma) -> Collection<Lex>
        get() = { lexFinder(it)!! }

    // S E N S E

    /**
     * Sense finder (nullable result)
     * Resolution may yield null
     */
    val senseFinder: (SenseKey) -> Sense?
        get() = { sensesById!![it] }

    /**
     * Sense resolver
     * Assumes non-null resolution, otherwise an exception is thrown
     */
    val senseResolver: (SenseKey) -> Sense
        get() = { senseFinder(it)!! }

    // S E N S E

    /**
     * Synset finder (nullable result)
     * Resolution may yield null
     */
    val synsetFinder: (SynsetId) -> Synset?
        get() = { synsetsById!![it] }

    /**
     * Synset resolver
     * Assumes non-null resolution, otherwise an exception is thrown
     */
    val synsetResolver: (SynsetId) -> Synset
        get() = { synsetFinder(it)!! }

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

}
