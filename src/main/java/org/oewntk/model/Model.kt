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
 * @property lexes                lexes
 * @property senses               senses
 * @property synsets              synsets
 * @property verbFrames           verb frames
 * @property verbTemplates        verb templates
 * @param    senseToVerbTemplates sensekey-to-verb template
 * @param    senseToTagCounts     sensekey-to-tagcount
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

    /**
     * Verb frames
     */
    val verbFrames: Collection<VerbFrame> = Collections.unmodifiableCollection(verbFrames)

    /**
     * Verb templates
     */
    private val verbTemplates: Collection<VerbTemplate> = Collections.unmodifiableCollection(verbTemplates)

    /**
     * Extra input directory
     */
    var source2: File? = null

    /**
     * Constructor
     */
    init {
        // set sense's verb templates
        val sensesById = sensesById
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
    val verbFramesById: Map<String, VerbFrame>
        get() = map(verbFrames) { it.id }

    /**
     * Verb templates mapped by id
     */
    val verbTemplatesById: Map<Int, VerbTemplate>
        get() = map(verbTemplates) { it.id }

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
