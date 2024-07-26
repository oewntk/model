/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.MapFactory.map
import java.io.Serializable

@kotlinx.serialization.Serializable
data class DataModel(
    override val lexes: Collection<Lex>,
    override val senses: Collection<Sense>,
    override val synsets: Collection<Synset>,
    val verbFrames: Collection<VerbFrame>,
    val verbTemplates: Collection<VerbTemplate>,

    ) : BaseModel(), Serializable {

    constructor (
        model: Model,
    ) : this(model.lexes, model.senses, model.synsets, model.verbFrames, model.verbTemplates)
}

/**
 * Language model
 *
 * @param  lexes                  lexes
 * @param  senses                 senses
 * @param  synsets                synsets
 * @param  verbFrames             verb frames
 * @param  verbTemplates          verb templates
 *
 * @property lexes                lexes (inherited, unmodifiable)
 * @property senses               senses (inherited, unmodifiable)
 * @property synsets              synsets (inherited, unmodifiable)
 * @property source               source, typically input directory (inherited)
 * @property lexesByLemma         transient lexical items mapped by lemma written form (inherited)
 * @property lexesByLCLemma       transient lexical items mapped by lower-cased lemma written form (inherited)
 * @property sensesById           transient senses mapped by sense id (sensekey) (inherited)
 * @property synsetsById          transient synsets mapped by synset id (inherited)

 * @property verbFrames           verb frames (unmodifiable)
 * @property verbTemplates        verb templates (unmodifiable)
 * @property source2              source2
 */
class Model private constructor(
    override val lexes: Collection<Lex>,
    override val senses: Collection<Sense>,
    override val synsets: Collection<Synset>,
    val verbFrames: Collection<VerbFrame>,
    val verbTemplates: Collection<VerbTemplate>,
) : CoreModel(lexes, senses, synsets) {

    var source2: String? = null

    /**
     * Language model
     *
     * @param  lexes                  lexes
     * @param  senses                 senses
     * @param  synsets                synsets
     * @param  verbFrames             verb frames
     * @param  verbTemplates          verb templates
     * @param  senseToVerbTemplates   sensekey-to-verb template
     * @param  senseToTagCounts       sensekey-to-tagcount
     */
    constructor(
        lexes: Collection<Lex>,
        senses: Collection<Sense>,
        synsets: Collection<Synset>,
        verbFrames: Collection<VerbFrame>,
        verbTemplates: Collection<VerbTemplate>,
        senseToVerbTemplates: Collection<Pair<SenseKey, Array<VerbTemplateId>>>,
        senseToTagCounts: Collection<Pair<SenseKey, TagCount>>,
    ) : this(lexes, senses, synsets, verbFrames, verbTemplates) {

        // set sense's verb templates
        for ((sensekey, templatesIds) in senseToVerbTemplates) {
            val sense = sensesById?.get(sensekey)
            sense?.verbTemplates = templatesIds
        }

        // set sense's tag counts
        for ((sensekey, tagCount) in senseToTagCounts) {
            val sense = sensesById?.get(sensekey)
            sense?.tagCount = tagCount
        }
    }

    /**
     * Constructor from data
     *
     * @param data data core model
     */
    constructor (
        data: DataModel,
        source0: String,
        source20: String,
    ) : this(data.lexes, data.senses, data.synsets, data.verbFrames, data.verbTemplates) {
        source = source0
        source2 = source20
    }

    /**
     * Constructor from base model
     *
     * @param coreModel             base model
     * @param verbFrames            verb frames
     * @param verbTemplates         verb templates
     * @param sensesToVerbTemplates collection of entries of type sensekey-to-verb template
     * @param sensesToTagCounts     collection of entries of type sensekey-to-tagcount
     */
    constructor(
        coreModel: CoreModel,
        verbFrames: Collection<VerbFrame>,
        verbTemplates: Collection<VerbTemplate>,
        sensesToVerbTemplates: Collection<Pair<SenseKey, Array<VerbTemplateId>>>,
        sensesToTagCounts: Collection<Pair<SenseKey, TagCount>>,
    ) : this(
        coreModel.lexes,
        coreModel.senses,
        coreModel.synsets,
        verbFrames,
        verbTemplates,
        sensesToVerbTemplates,
        sensesToTagCounts
    )

    /**
     * Verb frames mapped by id
     */
    @Transient
    var verbFramesById: Map<VerbFrameId, VerbFrame>? = null
        get() {
            if (field == null) {
                field = map(verbFrames) { it.id }
            }
            return field
        }
        private set

    /**
     * Verb templates mapped by id
     */
    @Transient
    var verbTemplatesById: Map<VerbTemplateId, VerbTemplate>? = null
        get() {
            if (field == null) {
                field = map(verbTemplates) { it.id }
            }
            return field
        }
        private set

    /**
     * Input sources
     */
    val sources: Array<String?>
        get() = arrayOf(source, source2)

    /**
     * Info about this model
     *
     * @return info
     */
    override fun info(): String {
        return super.info() + ", verb frames: ${verbFrames.size}, verb templates: ${verbTemplates.size}"
    }
}
