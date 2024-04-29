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
    val synsetId: SynsetId,

    /**
     * Synset type ss_type {'n', 'v', 'a', 'r', 's'}
     */
    val type: PosType,

    /**
     * Source file
     */
    val domain: String,

    /**
     * Lemma members
     */
    val members: Array<String>,

    /**
     * Definitions
     */
    val definitions: Array<String>,

    /**
     * Examples
     */
    val examples: Array<String>?,

    /**
     * Wiki data
     */
    private val wikidata: String?,

    /**
     * Synset relations, mapped by type
     */
    var relations: MutableMap<RelationType, MutableSet<SynsetId>>?,

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
            if (definitions.isNotEmpty()) {
                return definitions[0]
            }
            return null
        }

    /**
     * Lex file
     */
    val lexfile: String?
        get() {
            return when (partOfSpeech) {
                'n'  -> "noun.$domain"
                'v'  -> "verb.$domain"
                'a'  -> "adj.$domain"
                'r'  -> "adv.$domain"
                else -> null
            }
        }

    // mutation

    /**
     * Add inverse synset relations of this synset
     *
     * @param inverseType    inverse type
     * @param targetSynsetId target synset id
     */
    fun addInverseRelation(inverseType: String, targetSynsetId: SynsetId) {
        if (relations == null) {
            relations = HashMap()
        }
        val inverseRelations = relations!!.computeIfAbsent(inverseType) { LinkedHashSet() }
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
    fun findSenses(lexesByLemma: Map<String, Collection<Lex>>): Array<Sense> {
        val senses = ArrayList<Sense>()
        for (member in members) {
            for (lex in lexesByLemma[member]!!) {
                if (lex.partOfSpeech != partOfSpeech) {
                    continue
                }
                for (sense in lex.senses) {
                    if (sense.synsetId == synsetId) {
                        senses.add(sense)
                    }
                }
            }
        }
        return senses.toTypedArray()
    }

    /**
     * Find sense of lemma in this synset
     *
     * @param lemma        lemma
     * @param lexesByLemma lexes
     * @return sense of lemma in this synset, null if not found
     */
    @Throws(IllegalStateException::class)
    fun findSenseOf(lemma: String, lexesByLemma: Map<String, Collection<Lex>>): Sense {
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
        throw IllegalStateException("Lemma $lemma not found in synset $this")
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

