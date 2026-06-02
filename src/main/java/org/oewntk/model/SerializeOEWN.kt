package org.oewntk.model

import org.oewntk.model.InverseRelationFactory.INVERSE_SENSE_RELATIONS_SET
import org.oewntk.model.InverseRelationFactory.INVERSE_SYNSET_RELATIONS_SET
import org.oewntk.model.Lex.Groups.lexByLemmaThenByKey2

const val INCLUDE_LEXFILE = false

typealias Filename = String

/**
 * Pronunciation to serializable map
 *
 * @receiver pronunciation
 * @return map
 * Keys:
 *  - value
 *  - variety
 */
fun Pronunciation.toSerializable(): Map<String, Any> {
    return mutableMapOf("value" to value)
        .apply {
            variety?.let { this["variety"] = it }
        }
}

/*
 * Example (Pair<text, source>) to serializable data
 * @return text string if source is null a map otherwise
 * Keys if a map
    text
    source
*/

// L E X

/**
 * Lex to serializable map
 *
 * @receiver lex
 * @param resolver senseKey to sense resolver
 * @return map
 * Keys:
 * - form
 * - pronunciation
 * - sense
 * - source
 */
fun Lex.toSerializable(resolver: (SenseKey) -> Sense?): Map<String, Any> {
    val serializedSenses = senseKeys
        .map { resolver.invoke(it)!! }
        .map(Sense::toSerializable)
        .toList()
    return mutableMapOf<String, Any>(
        "sense" to serializedSenses,
        // "key2" to key2,
    ).apply {
        forms?.let { this["form"] = it.map { it2 -> it2 }.toList() }
        pronunciations?.let { this["pronunciation"] = it.map(Pronunciation::toSerializable).toList() }
        if (INCLUDE_LEXFILE) lexfile.let { this["lexfile"] = it }
    }.toSortedMap()
}

// S E N S E

/**
 * Sense to serializable map
 *
 * @receiver sense
 * @return map
 * Keys:
 * - id
 * - synset
 * - adjposition
 * - subcat
 * - sent
 * - <relations>
 */
fun Sense.toSerializable(): Map<String, Any> {
    return mutableMapOf<String, Any>(
        "id" to senseKey,
        "synset" to synsetId,
    ).apply {
        adjPosition?.let { this["adjposition"] = it }
        examples?.let { this["sent"] = it.map { it2 -> if (it2.second == null) it2.first else mapOf("text" to it2.first, "source" to it2.second) } }
        verbFrames?.let { this["subcat"] = it }
        relations
            ?.filterNot { it.key in INVERSE_SENSE_RELATIONS_SET }
            ?.forEach { (rel: String, target) ->
                this[rel] = target.toList()
            }
    }.toSortedMap()
}

/**
 * Senses to serializable list
 *
 * @receiver sequence of senses
 * @return list of serialized senses
 */
fun Sequence<Sense>.toSerializable(): List<Any> {
    return this
        .map(Sense::toSerializable)
        .toList()
}

// S Y N S E T

/**
 * Synset to serializable map
 *
 * @receiver synset
 * @return map
 * Keys:
 * - members
 * - partOfSpeech
 * - definition
 * - example
 * - usage
 * - wikidata
 * - ili
 * - <relations>
 */
fun Synset.toSerializable(): Map<String, Any> {
    return mutableMapOf(
        // "id" to synsetId,
        "partOfSpeech" to partOfSpeech.value,
        "definition" to listOf(definition!!),
        "members" to members.toList(),
    ).apply {
        examples?.let { this["example"] = it.map { it2 -> if (it2.second == null) it2.first else mapOf("source" to it2.second, "text" to it2.first) } }
        usages?.let { this["usage"] = it }
        relations
            ?.filterNot { it.key in INVERSE_SYNSET_RELATIONS_SET }
            ?.forEach { (rel, target) ->
                this[rel] = target.toList()
            }
        wikidata?.let { if (it.isNotEmpty()) this["wikidata"] = if (it.size == 1) it[0] else it }
        ili?.let { this["ili"] = it }
        source?.let { this["source"] = it }
        if (INCLUDE_LEXFILE) this["lexfile"] = lexfile
    }.toSortedMap()
}

/**
 * Synsets to serializable map
 *
 * @receiver sequence of synsets
 * @return map of synset by id
 */
fun Sequence<Synset>.toSerializable(): Map<SynsetId, Any> = this.associate { it.synsetId to it.toSerializable() }

/**
 * Synsets by id to serializable map
 */
fun Map<SynsetId, Synset>.toSerializable(): Map<SynsetId, Any> = this.mapValues { it.value.toSerializable() }

// M A P P E D   L E X E S

/**
 * Lexes to serializable map
 *
 * @receiver sequence of lexes
 * @param senseResolver senseKey to sense resolver
 * @return map by lemma
 */
fun Sequence<Lex>.toSerializable(senseResolver: (SenseKey) -> Sense): Map<Lemma, Map<Key2, Map<String, Any>>> {
    val hypermap1: HyperMap1 = this.lexByLemmaThenByKey2()
    return hypermap1.toSerializable(senseResolver)
}

// TODO REMOVE
fun Sequence<Lex>.toSerializable0(senseResolver: (SenseKey) -> Sense): Map<Lemma, Map<Key2, Map<String, Any>>> {
    return mutableMapOf<String, Map<Key2, Map<String, Any>>>()
        .apply {
            this@toSerializable0
                .sortedBy { it.lemma }
                .groupBy { it.lemma }
                .forEach { (lemma, lexes) ->
                    this[lemma] = lexes.associate { it.key2 to it.toSerializable(senseResolver) }
                }
        }
}

/**
 * Hyper map to serializable vap
 *
 * @receiver hypermap1 (lex by lemma then by key2)
 */
fun HyperMap1.toSerializable(senseResolver: (SenseKey) -> Sense): Map<Lemma, Map<Key2, Map<String, Any>>> {
    return this
        .mapValues { (_: Lemma, v) ->
            v.mapValues { (_: Key2, lex) -> lex.toSerializable { senseResolver(it) } }.toSortedMap()
        }.toSortedMap()
}

// M O D E L

/**
 * Merged data generator
 *
 * @receiver core model
 * @yield content (either lex entries or synsets) to file
 */
fun CoreModel.toOneSerializable(): Sequence<Pair<Map<String, Any>, Filename>> {

    return sequence {
        val yEntries = lexes.asSequence().toSerializable(senseResolver)
        yield(yEntries to "entries-all") // yield content with source file base

        val ySynsets = synsets.asSequence().toSerializable()
        yield(ySynsets to "data-all")  // yield content with source file base
    }
}

/**
 * Split data generator
 *
 * @receiver core model
 * @yield content (either lex entries or synsets) to file
 */
fun CoreModel.toSplitSerializable(generated: Boolean = false): Sequence<Pair<Map<String, Any>, Filename>> {

    return sequence {
        lexes
            .groupBy {
                if (generated) {
                    if (it.generated) "entries-generated" else it.lexfile
                } else it.lexfile
            }
            .forEach { (file, lexes) ->
                val yEntries = lexes.asSequence().toSerializable(senseResolver)
                yield(yEntries to file) // yield content with source file base
            }

        synsets
            .sortedBy { it.synsetId }
            .groupBy { it.lexfile }
            .forEach { (lexfile, synsets) ->
                val ySynsets = synsets.asSequence().toSerializable()
                yield(ySynsets to lexfile)  // yield content with source file base
            }
    }
}