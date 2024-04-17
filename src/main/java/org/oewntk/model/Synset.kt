/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */
package org.oewntk.model

import java.io.Serializable
import java.util.*

/**
 * Synset
 *
 * @property synsetId    synset id
 * @property type        type: {n,v,a,r,s}
 * @property domain      source file
 * @property members     synset members
 * @property definitions definitions
 * @property examples    examples
 * @property wikidata    wiki data
 * @property relations   synset relations
 */
class Synset(
	/**
	 * Synset id
	 */
	@JvmField val synsetId: String,

	/**
	 * Synset type ss_type {'n', 'v', 'a', 'r', 's'}
	 */
	@JvmField val type: Char,

	/**
	 * Source file
	 */
	val domain: String,

	/**
	 * Lemma members
	 */
	@JvmField val members: Array<String>,

	/**
	 * Definitions
	 */
	@JvmField val definitions: Array<String>,

	/**
	 * Examples
	 */
	@JvmField val examples: Array<String>?,

	/**
	 * Wiki data
	 */
	val wikidata: String,

	/**
	 * Synset relations
	 */
	var relations: MutableMap<String, MutableSet<String>>?

) : Comparable<Synset>, Serializable {

	/**
	 * Part-of-speech
	 */
	val partOfSpeech: Char
		get() {
			if (type == 's') {
				return 'a'
			}
			return type
		}

	/**
	 * First definition
	 */
	val definition: String?
		get() {
			if (!definitions.isNullOrEmpty()) {
				return definitions[0]
			}
			return null
		}

	/**
	 * Lex file
	 */
	val lexfile: String?
		get() {
			when (partOfSpeech) {
				'n' -> return "noun.$domain"
				'v' -> return "verb.$domain"
				'a' -> return "adj.$domain"
				'r' -> return "adv.$domain"
			}
			return null
		}

	// mutation

	/**
	 * Add inverse synset relations of this synset
	 *
	 * @param inverseType    inverse type
	 * @param targetSynsetId target synset id
	 */
	fun addInverseRelation(inverseType: String, targetSynsetId: String) {
		if (relations == null) {
			relations = HashMap()
		}
		val inverseRelations = relations!!.computeIfAbsent(inverseType) { _: String? -> LinkedHashSet() }
		require(!inverseRelations.contains(targetSynsetId)) { "Inverse relation $inverseType from $synsetId to $targetSynsetId was already there." }
		inverseRelations.add(targetSynsetId)
	}

	// computed

	/**
	 * Find senses of this synset
	 *
	 * @param lexesByLemma lexes
	 * @return senses of this synset
	 */
	fun findSenses(lexesByLemma: Map<String?, Collection<Lex>>): Array<Sense?> {
		val members = members
		val senses = arrayOfNulls<Sense>(members.size)
		var i = 0
		for (member in members) {
			for (lex in lexesByLemma[member]!!) {
				if (lex.partOfSpeech != partOfSpeech) {
					continue
				}
				for (sense in lex.senses) {
					if (sense.synsetId == synsetId) {
						senses[i] = sense
						i++
					}
				}
			}
		}
		assert(i == members.size)
		return senses
	}

	/**
	 * Find sense of lemma in this synset
	 *
	 * @param lemma        lemma
	 * @param lexesByLemma lexes
	 * @return sense of lemma in this synset, null if not found
	 */
	fun findSenseOf(lemma: String?, lexesByLemma: Map<String, Collection<Lex>?>): Sense? {
		val lexes: Collection<Lex> = checkNotNull(lexesByLemma[lemma]) { "$lemma has no sense" }
		for (lex in lexes) {
			if (lex.partOfSpeech != partOfSpeech) {
				continue
			}
			for (sense in lex.senses) {
				if (sense.synsetId == synsetId) {
					return sense
				}
			}
		}
		// System.err.printf("lemma %s not found in synset %s%n", lemma, this);
		return null
	}

	/**
	 * Find 0-based index of member is members
	 *
	 * @param lemma member lemma
	 * @return index of member is members, -1 if not found
	 */
	fun findIndexOfMember(lemma: String): Int {
		val members = members
		val memberList = listOf(*members)
		return memberList.indexOf(lemma)
	}

	// stringify

	override fun toString(): String {
		val membersStr = Formatter.join(members, ",")
		val relationsStr = Formatter.join(relations, ",")
		return "$synsetId $type {$membersStr} '$definition' {$relationsStr}"
	}

	// identity

	override fun equals(other: Any?): Boolean {
		if (this === other) {
			return true
		}
		if (other == null || javaClass != other.javaClass) {
			return false
		}
		val synset = other as Synset
		return synsetId == synset.synsetId
	}

	override fun hashCode(): Int {
		return Objects.hash(synsetId)
	}

	// ordering

	override fun compareTo(other: Synset): Int {
		return synsetId.compareTo(other.synsetId)
	}
}

