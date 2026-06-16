/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.Formatter.joinToString
import java.io.Serializable
import java.util.*

/**
 * Synset
 *
 * @param synsetId        synset id
 * @param type            synset ss_type {'n', 'v', 'a', 'r', 's'}
 * @param domain          source file
 * @param members         synset lemma members
 * @param definitions     definitions
 * @param examples        examples
 * @param usages          usages
 * @param wikidata        wiki data
 * @param relations       synset relations mapped by type
 *
 * @property synsetId     synset id
 * @property type         synset ss_type {'n', 'v', 'a', 'r', 's'}
 * @property domain       source file
 * @property members      synset lemma members
 * @property definitions  definitions
 * @property examples     examples
 * @property usages       usages
 * @property wikidata     wiki data
 * @property relations    synset relations mapped by type
 *
 * @property partOfSpeech synset part-of-speech {'n', 'v', 'a', 'r'} with ss_type 's' (satellite adj) mapped to 'a'
 * @property definition   first definition
 * @property lexfile      partOfSpeech.domain
 */
@kotlinx.serialization.Serializable
data class Synset(

    // key
    val synsetId: SynsetId,

    // value
    val type: SynsetType,
    val domain: Domain,
    val members: Set<Lemma>,
    val definitions: List<String>,
    val examples: List<Pair<String, String?>>? = null,
    val usages: List<String>? = null,
    var relations: Map<Relation, Set<SynsetId>>? = null,
    val ili: String? = null,
    val wikidata: List<String>? = null,

    ) : Comparable<Synset>, Serializable {

    var source: String? = null

    // computed properties (key, value)
    val key: SenseKey
        get() = synsetId
    val value: Array<Any?>
        get() = arrayOf(type, domain, members, definitions, examples, usages, relations, ili, wikidata, source)

    // computed properties
    val partOfSpeech: PartOfSpeech
        get() = type.toPartOfSpeech()
    val partOfSpeechName: String
        get() = partOfSpeech.fullName
    val definition: String?
        get() = if (definitions.isNotEmpty()) definitions[0] else null
    val lexfile: String = "$partOfSpeechName.$domain"
    val flatRelations: List<Pair<Relation, SynsetId>>?
        get() = relations?.flatMap { (key, values) -> values.map { key to it } }

    // identity

    override fun equals(other: Any?): Boolean {
        // throw UnsupportedOperationException("$this / $other")
        if (this === other) return true
        return if (other is Synset) {
            key == other.key
                    && value.contentEquals(other.value)
        } else false
    }

    override fun hashCode(): Int {
        // throw UnsupportedOperationException("$this")
        return Objects.hash(key, *value)
    }

    // ordering

    override fun compareTo(other: Synset): Int {
        return synsetId.compareTo(other.synsetId)
    }

    // mutation

    /**
     * Add inverse synset relations of this synset
     *
     * @param inverseType    inverse type
     * @param targetSynsetId target synset id
     */
    fun addInverseRelation(inverseType: Relation, targetSynsetId: SynsetId) {
        val mutableRelations = if (relations == null) HashMap() else relations!!.toMutableMap()
        relations = mutableRelations
        val inverseRelations =
            mutableRelations.computeIfPresent(inverseType) { _: Relation, v: Set<SynsetId> -> v.toMutableSet() }
                ?: mutableRelations.computeIfAbsent(inverseType) { LinkedHashSet() }

        require(!inverseRelations.contains(targetSynsetId)) { "Inverse relation $inverseType from $synsetId to $targetSynsetId was already there." }
        (inverseRelations as MutableSet<SynsetId>).add(targetSynsetId)
    }

    // computed

    /**
     * Find senses of this synset
     *
     * @param lemma2Lexes lemma to lexes mapper
     * @param senseKey2Sense senseKey to sense mapper
     * @return senses of this synset
     */
    fun findSenses(
        lemma2Lexes: (lemma: Lemma) -> Collection<Lex>,
        senseKey2Sense: (senseKey: SenseKey) -> Sense,
    ): List<Sense> {

        return members
            .asSequence()
            .map { lemma2Lexes(it) }
            .flatMap { it.asSequence() }
            .filter { it.partOfSpeech == this.partOfSpeech }
            .flatMap { it.senseKeys }
            .map { sk -> senseKey2Sense(sk) }
            .filter { s -> s.synsetId == this.synsetId }
            .toList()
    }

    /**
     * Find sense of lemma in this synset
     *
     * @param lemma lemma
     * @param lexResolver lemma to lexes resolver
     * @param senseKeyResolver senseKey to sense resolver
     * @return sense of lemma in this synset, null if not found
     */
    @Throws(IllegalStateException::class)
    fun findSenseOf(
        lemma: Lemma,
        lexResolver: (Lemma) -> Collection<Lex>,
        senseKeyResolver: (SenseKey) -> Sense,
    ): Sense? {
        val lexes: Collection<Lex> = lexResolver(lemma)
        return lexes
            .asSequence()
            .filter { it.partOfSpeech == partOfSpeech }
            .flatMap { it.senseKeys }
            .map { sk -> senseKeyResolver(sk) }
            .firstOrNull { s -> s.synsetId == this.synsetId }
    }

    /**
     * Find sense of lemma in this synset
     *
     * @param lemma lemma
     * @param lexResolver lemma to lexes resolver
     * @param senseKeyResolver senseKey to sense resolver
     * @return sense of lemma in this synset, null if not found
     */
    @Throws(IllegalStateException::class)
    fun resolveSenseOf(
        lemma: Lemma,
        lexResolver: (lemma: Lemma) -> Collection<Lex>,
        senseKeyResolver: (senseKey: SenseKey) -> Sense,
    ): Sense {
        return findSenseOf(lemma, lexResolver, senseKeyResolver) ?: throw IllegalStateException("Lemma '$lemma' not found in synset $this")
    }

    /**
     * Find 0-based index of member is members
     *
     * @param lemma member lemma
     * @return index of member is members, -1 if not found
     */
    fun findIndexOfMember(lemma: Lemma): Int = members.toList().indexOf(lemma)

    // stringify

    override fun toString(): String {
        val membersStr = members.joinToString(",")
        val relationsStr = relations?.get("hypernym") ?: ""
        return "$synsetId ${type.value} {$membersStr} '$definition' ^$relationsStr"
    }

    fun toLongString(): String {
        val membersStr = members.joinToString(",")
        val relationsStr = relations?.joinToString(",") ?: ""
        return "$synsetId ${type.value} {$membersStr} '$definition' {$relationsStr}"
    }

    companion object {

        val VALID_SYNSET_RELATIONS = arrayOf(

            "hypernym",  // "hyponym",
            "instance_hypernym",  // "instance_hyponym",
            "mero_part",  // "holo_part",
            "mero_member",  // "holo_member",
            "mero_substance",  // "holo_substance",
            "causes",  // "is_caused_by",
            "entails",  // "is_entailed_by",
            "exemplifies",  // "is_exemplified_by",
            "domain_topic",  // "has_domain_topic"
            "domain_region",  // "has_domain_region"
            "attribute",
            "similar",
            "also",
        )

        val SYNSET_RELATIONS = arrayOf(

            "hypernym", "hyponym",
            "instance_hypernym", "instance_hyponym",
            "mero_part", "holo_part",
            "mero_member", "holo_member",
            "mero_substance", "holo_substance",
            "causes", "is_caused_by",
            "entails", "is_entailed_by",
            "exemplifies", "is_exemplified_by",
            "domain_topic", "has_domain_topic",
            "domain_region", "has_domain_region",
            "attribute",
            "similar",
            "also",
        )

        val INVERSE_SYNSET_RELATIONS = mapOf(
            "hypernym" to "hyponym",
            "instance_hypernym" to "instance_hyponym",
            "mero_part" to "holo_part",
            "mero_member" to "holo_member",
            "mero_substance" to "holo_substance",
            "causes" to "is_caused_by",
            "entails" to "is_entailed_by",
            "exemplifies" to "is_exemplified_by",
            "domain_topic" to "has_domain_topic",
            "domain_region" to "has_domain_region",
        )

        /*
        ignored

        agent|
        // also|
        // attribute|
        be_in_state|
        // causes|
        classified_by|
        classifies|
        co_agent_instrument|
        co_agent_patient|
        co_agent_result|
        co_instrument_agent|
        co_instrument_patient|
        co_instrument_result|
        co_patient_agent|
        co_patient_instrument|
        co_result_agent|
        co_result_instrument|
        co_role|
        direction|
        // domain_region|
        // domain_topic|
        // exemplifies|
        // entails|
        eq_synonym|
        // has_domain_region|
        // has_domain_topic|
        // is_exemplified_by|
        holo_location|
        // holo_member|
        // holo_part|
        holo_portion|
        // holo_substance|
        holonym|
        // hypernym|
        // hyponym|
        in_manner|
        // instance_hypernym|
        // instance_hyponym|
        instrument|
        involved|
        involved_agent|
        involved_direction|
        involved_instrument|
        involved_location|
        involved_patient|
        involved_result|
        involved_source_direction|
        involved_target_direction|
        // is_caused_by|
        // is_entailed_by|
        location|
        manner_of|
        mero_location|
        // mero_member|
        // mero_part|
        mero_portion|
        // mero_substance|
        meronym|
        // similar|
        // other|
        patient|
        restricted_by|
        restricts|
        result|
        role|
        source_direction|
        state_of|
        target_direction|
        subevent|
        is_subevent_of|
        // antonym|
        feminine|
        has_feminine|
        masculine|
        has_masculine|
        young|
        has_young|
        diminutive|
        has_diminutive|
        augmentative|
        has_augmentative|
        anto_gradable|
        anto_simple|
        anto_converse|
        ir_synonym
       */

    }
}
