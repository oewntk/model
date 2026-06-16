/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.MapFactory.map
import java.io.Serializable
import java.lang.System.identityHashCode
import java.util.*

@kotlinx.serialization.Serializable
@kotlinx.serialization.SerialName("data_model")
data class DataModel(
    override val lexes: List<Lex>,
    override val senses: List<Sense>,
    override val synsets: List<Synset>,
    val verbFrames: List<VerbFrame>,
    val verbTemplates: List<VerbTemplate>,

    ) : BaseModel(), Serializable {

    constructor (
        model: Model,
    ) : this(model.lexes, model.senses, model.synsets, model.verbFrames, model.verbTemplates)

    constructor (
        coreModel: CoreModel,
        verbFrames: List<VerbFrame>,
        verbTemplates: List<VerbTemplate>,
    ) : this(coreModel.lexes, coreModel.senses, coreModel.synsets, verbFrames, verbTemplates)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return if (other is DataModel)
            lexes == other.lexes
                    && senses == other.senses
                    && synsets == other.synsets
                    && verbFrames == other.verbFrames
                    && verbTemplates == other.verbTemplates
        else false
    }

    override fun hashCode(): Int {
        return Objects.hash(lexes, senses, synsets, verbFrames, verbTemplates)
    }

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
class Model(
    lexes: List<Lex>,
    senses: List<Sense>,
    synsets: List<Synset>,
    val verbFrames: List<VerbFrame>,
    val verbTemplates: List<VerbTemplate>,
) : CoreModel(lexes, senses, synsets) {

    var source2: String? = null

    /**
     * Private constructor that injects template and data count into senses
     *
     * @param  lexes                  lexes
     * @param  senses                 senses
     * @param  synsets                synsets
     * @param  verbFrames             verb frames
     * @param  verbTemplates          verb templates
     * @param  senseToVerbTemplates   sensekey-to-verb template
     * @param  senseToTagCounts       sensekey-to-tagcount
     */
    private constructor(
        lexes: List<Lex>,
        senses: List<Sense>,
        synsets: List<Synset>,
        verbFrames: List<VerbFrame>,
        verbTemplates: List<VerbTemplate>,
        senseToVerbTemplates: Collection<Pair<SenseKey, List<VerbTemplateId>>>?,
        senseToTagCounts: Collection<Pair<SenseKey, TagCount>>?,
    ) : this(lexes, senses, synsets, verbFrames, verbTemplates) {

        // inject sense's verb templates
        if (senseToVerbTemplates != null)
            for ((sensekey, templatesIds) in senseToVerbTemplates) {
                val sense = senseFinder(sensekey)
                if (sense != null) {
                    sense.verbTemplates = templatesIds.toSet()
                } else if (WARN_UNRESOLVABLE_SENSE) {
                    Tracing.psErr.println("[W] Unresolvable $sensekey with templates ${templatesIds.joinToString()}")
                }
            }

        // inject sense's tag counts
        if (senseToTagCounts != null)
            for ((sensekey, tagCount) in senseToTagCounts) {
                val sense = senseFinder(sensekey)
                if (sense != null) {
                    sense.tagCount = tagCount.count
                    if (sense.indexInLex + 1 != tagCount.senseNum) {
                        if (WARN_IF_SENSENUM_NOT_EQUAL_INDEX) Tracing.psErr.println("[W] Unequal sense index ${sense.indexInLex + 1} in ${sense.senseId} with tag count sense num ${tagCount.senseNum}")
                        if (WARN_IF_SENSENUM_LESS_THAN_INDEX && sense.indexInLex + 1 > tagCount.senseNum) Tracing.psErr.println("[W] Sense index ${sense.indexInLex + 1} in ${sense.senseId} more than tag count sense num ${tagCount.senseNum}")
                    }
                } else if (WARN_UNRESOLVABLE_SENSE)
                    Tracing.psErr.println("[W] Unresolvable $sensekey with tagcount $tagCount")
            }
    }

    /**
     * Constructor from core model and frame and template data
     *
     * @param coreModel             base model
     * @param verbFrames            verb frames
     * @param verbTemplates         verb templates
     * @param sensesToVerbTemplates collection of entries of type sensekey-to-verb template
     * @param sensesToTagCounts     collection of entries of type sensekey-to-tagcount
     */
    constructor(
        coreModel: CoreModel,
        verbFrames: List<VerbFrame>,
        verbTemplates: List<VerbTemplate>,
        sensesToVerbTemplates: Collection<Pair<SenseKey, List<VerbTemplateId>>>?,
        sensesToTagCounts: Collection<Pair<SenseKey, TagCount>>?,
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
     * Constructor from data
     *
     * @param data data core model
     */
    constructor (
        data: DataModel,
        source: String,
        source2: String,
    ) : this(data.lexes, data.senses, data.synsets, data.verbFrames, data.verbTemplates) {
        this.source = source
        this.source2 = source2
    }

    // identity

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return if (other is Model) {
            val superEq = super.equals(other)
            superEq && verbFrames == other.verbFrames && verbTemplates == other.verbTemplates
        } else false
    }

    override fun hashCode(): Int {
        return Objects.hash(super.hashCode(), verbFrames, verbTemplates)
    }

    /**
     * Verb frames mapped by id
     */
    @Suppress("unused")
    @Transient
    var verbFramesById: Map<VerbFrameId, VerbFrame>? = null
        get() {
            if (field == null) {
                field = map(verbFrames.asSequence()) { it.id }
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
                field = map(verbTemplates.asSequence()) { it.id }
            }
            return field
        }
        private set

    /**
     * Input sources
     */
    val sources: Array<String?>
        get() = arrayOf(source, source2)

    @OptIn(ExperimentalStdlibApi::class)
    override fun toString() = "@${identityHashCode(this).toHexString()} #${hashCode().toHexString()} ${sources.joinToString(prefix = "[", postfix = "]") { it ?: "NULL" }}"

    /**
     * Info about this model
     *
     * @return info
     */
    override fun info(): String {
        return super.info() + ", verb frames: ${verbFrames.size}, verb templates: ${verbTemplates.size}"
    }

    companion object {
        private const val WARN_UNRESOLVABLE_SENSE = false
        private const val WARN_IF_SENSENUM_NOT_EQUAL_INDEX = false
        private const val WARN_IF_SENSENUM_LESS_THAN_INDEX = false
    }
}
