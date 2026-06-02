package org.oewntk.model

import org.oewntk.model.InverseRelationFactory.INVERSE_SENSE_RELATIONS_SET
import org.oewntk.model.InverseRelationFactory.INVERSE_SYNSET_RELATIONS_SET
import org.oewntk.model.Lex.Groups.lexByLemmaThenByKey2

const val INCLUDE_LEXFILE = false

typealias Filename = String

/**
 * Pronunciation to OEWN serializable map
 *
 * @receiver pronunciation
 * @return map
 * Keys:
 *  - value
 *  - variety
 */
fun Pronunciation.toOEWNData(): Map<String, Any> {
    return mutableMapOf("value" to value)
        .apply {
            variety?.let { this["variety"] = it }
        }
}

/*
 * Example (Pair<text, source>) to OEWN serializable data
 * @return text string if source is null a map otherwise
 * Keys if a map
    text
    source
*/

// L E X

/**
 * Lex to OEWN serializable map
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
fun Lex.toOEWNData(resolver: (SenseKey) -> Sense?): Map<String, Any> {
    val serializedSenses = senseKeys
        .map { resolver.invoke(it)!! }
        .map(Sense::toOEWNData)
        .toList()
    return mutableMapOf<String, Any>(
        "sense" to serializedSenses,
        // "key2" to key2,
    ).apply {
        forms?.let { this["form"] = it.map { it2 -> it2 }.toList() }
        pronunciations?.let { this["pronunciation"] = it.map(Pronunciation::toOEWNData).toList() }
        if (INCLUDE_LEXFILE) lexfile.let { this["lexfile"] = it }
    }.toSortedMap()
}

// S E N S E

/**
 * Sense to OEWN serializable map
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
fun Sense.toOEWNData(): Map<String, Any> {
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
 * Senses to OEWN serializable list
 *
 * @receiver sequence of senses
 * @return list of serialized senses
 */
fun Sequence<Sense>.toOEWNData(): List<Any> {
    return this
        .map(Sense::toOEWNData)
        .toList()
}

// S Y N S E T

/**
 * Synset to OEWN serializable map
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
fun Synset.toOEWNData(): Map<String, Any> {
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
 * Synsets to OEWN serializable map
 *
 * @receiver sequence of synsets
 * @return map of synset by id
 */
fun Sequence<Synset>.toOEWNData(): Map<SynsetId, Any> = this.associate { it.synsetId to it.toOEWNData() }

/**
 * Synsets by id to OEWN serializable map
 */
fun Map<SynsetId, Synset>.toOEWNData(): Map<SynsetId, Any> = this.mapValues { it.value.toOEWNData() }

// M A P P E D   L E X E S

/**
 * Lexes to OEWN serializable map
 *
 * @receiver sequence of lexes
 * @param senseResolver senseKey to sense resolver
 * @return map by lemma
 */
fun Sequence<Lex>.toOEWNData(senseResolver: (SenseKey) -> Sense): Map<Lemma, Map<Key2, Map<String, Any>>> {
    val hypermap1: HyperMap1 = this.lexByLemmaThenByKey2()
    return hypermap1.toOEWNData(senseResolver)
}

// TODO remove
fun Sequence<Lex>.toOEWNDataAlt(senseResolver: (SenseKey) -> Sense): Map<Lemma, Map<Key2, Map<String, Any>>> {
    return mutableMapOf<String, Map<Key2, Map<String, Any>>>()
        .apply {
            this@toOEWNDataAlt
                .sortedBy { it.lemma }
                .groupBy { it.lemma }
                .forEach { (lemma, lexes) ->
                    this[lemma] = lexes.associate { it.key2 to it.toOEWNData(senseResolver) }
                }
        }
}

/**
 * Hyper map to OEWN serializable map
 *
 * @receiver hypermap1 (lex by lemma then by key2)
 */
fun HyperMap1.toOEWNData(senseResolver: (SenseKey) -> Sense): Map<Lemma, Map<Key2, Map<String, Any>>> {
    return this
        .mapValues { (_: Lemma, v) ->
            v.mapValues { (_: Key2, lex) -> lex.toOEWNData { senseResolver(it) } }.toSortedMap()
        }.toSortedMap()
}

// M O D E L

/**
 * Merged OEWN serializable data generator
 *
 * @receiver core model
 * @yield content (either lex entries or synsets) to file
 */
fun CoreModel.toOneOEWNData(): Sequence<Pair<Map<String, Any>, Filename>> {

    return sequence {
        val lexData = lexes.asSequence().toOEWNData(senseResolver)
        yield(lexData to "entries-all") // yield content with source file base

        val synsetData = synsets.asSequence().toOEWNData()
        yield(synsetData to "data-all")  // yield content with source file base
    }
}

/**
 * Split OEWN serializable data generator
 *
 * @receiver core model
 * @yield content (either lex entries or synsets) to file
 */
fun CoreModel.toSplitOEWNData(generated: Boolean = false): Sequence<Pair<Map<String, Any>, Filename>> {

    return sequence {
        lexes
            .groupBy {
                if (generated) {
                    if (it.generated) "entries-generated" else it.lexfile
                } else it.lexfile
            }
            .forEach { (file, lexes) ->
                val yEntries = lexes.asSequence().toOEWNData(senseResolver)
                yield(yEntries to file) // yield content with source file base
            }

        synsets
            .sortedBy { it.synsetId }
            .groupBy { it.lexfile }
            .forEach { (lexfile, synsets) ->
                val ySynsets = synsets.asSequence().toOEWNData()
                yield(ySynsets to lexfile)  // yield content with source file base
            }
    }
}