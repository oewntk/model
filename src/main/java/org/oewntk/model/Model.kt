/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.MapFactory.map
import java.io.File
import java.util.*

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
class Model(
    lexes: Collection<Lex>,
    senses: Collection<Sense>,
    synsets: Collection<Synset>,
    verbFrames: Collection<VerbFrame>,
    verbTemplates: Collection<VerbTemplate>,
    senseToVerbTemplates: Collection<Pair<SenseKey, Array<VerbTemplateType>>>,
    senseToTagCounts: Collection<Pair<SenseKey, TagCount>>,
) : CoreModel(lexes, senses, synsets) {

    val verbFrames: Collection<VerbFrame> = Collections.unmodifiableCollection(verbFrames)
    private val verbTemplates: Collection<VerbTemplate> = Collections.unmodifiableCollection(verbTemplates)
    var source2: File? = null

    /**
     * Init
     */
    init {
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
        sensesToVerbTemplates: Collection<Pair<SenseKey, Array<VerbTemplateType>>>,
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
    var verbFramesById: Map<String, VerbFrame>? = null
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
    var verbTemplatesById: Map<Int, VerbTemplate>? = null
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
    val sources: Array<File?>
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
