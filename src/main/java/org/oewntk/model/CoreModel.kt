/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.MapFactory.sensesById
import org.oewntk.model.MapFactory.synsetsById
import java.io.File
import java.io.Serializable
import java.util.*

/**
 * Base language model
 *
 * @property lexes   lexical items
 * @property senses  senses
 * @property synsets synsets
 * @property source  source
 */
open class CoreModel(
    lexes: Collection<Lex>,
    senses: Collection<Sense>,
    synsets: Collection<Synset>,
) : Serializable {

    /**
     * Lexical items
     */
    val lexes: Collection<Lex> = Collections.unmodifiableCollection(lexes)

    /**
     * Senses
     */
    val senses: Collection<Sense> = Collections.unmodifiableCollection(senses)

    /**
     * Synsets
     */
    val synsets: Collection<Synset> = Collections.unmodifiableCollection(synsets)

    /**
     * Source
     * Input directory
     */
    var source: File? = null

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
     * Cached
     * Lexical units mapped by lemma written form.
     * A multimap: each value is an array of lexes for the lemma.
     * Emulates (@Transient not allowed for properties with delegates) :
     * @Transient
     * val lexesByLemma: Map<String, Collection<Lex>> by lazy { LexGroupings.lexesByLemma(lexes) }
     */
    @Transient
    var lexesByLemma: Map<String, Collection<Lex>>? = null
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
     * val lexesByLCLemma: Map<String, Collection<Lex>> by lazy { LexGroupings.lexesByLCLemma(lexes) }
     */
    @Transient
    var lexesByLCLemma: Map<String, Collection<Lex>>? = null
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
     * val sensesById2: Map<String, Sense> by lazy { sensesById(senses) }
     */
    @Transient
    var sensesById: Map<String, Sense>? = null
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
     * val synsetsById: Map<String, Synset> by lazy { synsetsById(synsets) }
     */
    @Transient
    var synsetsById: Map<String, Synset>? = null
        get() {
            if (field == null) {
                field = synsetsById(synsets)
            }
            return field
        }
        private set

    // val lexesByLCLemma: Map<String, Collection<Lex>> by lazy { LexGroupings.lexesByLCLemma(lexes) }
    // val lexesByLemma: Map<String, Collection<Lex>> by lazy { LexGroupings.lexesByLemma(lexes) }
    // val sensesById: Map<String, Sense> by lazy { sensesById(senses) }
    // val synsetsById: Map<String, Synset> by lazy { synsetsById(synsets) }

    /**
     * Info about this model
     *
     * @return info
     */
    open fun info(): String? {
        return String.format("lexes: %d, senses: %d, synsets: %s", lexes.size, senses.size, synsets.size)
    }
}
