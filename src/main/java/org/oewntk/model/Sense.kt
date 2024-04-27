/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */
package org.oewntk.model

import java.io.Serializable
import java.util.*

/**
 * Sense
 *
 * @property senseId     sense id / sensekey
 * @property lex         lexical item
 * @property type        {n,v,a,r,s}
 * @property synsetId    synset id
 * @property examples    examples
 * @property verbFrames  verb frames
 * @property adjPosition adjective position
 * @property relations   sense relations
 *
 * @property senseKey    sense id / sensekey
 * @property lemma       lemma
 * @property lCLemma     lower-cased lemma
 * @property tagCount    tag count
 * @property intTagCount integer tag count
 * @param    indexInLex  index of this sense in lex
 */
class Sense(

	/**
	 * Sense id or sensekey
	 */
	private val senseId: SenseKey,

	/**
	 * Lexical item, lex this sense is contained in
	 */
	@JvmField val lex: Lex,

	/**
	 * Synset type ss_type {'n', 'v', 'a', 'r', 's'}
	 */
	@JvmField val type: PosType,

	/**
	 * Index in lex entry
	 */
	@JvmField val indexInLex: Int,

	/**
	 * Synset id
	 */
	@JvmField val synsetId: SynsetId,

	/**
	 * Examples
	 */
	@JvmField val examples: Array<String>?,

	/**
	 * Verb frames
	 */
	@JvmField val verbFrames: Array<VerbFrameType>?,

	/**
	 * Adjective position in {'a', 'ip', 'p'} meaning {attribute,immediate postnominal,predicate}
	 */
	@JvmField val adjPosition: AdjPositionType?,

	/**
	 * Sense relations mapped by type
	 */
	relations: MutableMap<RelationType, MutableSet<SenseKey>>?

) : Comparable<Sense>, Serializable {

	/**
	 * Sense id, sensekey
	 */
	val senseKey: SenseKey
		get() = senseId

	/**
	 * Synset part-of-speech ss_type {'n', 'v', 'a', 'r'}
	 */
	@JvmField
	val partOfSpeech: Char = if (this.type == 's') 'a' else this.type

	/**
	 * Zero-based index of this sense in lex list/array of senses
	 */
	val lexIndex: Int = indexInLex

	/**
	 * Verb sentence templates
	 */
	var verbTemplates: Array<Int>? = null

	/**
	 * Tag count
	 */
	var tagCount: TagCount? = null

	/**
	 * Sense relations
	 */
	var relations: MutableMap<RelationType, MutableSet<SenseKey>>?

	/**
	 * Lemma
	 */
	val lemma: String
		get() = lex.lemma

	/**
	 * Lower-case lemma
	 */
	val lCLemma: String
		get() = lex.lCLemma

	/**
	 * Int tag count
	 */
	val intTagCount: Int
		get() = if (tagCount == null) 0 else tagCount!!.count

	/**
	 * Source file
	 */
	val source: String?
		get() = lex.source

	// init

	init {
		this.relations = relations
	}

	// mutation

	/**
	 * Add inverse sense relations of this synset
	 *
	 * @param inverseType    inverse type
	 * @param targetSensekey target sense id (sensekey)
	 */
	fun addInverseRelation(inverseType: String, targetSensekey: String) {
		if (relations == null) {
			relations = HashMap()
		}
		val inverseRelations = relations!!.computeIfAbsent(inverseType) { _: String? -> LinkedHashSet() }
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
	fun findSynsetIndex(synsetsById: Map<String, Synset>): Int {
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
		return " $senseId (${lexIndex + 1}th of '${lex.lemma}', $synsetId $type)"
	}

	fun toLongString(): String {
		val relationsStr = Formatter.join(relations, ",")
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
