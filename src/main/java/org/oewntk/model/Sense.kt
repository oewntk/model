/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.Formatter.joinToString
import java.io.Serializable
import java.util.*

/**
 * Sense
 *
 * @param senseId             sense id / sensekey
 * @param lexId               lexical item (lex) id this sense refers to
 * @param synsetId            synset id
 * @param indexInLex          zero-based index of this sense in lex list/array of senses
 * @param examples            examples
 * @param verbFrames          verb frames
 * @param adjPosition         adjective position {'a', 'ip', 'p'} meaning {attribute,immediate postnominal,predicate}
 * @param relations           sense relations mapped by type
 *
 * @property senseId          sense id / sensekey
 * @property lexId            lexical item (lex) id this sense refers to
 * @property synsetId         synset id
 * @property type             synset type ss_type {'n', 'v', 'a', 'r', 's'}
 * @property indexInLex         zero-based index of this sense in lex list/array of senses
 * @property examples         examples
 * @property verbFrames       verb frames
 * @property adjPosition      adjective position {'a', 'ip', 'p'} meaning {attribute,immediate postnominal,predicate}
 * @property relations        sense relations mapped by type
 *
 * @property tagCount         tag count
 * @property verbTemplates    verb sentence templates
 *
 * @property senseKey         alias for sense id
 * @property lemma            lemma
 * @property lCLemma          lower-cased lemma
 * @property partOfSpeech     sense part-of-speech {'n', 'v', 'a', 'r'} with ss_type 's' (satellite adj) mapped to 'a'
 * @property intTagCount      integer tag count
 */
@kotlinx.serialization.Serializable
data class Sense(
    // key
    val senseId: SenseKey,
    // value
    val lexId: LexId,
    val synsetId: SynsetId,

    val indexInLex: Int = 0,
    val examples: List<Pair<String, String?>>? = null,
    val verbFrames: List<VerbFrameId>? = null,
    var verbTemplates: List<VerbTemplateId>? = null,
    val adjPosition: AdjPosition? = null,
    var tagCount: Int? = null,
    var relations: Map<Relation, Set<SenseKey>>? = null,

    ) : Comparable<Sense>, Serializable {

    // computed properties (key, value, properties)
    val key: SenseKey
        get() = senseId
    val value: Pair<LexId, SynsetId>
        get() = lexId to synsetId
    val properties: Array<Any?>
        get() = arrayOf(type, indexInLex, examples, adjPosition, relations, verbFrames, verbTemplates, tagCount)

    // computed properties
    val lemma: Lemma
        get() = lexId.lemma
    val lCLemma: Lemma
        get() = lemma.lowercase(Locale.ENGLISH)
    val isCased: Boolean
        get() = lemma != lCLemma
    val type: SynsetType
        get() = lexId.type
    val partOfSpeech: PartOfSpeech
        get() = type.toPartOfSpeech()
    val senseKey: SenseKey
        get() = senseId
    val intTagCount: Int
        get() = tagCount ?: 0
    val flatRelations: List<Pair<Relation, SynsetId>>?
        get() = relations?.flatMap { (key, values) -> values.map { key to it } }

    // identity

    override fun equals(other: Any?): Boolean {
        // throw UnsupportedOperationException("$this / $other")
        if (this === other) return true
        return if (other is Sense) Objects.equals(key, other.key) && Objects.equals(value, other.value) && Objects.equals(properties, other.properties) else false
    }

    override fun hashCode(): Int {
        // throw UnsupportedOperationException("$this")
        return Objects.hash(key, value, *properties)
    }

    // mutation

    /**
     * Add inverse sense relations of this synset
     *
     * @param inverseType    inverse type
     * @param targetSensekey target sense id (sensekey)
     */
    fun addInverseRelation(inverseType: Relation, targetSensekey: SenseKey) {
        val mutableRelations = if (relations == null) HashMap() else relations!!.toMutableMap()
        relations = mutableRelations
        val inverseRelations =
            mutableRelations.computeIfPresent(inverseType) { _: Relation, v: Set<SynsetId> -> v.toMutableSet() }
                ?: mutableRelations.computeIfAbsent(inverseType) { LinkedHashSet() }

        require(!inverseRelations.contains(targetSensekey)) { "Inverse relation $inverseType from $synsetId to $targetSensekey was already there." }
        (inverseRelations as MutableSet<SynsetId>).add(targetSensekey)
    }

    // computed

    /**
     * Find synset member index
     *
     * @param synsetResolver synset resolver from id
     * @return index of this sense in synset members
     */
    fun findSynsetIndex(synsetResolver: (SynsetId) -> Synset): Int {
        val synset = synsetResolver(synsetId)
        return synset.findIndexOfMember(lemma)
    }

    /**
     * Find lexid
     * WNDB
     * One digit hexadecimal integer that, when appended onto lemma , uniquely identifies a sense within a lexicographer file.
     * lex_id numbers usually start with 0 , and are incremented as additional senses of the word are added to the same file,
     * although there is no requirement that the numbers be consecutive or begin with 0 .
     * Note that a value of 0 is the default, and therefore is not present in lexicographer files.
     * synset = synset_offset  lex_filenum  ss_type  w_cnt  word  lex_id  [word  lex_id...] ...
     *
     *
     * SENSEIDX
     * Two digit decimal integer that, when appended onto lemma , uniquely identifies a sense within a lexicographer file.
     * lex_id numbers usually start with 00 , and are incremented as additional senses of the word are added to the same file,
     * although there is no requirement that the numbers be consecutive or begin with 00 .
     * Note that a value of 00 is the default, and therefore is not present in lexicographer files. (senseidx)
     * sensekey = ss_type:lex_filenum:lex_id:head_word:head_id
     *
     *
     * SENSEKEY
     * lemma % lex_sense
     * where lex_sense is encoded as:
     * ss_type:lex_filenum:lex_id:head_word:head_id
     *
     * @return lexid
     */
    fun findLexid(): Int {
        return senseId.split("%".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()[1]
            .split(":".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()[2].toInt()
    }

    // stringify

    override fun toString(): String {
        return "$senseId (${indexInLex + 1}th of '${lemma}', $synsetId ${type.value})"
    }

    fun toLongString(): String {
        val relationsStr = relations?.joinToString(",") ?: ""
        return "[${indexInLex + 1}] of '${lemma}' $senseId ${type.value} $synsetId {$relationsStr}"
    }

    // ordering

    override fun compareTo(other: Sense): Int {
        return senseId.compareTo(other.senseId)
    }

    companion object {

        val VALID_SENSE_RELATIONS = arrayOf(
            "antonym",
            "similar",
            "exemplifies",
            "derivation",
            "pertainym",
            "participle",
            "also",
            "domain_region",
            "domain_topic",

            "state",
            "result",
            "event",
            "property",
            "location",
            "destination",
            "agent",
            "undergoer",
            "uses",
            "instrument",
            "by_means_of",
            "material",
            "vehicle",
            "body_part",

            "collocation",
            "other"
        )

        val SENSE_RELATIONS = arrayOf(
            "antonym",
            "similar",
            "exemplifies", "is_exemplified_by",
            "derivation",
            "pertainym",
            "participle",
            "also",
            "domain_region", "has_domain_region",
            "domain_topic", "has_domain_topic",

            "agent",
            "material",
            "event",
            "instrument",
            "location",
            "by_means_of",
            "undergoer",
            "property",
            "result",
            "state",
            "uses",
            "destination",
            "body_part",
            "vehicle",

            "collocation",
            "other"
        )

        val INVERSE_SENSE_RELATIONS = mapOf(
            "exemplifies" to "is_exemplified_by",
            "domain_topic" to "has_domain_topic",
            "domain_region" to "has_domain_region",
        )

        /*
        ignored
          // antonym|
          // also|
          // participle|
          // pertainym|
          // derivation|
          // domain_topic|
          // has_domain_topic|
          // domain_region|
          // has_domain_region|
          // exemplifies|
          // is_exemplified_by|
          // similar|
          // other|
          simple_aspect_ip|
          secondary_aspect_ip|
          simple_aspect_pi|
          secondary_aspect_pi|
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
          anto_converse
         */

    }
}
