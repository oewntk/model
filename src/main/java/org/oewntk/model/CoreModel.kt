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
 * @property source         source, typically input directory
 */
@kotlinx.serialization.Serializable
sealed class BaseModel : Serializable {

    abstract val lexes: Collection<Lex>
    abstract val senses: Collection<Sense>
    abstract val synsets: Collection<Synset>
    abstract var source: String?
}

@kotlinx.serialization.Serializable
data class DataCoreModel(
    override val lexes: Collection<Lex>,
    override val senses: Collection<Sense>,
    override val synsets: Collection<Synset>,
    override var source: String? = null,

    ) : BaseModel(), Serializable {

    constructor (
        model: CoreModel,
    ) : this(model.lexes, model.senses, model.synsets, model.source)
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
    override var source: String? = null,

    ) : BaseModel(), Serializable {

    /**
     * Constructor from data
     *
     * @param data data core model
     */
    constructor (
        data: DataCoreModel,
    ) : this(data.lexes, data.senses, data.synsets, data.source)

    /**
     * Cached
     * Lexical units mapped by lemma written form.
     * A multimap: each value is an array of lexes for the lemma.
     * Emulates (@Transient not allowed for properties with delegates) :
     * @Transient
     * val lexesByLemma: Map<LemmaType, Collection<Lex>> by lazy { LexGroupings.lexesByLemma(lexes) }
     */
    @Transient
    var lexesByLemma: Map<VerbFrameId, Collection<Lex>>? = null
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
    var lexesByLCLemma: Map<LemmaType, Collection<Lex>>? = null
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
        InverseRelationFactory.makeSynsetRelations(synsetsById!!)
        InverseRelationFactory.makeSenseRelations(sensesById!!)
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
