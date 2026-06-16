/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.Lex.Groups.groupByLCLemma
import org.oewntk.model.Lex.Groups.groupByLemma
import org.oewntk.model.Lex.Groups.groupByLemmaThenByKey2
import org.oewntk.model.Lex.Groups.lexByLemmaThenByKey2
import org.oewntk.model.MapFactory.sensesById
import org.oewntk.model.MapFactory.synsetsById
import java.io.Serializable
import java.lang.System.identityHashCode
import java.util.*

/**
 * Sealed Base language model
 *
 * @property lexes          lexical items
 * @property senses         senses
 * @property synsets        synsets
 */
@kotlinx.serialization.Serializable
@kotlinx.serialization.SerialName("base_model")
sealed class BaseModel : Serializable {

    abstract val lexes: Collection<Lex>
    abstract val senses: Collection<Sense>
    abstract val synsets: Collection<Synset>

}

@kotlinx.serialization.Serializable
@kotlinx.serialization.SerialName("data_core_model")
data class DataCoreModel(
    override val lexes: List<Lex>,
    override val senses: List<Sense>,
    override val synsets: List<Synset>,

    ) : BaseModel(), Serializable {

    constructor (
        model: CoreModel,
    ) : this(model.lexes, model.senses, model.synsets)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return if (other is DataCoreModel)
            lexes == other.lexes
                    && senses == other.senses
                    && synsets == other.synsets
        else false
    }

    override fun hashCode(): Int {
        return Objects.hash(lexes, senses, synsets)
    }

}

/**
 * Base language model
 *
 * @property lexes          lexical items (unmodifiable)
 * @property senses         senses (unmodifiable)
 * @property synsets        synsets (unmodifiable)
 * @property source         source, typically input directory
 *
 * @property lexHyperMap1   transient lexical item mapped by lemma then key2
 * @property lexesByLemma   transient lexical items mapped by (case-sensitive) lemma written form
 * @property lexesByLCLemma transient lexical items mapped by lower-cased lemma written form
 * @property sensesById     transient senses mapped by sense id (sensekey)
 * @property synsetsById    transient synsets mapped by synset id
 */
@kotlinx.serialization.Serializable
@kotlinx.serialization.SerialName("core_model")
open class CoreModel(
    override val lexes: List<Lex>,
    override val senses: List<Sense>,
    override val synsets: List<Synset>,
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

    // B Y - K E Y    C A C H E D    L A Z Y   M A P S

    /**
     * Lexes mapped by lemma then key2
     * @Transient
     */
    val lexHyperMap: HyperMap by lazy { lexes.asSequence().groupByLemmaThenByKey2() }

    /**
     * Lex mapped by lemma then key2
     * @Transient
     */
    val lexHyperMap1: HyperMap1 by lazy { lexes.asSequence().lexByLemmaThenByKey2() }

    /**
     * Lexical units mapped by lemma written form.
     * A multimap: each value is an array of lexes for the lemma.
     * @Transient
     */
    private val lexesByLemma: Map<Lemma, Collection<Lex>> by lazy { lexes.asSequence().groupByLemma() }

    /**
     * Lexical units mapped by lemma lower-cased written form.
     * A multimap: each value is an array of lexes for the lemma.
     * @Transient
     */
    private val lexesByLCLemma: Map<LowerCasedLemma, Collection<Lex>> by lazy { lexes.asSequence().groupByLCLemma() }

    /**
     * Senses mapped by id (sensekey)
     * @Transient
     */
    private val sensesById: Map<SenseKey, Sense> by lazy { senses.asSequence().sensesById() }

    /**
     * Synsets mapped by id (synset id)
     * @Transient
     */
    private val synsetsById: Map<SynsetId, Synset> by lazy { synsets.asSequence().synsetsById() }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return if (other is CoreModel) {
            lexes == other.lexes && senses == other.senses && synsets == other.synsets
        } else false
    }

    override fun hashCode(): Int {
        return Objects.hash(lexes, senses, synsets)
    }

    // F I N D E R S   A N D   R E S O L V E R S

    // lex

    /**
     * Lex finder (nullable result)
     * Resolution may yield null
     */
    val lexFinder: (Lemma) -> Collection<Lex>?
        get() = { lexesByLemma[it] }

    /**
     * Lex finder ignoring lemma case (nullable result)
     * Resolution may yield null
     */
    val lexIgnoreCaseFinder: (Lemma) -> Collection<Lex>?
        get() = { lexesByLCLemma[it.lowercase(Locale.ENGLISH)] }

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

    // unique lex

    /**
     * Resolves (Lemma, Key1) to assumed unique Lex
     */
    val lexFinder1: (Lemma, Key2) -> Lex?
        get() = { lemma: Lemma, key2: Key2 ->
            lexHyperMap1[lemma]?.get(key2)
        }

    /**
     * Resolves (Lemma, Key1) to assumed unique existing Lex
     */
    val lexResolver1: (lemma: Lemma, key2: Key2) -> Lex
        get() = { lemma, key2 -> lexFinder1(lemma, key2)!! }

    // S E N S E

    /**
     * Sense finder (nullable result)
     * Resolution may yield null
     */
    val senseFinder: (SenseKey) -> Sense?
        get() = { sensesById[it] }

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
        get() = { synsetsById[it] }

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
        InverseRelationFactory.makeInverseSynsetRelations(synsetsById)
        InverseRelationFactory.makeInverseSenseRelations(sensesById)
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
        InverseRelationFactory.makeInverseSynsetRelations(toSynsetRelationInverse, synsetsById)
        InverseRelationFactory.makeInverseSenseRelations(toSenseRelationInverse, sensesById)
        return this
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun toString() = "@${identityHashCode(this).toHexString()} #${hashCode().toHexString()} $source"

    /**
     * Info about this model
     *
     * @return info
     */
    open fun info(): String? {
        return "lexes: ${lexes.size}, senses: ${senses.size}, synsets: ${synsets.size}"
    }

}
