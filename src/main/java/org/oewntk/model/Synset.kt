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
 * @param wikidata        wiki data
 * @param relations       synset relations mapped by type
 *
 * @property synsetId     synset id
 * @property type         synset ss_type {'n', 'v', 'a', 'r', 's'}
 * @property domain       source file
 * @property members      synset lemma members
 * @property definitions  definitions
 * @property examples     examples
 * @property wikidata     wiki data
 * @property relations    synset relations mapped by type
 *
 * @property partOfSpeech synset part-of-speech {'n', 'v', 'a', 'r'} with ss_type 's' (satellite adj) mapped to 'a'
 * @property definition   first definition
 * @property lexfile      partOfSpeech.domain
 */
@kotlinx.serialization.Serializable
data class Synset(

    val synsetId: SynsetId,
    val type: Category,
    val domain: Domain,
    val members: Array<Lemma>,
    val definitions: Array<String>,
    val examples: Array<String>? = null,
    var relations: MutableMap<Relation, MutableSet<SynsetId>>? = null,
    val wikidata: String? = null,

    ) : Comparable<Synset>, Serializable {

    val partOfSpeech: Char
        get() = if (type == 's') 'a' else type
    val definition: String?
        get() = if (definitions.isNotEmpty()) definitions[0] else null
    val lexfile: String?
        get() = when (partOfSpeech) {
            'n'  -> "noun.$domain"
            'v'  -> "verb.$domain"
            'a'  -> "adj.$domain"
            'r'  -> "adv.$domain"
            else -> null
        }

    // mutation

    /**
     * Add inverse synset relations of this synset
     *
     * @param inverseType    inverse type
     * @param targetSynsetId target synset id
     */
    fun addInverseRelation(inverseType: Relation, targetSynsetId: SynsetId) {
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
     * @param lemma        lemma
     * @param lemma2Lexes lemma to lexes mapper
     * @param senseKey2Sense senseKey to sense mapper
     * @return sense of lemma in this synset, null if not found
     */
    @Throws(IllegalStateException::class)
    fun findSenseOf(
        lemma: Lemma,
        lemma2Lexes: (lemma: Lemma) -> Collection<Lex>?,
        senseKey2Sense: (senseKey: SenseKey) -> Sense,
    ): Sense {
        val lexes: Collection<Lex> = checkNotNull(lemma2Lexes(lemma)) { "$lemma has no sense" }
        return lexes
            .asSequence()
            .filter { it.partOfSpeech == partOfSpeech }
            .flatMap { it.senseKeys }
            .map { sk -> senseKey2Sense(sk) }
            .firstOrNull { s -> s.synsetId == this.synsetId } ?: throw IllegalStateException("Lemma $lemma not found in synset $this")
    }

    /**
     * Find 0-based index of member is members
     *
     * @param lemma member lemma
     * @return index of member is members, -1 if not found
     */
    fun findIndexOfMember(lemma: Lemma): Int {
        val members = members
        val memberList = listOf(*members)
        return memberList.indexOf(lemma)
    }

    // stringify

    override fun toString(): String {
        val membersStr = members.joinToString(",")
        val relationsStr = relations?.joinToString(",") ?: ""
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

