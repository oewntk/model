package org.oewntk.model

import org.oewntk.model.InverseRelationFactory.INVERSE_SENSE_RELATIONS_SET
import org.oewntk.model.InverseRelationFactory.INVERSE_SYNSET_RELATIONS_SET
import org.oewntk.model.Lex.Groups.lexByLemmaThenByKey2
import org.oewntk.model.Sense.Companion.SENSE_RELATIONS
import org.oewntk.model.Synset.Companion.SYNSET_RELATIONS

typealias Filename = String

// K E Y S

private const val KEY_ADJPOSITION = "adjposition"
private const val KEY_DEFINITION = "definition"
private const val KEY_DOMAIN = "domain"
private const val KEY_EXAMPLE = "example"
private const val KEY_FORM = "form"
private const val KEY_ID = "id"
private const val KEY_ILI = "ili"
private const val KEY_KEY2 = "key2"
private const val KEY_LEXFILE = "lexfile"
private const val KEY_MEMBERS = "members"
private const val KEY_PART_OF_SPEECH = "partOfSpeech"
private const val KEY_PRONUNCIATION = "pronunciation"
private const val KEY_SENSE = "sense"
private const val KEY_SENSE_EXAMPLE = "sent"
private const val KEY_SOURCE = "source"
private const val KEY_SYNSET = "synset"
private const val KEY_TAGCOUNT = "tagcount"
private const val KEY_TEXT = "text"
private const val KEY_USAGE = "usage"
private const val KEY_VALUE = "value"
private const val KEY_VARIETY = "variety"
private const val KEY_VERBFRAME = "subcat"
private const val KEY_VERBTEMPLATE = "template"
private const val KEY_WIKIDATA = "wikidata"

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

fun examplesFromOEWNData(list: List<Any>): List<Example> {
    val data = safeCast<List<Any>>(list)
    return data.map { example ->
        when (example) {
            is String -> Example(example, null)
            is Map<*, *> -> Example(example[KEY_TEXT] as String, example[KEY_SOURCE] as String?)
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
    return mutableMapOf(KEY_VALUE to value)
        .apply {
            variety?.let { this[KEY_VARIETY] = it }
        }
}

/**
 * Pronunciation from dict
 *
 * @param dict dictionary
 * @return pronunciation
 */
fun pronunciationFromOEWNData(dict: Map<String, Any>): Pronunciation {
    val value = dict[KEY_VALUE] as PronunciationValue
    val variety = dict[KEY_VARIETY] as PronunciationVariety?
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
fun Lex.toOEWNData(resolver: (SenseKey) -> Sense?, includeLexFile: Boolean = false, leaveRedundantRelation: Boolean = false): Map<String, Any> {
    val serializedSenses = senseKeys
        .map { resolver.invoke(it)!! }
        .map { it.toOEWNData(leaveRedundantRelation = leaveRedundantRelation) }
        .toList()
    return mutableMapOf<String, Any>(
        KEY_SENSE to serializedSenses,
        //  KEY_KEY2 to key2,
    ).apply {
        forms?.let { this[KEY_FORM] = it.toList() }
        pronunciations?.let { this[KEY_PRONUNCIATION] = it.map(Pronunciation::toOEWNData).toList() }
        if (includeLexFile) lexfile.let { this[KEY_LEXFILE] = it }
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
    val senseDicts = safeCast<List<Map<String, Any>>>(dict[KEY_SENSE]!!)
    val senseKeys = senseDicts.map { safeCast<SenseKey>(it[KEY_ID]!!) }.toList()
    return Lex(lemma, type, discriminant, senseKeys).apply {
        dict[KEY_FORM]?.let { forms = safeCast<List<String>>(it).toSet() }
        dict[KEY_PRONUNCIATION]?.let { pronunciations = safeCast<List<Map<String, Any>>>(it).map { p -> pronunciationFromOEWNData(p) }.toSet() }
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
fun Sense.toOEWNData(includeVerbTemplates: Boolean = true, includeTagCount: Boolean = true, leaveRedundantRelation: Boolean = false): Map<String, Any> {
    return mutableMapOf<String, Any>(
        KEY_ID to senseKey,
        KEY_SYNSET to synsetId,
    ).apply {
        adjPosition?.let { this[KEY_ADJPOSITION] = it }
        examples?.let { this[KEY_SENSE_EXAMPLE] = it.map { it2 -> if (it2.source == null) it2.text else mapOf(KEY_TEXT to it2.text, KEY_SOURCE to it2.source) } }
        verbFrames?.let { this[KEY_VERBFRAME] = it.toList() }
        if (includeVerbTemplates) verbTemplates?.let { this[KEY_VERBTEMPLATE] = it.toList() }
        relations
            ?.filterNot { leaveRedundantRelation && it.key in INVERSE_SENSE_RELATIONS_SET }
            ?.forEach { (rel: String, target) ->
                this[rel] = target.toList()
            }
        if (includeTagCount) tagCount?.let { this[KEY_TAGCOUNT] = it }
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
    val senseId: SenseKey = safeCast(dict[KEY_ID]!!)
    val synsetId: SynsetId = safeCast(dict[KEY_SYNSET]!!)
    val indexInLex: Int = idx
    val examples = dict[KEY_SENSE_EXAMPLE]?.let { examplesFromOEWNData(safeCast(it)) }
    val verbFrames: List<VerbFrameId>? = dict[KEY_VERBFRAME]?.let { safeCast(it) }
    val verbTemplates: List<VerbTemplateId>? = dict[KEY_VERBTEMPLATE]?.let { safeCast(it) }
    val adjPosition: AdjPosition? = safeNullableCast(dict[KEY_ADJPOSITION])
    val tagCount: Int? = safeNullableCast(dict[KEY_TAGCOUNT])
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
fun Sequence<Sense>.toOEWNData(leaveRedundantRelation: Boolean = false): List<Any> {
    return this
        .map { it.toOEWNData(leaveRedundantRelation = leaveRedundantRelation) }
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
fun Synset.toOEWNData(includeLexFile: Boolean = false, leaveRedundantRelation: Boolean = false): Map<String, Any> {
    return mutableMapOf(
        KEY_PART_OF_SPEECH to type.value,
        KEY_DEFINITION to definitions,
        KEY_MEMBERS to members.toList(),
        KEY_DOMAIN to domain,
    ).apply {
        examples?.let { this[KEY_EXAMPLE] = it.map { it2 -> if (it2.source == null) it2.text else mapOf(KEY_SOURCE to it2.source, KEY_TEXT to it2.text) } }
        usages?.let { this[KEY_USAGE] = it }
        relations
            ?.filterNot { leaveRedundantRelation && it.key in INVERSE_SYNSET_RELATIONS_SET }
            ?.forEach { (rel, target) ->
                this[rel] = target.toList()
            }
        wikidata?.let { if (it.isNotEmpty()) this[KEY_WIKIDATA] = if (it.size == 1) it[0] else it }
        ili?.let { this[KEY_ILI] = it }
        source?.let { this[KEY_SOURCE] = it }
        if (includeLexFile) this[KEY_LEXFILE] = lexfile
    }.toSortedMap()
}

/**
 * Synset from dict
 *
 * @param dict dictionary
 * @return synset
 */
fun synsetFromOEWNData(synsetId: SynsetId, dict: Map<String, Any>, includeLexFile: Boolean = false): Synset {

    val type = SynsetType.fromKey2(dict[KEY_PART_OF_SPEECH] as String)
    val domain = dict[KEY_DOMAIN] as Domain
    val members = safeCast<List<Lemma>>(dict[KEY_MEMBERS]!!)
    val definitions = safeCast<List<String>>(dict[KEY_DEFINITION]!!)
    val examples = dict[KEY_EXAMPLE]?.let { examplesFromOEWNData(safeCast(it)) }
    val usages = dict[KEY_USAGE]?.let { safeCast<List<String>>(it) }
    val relations: Map<Relation, Set<SenseKey>>? = synsetRelationsFromOEWNData(dict)
    val ili = dict[KEY_ILI] as String?
    val wikidata = dict[KEY_WIKIDATA]?.let {
        when (it) {
            is String -> listOf(it)
            is List<*> -> safeCast(it)
            else -> throw IllegalArgumentException(it.toString())
        }
    }
    val source = dict[KEY_SOURCE] as String?
    val lexfile = dict[KEY_LEXFILE] as String?
    return Synset(
        synsetId, type, domain, members.toSet(), definitions,
        examples = examples,
        usages = usages,
        relations = relations,
        ili = ili,
        wikidata = wikidata,
    ).apply {
        this.source = source
        if (includeLexFile && lexfile != null) this.lexfile = lexfile
    }
}

/**
 * Synsets to OEWN serializable dict
 *
 * @receiver sequence of synsets
 * @return dict of synset by id
 */
fun Sequence<Synset>.toOEWNData(leaveRedundantRelation: Boolean = false): Map<SynsetId, Any> = this.associate { it.synsetId to it.toOEWNData(leaveRedundantRelation = leaveRedundantRelation) }

/**
 * Synsets by id to OEWN serializable dict
 */
fun Map<SynsetId, Synset>.toOEWNData(leaveRedundantRelation: Boolean = false): Map<SynsetId, Any> = this.mapValues { it.value.toOEWNData(leaveRedundantRelation = leaveRedundantRelation) }

// M A P P E D   L E X E S

/**
 * Lexes to OEWN serializable dict
 *
 * @receiver sequence of lexes
 * @param senseResolver senseKey to sense resolver
 * @return dict by lemma
 */
fun Sequence<Lex>.toOEWNData(senseResolver: (SenseKey) -> Sense, leaveRedundantRelation: Boolean = false): Map<Lemma, Map<Key2, Map<String, Any>>> {
    val hypermap1: HyperMap1 = this.lexByLemmaThenByKey2()
    return hypermap1.toOEWNData(senseResolver, leaveRedundantRelation = leaveRedundantRelation)
}

fun Sequence<Lex>.toOEWNDataAlt(senseResolver: (SenseKey) -> Sense, leaveRedundantRelation: Boolean = false): Map<Lemma, Map<Key2, Map<String, Any>>> {
    return mutableMapOf<String, Map<Key2, Map<String, Any>>>()
        .apply {
            this@toOEWNDataAlt
                .sortedBy { it.lemma }
                .groupBy { it.lemma }
                .forEach { (lemma, lexes) ->
                    this[lemma] = lexes.associate { it.key2 to it.toOEWNData(senseResolver, leaveRedundantRelation = leaveRedundantRelation) }
                }
        }
}

/**
 * Hyper dict to OEWN serializable dict
 *
 * @receiver hypermap1 (lex by lemma then by key2)
 */
fun HyperMap1.toOEWNData(senseResolver: (SenseKey) -> Sense, leaveRedundantRelation: Boolean = false): Map<Lemma, Map<Key2, Map<String, Any>>> {
    return this
        .mapValues { (_: Lemma, v) ->
            v.mapValues { (_: Key2, lex) -> lex.toOEWNData(resolver = { senseResolver(it) }, leaveRedundantRelation = leaveRedundantRelation) }.toSortedMap()
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
        val senseDicts = safeCast<List<Map<String, Any>>>(lexDict[KEY_SENSE]!!)
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
fun CoreModel.toOneOEWNData(leaveRedundantRelation: Boolean = false): Sequence<Pair<Map<String, Any>, Filename>> {

    return sequence {
        val lexData = lexes.asSequence().toOEWNData(senseResolver, leaveRedundantRelation = leaveRedundantRelation)
        yield(lexData to "entries-all") // yield content with source file base

        val synsetData = synsets.asSequence().toOEWNData(leaveRedundantRelation = leaveRedundantRelation)
        yield(synsetData to "data-all")  // yield content with source file base
    }
}

/**
 * Split OEWN serializable data generator
 *
 * @receiver core model
 * @yield content (either lex entries or synsets) to file
 */
fun CoreModel.toSplitOEWNData(generated: Boolean = false, leaveRedundantRelation: Boolean = false): Sequence<Pair<Map<String, Any>, Filename>> {

    return sequence {
        lexes
            .groupBy {
                if (generated) {
                    if (it.generated) "entries-generated" else it.lexfile
                } else it.lexfile
            }
            .forEach { (file, lexes) ->
                val lexData = lexes.asSequence().toOEWNData(senseResolver, leaveRedundantRelation = leaveRedundantRelation)
                yield(lexData to file) // yield content with source file base
            }

        synsets
            .sortedBy { it.synsetId }
            .groupBy { it.lexfile }
            .forEach { (lexfile, synsets) ->
                val synsetData = synsets.asSequence().toOEWNData(leaveRedundantRelation = leaveRedundantRelation)
                yield(synsetData to lexfile)  // yield content with source file base
            }
    }
}