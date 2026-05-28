package org.oewntk.model

import org.oewntk.model.InverseRelationFactory.INVERSE_SENSE_RELATIONS_SET
import org.oewntk.model.InverseRelationFactory.INVERSE_SYNSET_RELATIONS_SET

const val INCLUDE_LEXFILE = false

typealias SData = Map<String, Any>
typealias Filename = String

/**
 * Pronunciation to serializable map
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

/**
 * Lex to serializable map
 * @param resolver senseKey to sense resolver
 * @return map
 * Keys:
 * - form
 * - pronunciation
 * - sense
 * - source
 */
fun Lex.toSerializable(resolver: (SenseKey) -> Sense?): Map<Key2, Any> {
    return mutableMapOf<String, Any>(
        "sense" to senseKeys.map { resolver.invoke(it)!! }.map(Sense::toSerializable).toList(),
        // "key2" to key2,
    ).apply {
        forms?.let { this["form"] = it.map { it2 -> it2 }.toList() }
        pronunciations?.let { this["pronunciation"] = it.map(Pronunciation::toSerializable).toList() }
        if (INCLUDE_LEXFILE) lexfile.let { this["lexfile"] = it }
    }.toSortedMap()
}

/**
 * Sense to serializable map
 * @return map
 * Keys:
 * - id
 * - synset
 * - adjposition
 * - subcat
 * - sent
 * - <relations>
 */
fun Sense.toSerializable(): Map<SenseKey, Any> {
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
 * Synset to serializable map
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
fun Synset.toSerializable(): Map<SynsetId, Any> {
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
 * Entries to serializable map
 * @param entries entries sequence of entries
 * @param resolver senseKey to sense resolver
 * @return map of entries by lemma
 */
fun entriesToSerializable(entries: Sequence<LexEntry>, resolver: (SenseKey) -> Sense?): Map<Key2, Any> {
    return mutableMapOf<Key2, Any>().apply {
        entries.forEach { (lemma, lexes) ->
            this[lemma] = lexes.associate { it.key2 to it.toSerializable(resolver) }
        }
    }
}

/**
 * Lexes to serializable map
 * @param lexes lexes sequence of lexes
 * @param resolver senseKey to sense resolver
 * @return list of lexes
 */
fun lexesToSerializable(lexes: Sequence<Lex>, resolver: (SenseKey) -> Sense?): List<Any> {
    fun Lex.toSerializable(): Map<String, Any> = this@toSerializable.toSerializable { resolver.invoke(it)!! }
    return lexes
        .map(Lex::toSerializable)
        .toList()
}

/**
 * Senses to serializable map
 * @param senses senses sequence of senses
 * @return list of sense
 */
fun sensesToSerializable(senses: Sequence<Sense>): List<Any> {
    return senses
        .map(Sense::toSerializable)
        .toList()
}

/**
 * Synsets to serializable map
 * @param synsets sequence of synsets
 * @return map of synset by id
 */
fun synsetsToSerializable(synsets: Sequence<Synset>): Map<SynsetId, Any> {
    return synsets.associate { it.synsetId to it.toSerializable() }
}

/**
 * Flat data producer
 *
 * @param whichEntries which entries to select, by default all
 * @param whichSynsets which synsets, by default all
 * @receiver core model
 * @return lex entries and synsets
 */
fun CoreModel.toFlatSerializable(
    whichEntries: Sequence<LexEntry> = lexEntries.sortedBy { it.key },
    whichSynsets: Sequence<Synset> = synsets.asSequence().sortedBy { it.synsetId },
): Pair<SData, SData> {
    val yEntries : Map<Key2, Any> = entriesToSerializable(whichEntries, senseResolver)
    val ySynsets: Map<SynsetId, Any> = synsetsToSerializable(whichSynsets)
    return yEntries to ySynsets
}

/**
 * Split data generator
 *
 * @receiver core model
 * @yield content (either lex entries or synsets) to file
 */
fun CoreModel.toSplitSerializable(generated: Boolean = false): Sequence<Pair<SData, Filename>> {
    fun Lex.toSerializable(): Map<String, Any> = toSerializable(senseResolver)

    return sequence {
        lexes
            .groupBy {
                if (generated) {
                    if (it.generated) "entries-generated" else it.lexfile
                } else it.lexfile
            }
            .forEach { (file, lexes) ->
                val yEntries = mutableMapOf<String, Any>().apply {
                    lexes
                        .sortedBy { it.lemma }
                        .groupBy { it.lemma }
                        .forEach { (lemma, lexes) ->
                            this[lemma] = lexes.associate { it.key2 to it.toSerializable() }
                        }
                }
                yield(yEntries to file) // yield content with source file base
            }

        synsets
            .sortedBy { it.synsetId }
            .groupBy { it.lexfile }
            .forEach { (lexfile, synsets) ->
                val ySynsets = synsets.associate { it.synsetId to it.toSerializable() }
                yield(ySynsets to lexfile)  // yield content with source file base
            }
    }
}