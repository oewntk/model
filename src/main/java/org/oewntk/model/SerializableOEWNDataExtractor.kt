package org.oewntk.model

import org.oewntk.model.InverseRelationFactory.INVERSE_SENSE_RELATIONS_SET
import org.oewntk.model.InverseRelationFactory.INVERSE_SYNSET_RELATIONS_SET
import org.oewntk.model.Lex.Groups.lexByLemmaThenByKey2
import org.oewntk.model.Sense.Companion.SENSE_RELATIONS
import org.oewntk.model.Synset.Companion.SYNSET_RELATIONS

typealias Filename = String

// U T I L S

fun keyPairs(dict: Map<Lemma, Map<Key2, Map<String, Any>>>): Sequence<Pair<Lemma, Key2>> = dict
    .asSequence()
    .flatMap { (key1, inner) -> inner.keys.map { key2 -> key1 to key2 } }

fun entries(dict: Map<Lemma, Map<Key2, Map<String, Any>>>): Sequence<Triple<Lemma, Key2, Any>> = dict
    .asSequence()
    .flatMap { (key1, inner) ->
        inner.map { (key2, thing) -> Triple(key1, key2, thing) }
    }

// O B J E C T S

fun examplesFromOEWNData(list: List<Any>): List<Pair<String, String?>> {
    val data = safeCast<List<Any>>(list)
    return data.map { example ->
        when (example) {
            is String -> example to null
            is Map<*, *> -> example["source"] as String to example["text"] as String?
            else -> throw IllegalArgumentException(example.toString())
        }
    }.toList()
}

fun synsetRelationsFromOEWNData(dict: Map<Relation, Any>): Map<Relation, Set<SynsetId>>? {
    val relations = safeCast<Map<Relation, List<SynsetId>>>(dict)
        .filter { it.key in SYNSET_RELATIONS }
        .mapValues { it.value.toSet() }
    return relations.ifEmpty { null }
}

fun senseRelationsFromOEWNData(dict: Map<Relation, Any>): Map<Relation, Set<SenseKey>>? {
    val relations = safeCast<Map<Relation, List<SenseKey>>>(dict)
        .filter { it.key in SENSE_RELATIONS }
        .mapValues { it.value.toSet() }
    return relations.ifEmpty { null }
}

/**
 * Pronunciation to OEWN serializable dict
 *
 * @receiver pronunciation
 * @return dict
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

/**
 * Pronunciation from dict
 *
 * @param dict dictionary
 * @return pronunciation
 */
fun pronunciationFromOEWNData(dict: Map<String, Any>): Pronunciation {
    val value = dict["value"] as PronunciationValue
    val variety = dict["variety"] as PronunciationVariety?
    return Pronunciation(value, variety)
}

/*
 * Example (Pair<text, source>) to OEWN serializable data
 * @return text string if source is null a dict otherwise
 * Keys if a dict
    text
    source
*/

// L E X

/**
 * Lex to OEWN serializable dict
 *
 * @receiver lex
 * @param resolver senseKey to sense resolver
 * @return dict
 * Keys:
 * - form
 * - pronunciation
 * - sense
 * - source
 */
fun Lex.toOEWNData(resolver: (SenseKey) -> Sense?, includeLexFile: Boolean = false): Map<String, Any> {
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
        if (includeLexFile) lexfile.let { this["lexfile"] = it }
    }.toSortedMap()
}

/**
 * Lex from dict
 *
 * @param lemma lemma
 * @param type synset type
 * @param discriminant discriminant
 * @param dict dictionary
 * @return lex
 */
fun lexFromOEWNData(lemma: Lemma, type: SynsetType, discriminant: Discriminant?, dict: Map<String, Any>): Lex {
    val senseDicts = safeCast<List<Map<String, Any>>>(dict["sense"]!!)
    val senseKeys = senseDicts.map { safeCast<SenseKey>(it["id"]!!) }.toList()
    return Lex(lemma, type, discriminant, senseKeys).apply {
        dict["pronunciation"]?.let { pronunciations = safeCast<List<Map<String, Any>>>(it).map { p -> pronunciationFromOEWNData(p) }.toSet() }
    }
}

// S E N S E

/**
 * Sense to OEWN serializable dict
 *
 * @receiver sense
 * @return dict
 * Keys:
 * - id
 * - synset
 * - adjposition
 * - subcat
 * - sent
 * - <relations>
 */
fun Sense.toOEWNData(includeVerbTemplates: Boolean = true, includeTagCount: Boolean = true): Map<String, Any> {
    return mutableMapOf<String, Any>(
        "id" to senseKey,
        "synset" to synsetId,
    ).apply {
        adjPosition?.let { this["adjposition"] = it }
        examples?.let { this["sent"] = it.map { it2 -> if (it2.second == null) it2.first else mapOf("text" to it2.first, "source" to it2.second) } }
        verbFrames?.let { this["subcat"] = it }
        if (includeVerbTemplates) verbTemplates?.let { this["template"] = it }
        relations
            ?.filterNot { it.key in INVERSE_SENSE_RELATIONS_SET }
            ?.forEach { (rel: String, target) ->
                this[rel] = target.toList()
            }
        if (includeTagCount) tagCount?.let { this["tagcount"] = it }
    }.toSortedMap()
}

/**
 * Lex from dict
 *
 * @param lemma lemma
 * @param type synset type
 * @param discriminant discriminant
 * @param dict dictionary
 * @return sense
 */
fun senseFromOEWNData(lemma: Lemma, type: SynsetType, discriminant: Discriminant?, idx: Int, dict: Map<String, Any>): Sense {
    val lexId: LexId = LexId(lemma, type, discriminant)
    val senseId: SenseKey = safeCast(dict["id"]!!)
    val synsetId: SynsetId = safeCast(dict["synset"]!!)
    val indexInLex: Int = idx
    val examples = dict["example"]?.let { examplesFromOEWNData(safeCast(it)) }
    val verbFrames: List<VerbFrameId>? = dict["subcat"]?.let { safeCast(it) }
    val verbTemplates: List<VerbTemplateId>? = dict["template"]?.let { safeCast(it) }
    val adjPosition: AdjPosition? = safeNullableCast(dict["adjposition"])
    val tagCount: Int? = safeNullableCast(dict["tagcount"])
    val relations: Map<Relation, Set<SenseKey>>? = senseRelationsFromOEWNData(dict)
    return Sense(
        senseId, lexId, synsetId, indexInLex,
        examples = examples,
        adjPosition = adjPosition,
        verbFrames = verbFrames?.toSet(),
        verbTemplates = verbTemplates?.toSet(),
        tagCount = tagCount,
        relations = relations,
    ).apply {

    }
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
 * Synset to OEWN serializable dict
 *
 * @receiver synset
 * @return dict
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
fun Synset.toOEWNData(includeLexFile: Boolean = false): Map<String, Any> {
    return mutableMapOf(
        "partOfSpeech" to type.value,
        "definition" to definitions,
        "members" to members.toList(),
        "domain" to domain,
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
        if (includeLexFile) this["lexfile"] = lexfile
    }.toSortedMap()
}

/**
 * Synset from dict
 *
 * @param dict dictionary
 * @return synset
 */
fun synsetFromOEWNData(synsetId: SynsetId, dict: Map<String, Any>): Synset {

    val type = SynsetType.fromKey2(dict["partOfSpeech"] as String)
    val domain = dict["domain"] as Domain
    val members = safeCast<List<Lemma>>(dict["members"]!!)
    val definitions = safeCast<List<String>>(dict["definition"]!!)
    val examples = dict["example"]?.let { examplesFromOEWNData(safeCast(it)) }
    val usages = dict["usage"]?.let { safeCast<List<String>>(it) }
    val relations: Map<Relation, Set<SenseKey>>? = synsetRelationsFromOEWNData(dict)
    val ili = dict["ili"] as String?
    val wikidata = dict["wikidata"]?.let {
        when (it) {
            is String -> listOf(it)
            is List<*> -> safeCast(it)
            else -> throw IllegalArgumentException(it.toString())
        }
    }
    return Synset(
        synsetId, type, domain, members.toSet(), definitions,
        examples = examples,
        usages = usages,
        relations = relations,
        ili = ili,
        wikidata = wikidata
    )
}

/**
 * Synsets to OEWN serializable dict
 *
 * @receiver sequence of synsets
 * @return dict of synset by id
 */
fun Sequence<Synset>.toOEWNData(): Map<SynsetId, Any> = this.associate { it.synsetId to it.toOEWNData() }

/**
 * Synsets by id to OEWN serializable dict
 */
fun Map<SynsetId, Synset>.toOEWNData(): Map<SynsetId, Any> = this.mapValues { it.value.toOEWNData() }

// M A P P E D   L E X E S

/**
 * Lexes to OEWN serializable dict
 *
 * @receiver sequence of lexes
 * @param senseResolver senseKey to sense resolver
 * @return dict by lemma
 */
fun Sequence<Lex>.toOEWNData(senseResolver: (SenseKey) -> Sense): Map<Lemma, Map<Key2, Map<String, Any>>> {
    val hypermap1: HyperMap1 = this.lexByLemmaThenByKey2()
    return hypermap1.toOEWNData(senseResolver)
}

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
 * Hyper dict to OEWN serializable dict
 *
 * @receiver hypermap1 (lex by lemma then by key2)
 */
fun HyperMap1.toOEWNData(senseResolver: (SenseKey) -> Sense): Map<Lemma, Map<Key2, Map<String, Any>>> {
    return this
        .mapValues { (_: Lemma, v) ->
            v.mapValues { (_: Key2, lex) -> lex.toOEWNData(resolver = { senseResolver(it) }) }.toSortedMap()
        }.toSortedMap()
}

/**
 * Lexes and senses from dictionary
 */
fun lexesAndSensesFromOEWNData(dict: Map<Lemma, Map<Key2, Map<String, Any>>>): Pair<Sequence<Lex>, Sequence<Sense>> {

    // lexes
    val lexes = entries(dict).map {
        val (lemma, key2, value2) = it
        val lexDict = safeCast<Map<String, Any>>(value2)
        lexFromOEWNData(lemma, SynsetType.fromKey2(key2), SynsetType.discriminantFromKey2(key2), lexDict)
    }
    // senses
    val senses = entries(dict).flatMap {
        val (lemma, key2, value2) = it
        val lexDict = safeCast<Map<String, Any>>(value2)
        val senseDicts = safeCast<List<Map<String, Any>>>(lexDict["sense"]!!)
        senseDicts.withIndex().map { (idx, senseDict) ->
            senseFromOEWNData(lemma, SynsetType.fromKey2(key2), SynsetType.discriminantFromKey2(key2), idx, senseDict)
        }
    }

    return lexes to senses
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
                val lexData = lexes.asSequence().toOEWNData(senseResolver)
                yield(lexData to file) // yield content with source file base
            }

        synsets
            .sortedBy { it.synsetId }
            .groupBy { it.lexfile }
            .forEach { (lexfile, synsets) ->
                val synsetData = synsets.asSequence().toOEWNData()
                yield(synsetData to lexfile)  // yield content with source file base
            }
    }
}