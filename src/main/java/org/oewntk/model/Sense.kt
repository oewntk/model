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
 * @param lex                 lexical item (lex) this sense is contained in
 * @param type                synset type ss_type {'n', 'v', 'a', 'r', 's'}
 * @param synsetId            synset id
 * @param lexIndex            zero-based index of this sense in lex list/array of senses
 * @param examples            examples
 * @param verbFrames          verb frames
 * @param adjPosition         adjective position {'a', 'ip', 'p'} meaning {attribute,immediate postnominal,predicate}
 * @param relations           sense relations mapped by type
 *
 * @property senseId          sense id / sensekey
 * @property lex              lexical item (lex) this sense is contained in
 * @property type             synset type ss_type {'n', 'v', 'a', 'r', 's'}
 * @property synsetId         synset id
 * @property lexIndex         zero-based index of this sense in lex list/array of senses
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
 * @property source           sense source
 */
@kotlinx.serialization.Serializable
data class Sense(
    private val senseId: SenseKey,
    val lex: Lex,
    val type: PosId,
    val lexIndex: Int,
    val synsetId: SynsetId,
    val verbFrames: Array<VerbFrameId>? = null,
    val adjPosition: AdjPositionType? = null,
    var relations: MutableMap<RelationId, MutableSet<SenseKey>>? = null,
    val examples: Array<String>? = null,

    ) : Comparable<Sense>, Serializable {

    var verbTemplates: Array<Int>? = null
    var tagCount: TagCount? = null
    val senseKey: SenseKey
        get() = senseId
    val lemma: LemmaType
        get() = lex.lemma
    val lCLemma: LemmaType
        get() = lex.lCLemma
    val partOfSpeech: PosId = if (this.type == 's') 'a' else this.type
    val intTagCount: Int
        get() = if (tagCount == null) 0 else tagCount!!.count
    val source: String?
        get() = lex.source

    // mutation

    /**
     * Add inverse sense relations of this synset
     *
     * @param inverseType    inverse type
     * @param targetSensekey target sense id (sensekey)
     */
    fun addInverseRelation(inverseType: RelationId, targetSensekey: SenseKey) {
        if (relations == null) {
            relations = HashMap()
        }
        val inverseRelations = relations!!.computeIfAbsent(inverseType) { LinkedHashSet() }
        require(!inverseRelations.contains(targetSensekey)) { "Inverse relation $inverseType from $synsetId to $targetSensekey was already there." }
        inverseRelations.add(targetSensekey)
    }

    // computed

    /**
     * Find synset member index
     *
     * @param synsetsById synsets mapped by id
     * @return index of this sense in synset members
     */
    fun findSynsetIndex(synsetsById: Map<SynsetId, Synset>): Int {
        val synset = synsetsById[synsetId]
        return synset!!.findIndexOfMember(lex.lemma)
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
        return "$senseId (${lexIndex + 1}th of '${lex.lemma}', $synsetId $type)"
    }

    fun toLongString(): String {
        val relationsStr = relations.joinToString(",")
        return "[${lexIndex + 1}] of '${lex.lemma}' $senseId $type $synsetId {$relationsStr}"
    }

    // identity

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val sense = other as Sense
        return senseId == sense.senseId
    }

    override fun hashCode(): Int {
        return Objects.hash(senseId)
    }

    // ordering

    override fun compareTo(other: Sense): Int {
        return senseId.compareTo(other.senseId)
    }
}
