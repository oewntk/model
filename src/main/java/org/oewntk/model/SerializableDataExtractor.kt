package org.oewntk.model

// O B J E C T

/**
 * Avoid warning
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> safeCast(value: Any): T {
    return value as T
}

/**
 * Avoid warning
 */
@Suppress("UNCHECKED_CAST")
fun <T> safeNullableCast(value: Any?): T? {
    return value as T
}

/**
 * Pronunciation to OEWN serializable map
 *
 * @receiver pronunciation
 * @return map
 * Keys:
 *  - lemma
 *  - type
 *  - optional
 */
fun LexId.toData(): Map<String, Any> {
    return mutableMapOf(
        "lemma" to lemma,
        "type" to type.value,
    )
        .apply {
            discriminant?.let { this["discriminant"] = it }
        }
}

fun lexIdFromData(map: Map<String, Any>): LexId {
    val lemma = map["lemma"] as Lemma
    val type = SynsetType.fromKey2(map["type"] as String)
    val discriminant = map["discriminant"] as Discriminant?
    return LexId(lemma, type, discriminant)
}

/**
 * Pronunciation to OEWN serializable map
 *
 * @receiver pronunciation
 * @return map
 * Keys:
 *  - value
 *  - variety
 */
fun Pronunciation.toData(): Map<String, Any> {
    return mutableMapOf("value" to value)
        .apply {
            variety?.let { this["variety"] = it }
        }
}

fun pronunciationFromData(map: Map<String, Any>): Pronunciation {
    val value = map["value"] as PronunciationValue
    val variety = map["variety"] as PronunciationVariety?
    return Pronunciation(value, variety)
}

/**
 * Lex to serializable map
 *
 * @preceiver lex
 * @return serializable map
 */
fun Lex.toData(): Map<String, Any> {
    return mutableMapOf(
        "lemma" to lemma,
        "type" to type.value,
        "sense" to senseKeys,
    ).apply {
        discriminant?.let { this["discriminant"] = it }
        pronunciations?.let { this["pronunciation"] = it.map { pronunciation -> pronunciation.toData() } }
    }
}

fun lexFromData(map: Map<String, Any>): Lex {
    val lexId = lexIdFromData(map)
    val senseKeys = safeCast<List<SenseKey>>(map["sense"]!!)
    return Lex(lexId.lemma, lexId.type, lexId.discriminant, senseKeys).apply {
        map["pronunciation"]?.let { pronunciations = safeCast<List<Map<String, Any>>>(it).map { p -> pronunciationFromData(p) }.toSet() }
    }
}

/**
 * Synset to serializable map
 *
 * @preceiver synset
 * @return serializable map
 */
fun Synset.toData(): Map<String, Any> {
    return mutableMapOf(
        "id" to synsetId,
        "type" to type.value,
        "domain" to domain,
        "member" to members.toList(),
        "definition" to (definitions.toList()),
    ).apply {
        examples?.let { this["examples"] = it.map { example -> if (example.second == null) example.first else mapOf("text" to example.first, "source" to example.second) }.toList() }
        usages?.let { this["usages"] = it }
        relations?.let { this["relation"] = it }
        ili?.let { this["ili"] = it }
        wikidata?.let { this["wikidata"] = it.joinToString(separator = ";") }
    }
}

fun synsetFromData(map: Map<String, Any>): Synset {
    val synsetId = map["id"] as SynsetId
    val type = SynsetType.fromKey2(map["type"] as String)
    val domain = map["domain"] as Domain
    val members = safeCast<List<Lemma>>(map["member"]!!)
    val definitions = safeCast<List<String>>(map["definition"]!!)
    val examples = map["example"]?.let { safeCast<List<Pair<String, String?>>>(it) }
    val usages = map["usage"]?.let { safeCast<List<String>>(it) }
    val relations = map["relation"]?.let { safeCast<Map<Relation, List<SenseKey>>>(it).mapValues { e -> e.value.toSet() } }
    val ili = map["ili"] as String?
    val wikidata = map["wikidata"]?.let { safeCast<String>(it).split(";") }
    return Synset(synsetId, type, domain, members.toTypedArray(), definitions.toTypedArray(), examples?.toTypedArray(), usages?.toTypedArray(), relations, ili, wikidata)
}

/**
 * Sense to serializable map
 *
 * @preceiver sense
 * @return serializable map
 */
fun Sense.toData(): Map<String, Any> {
    return mutableMapOf<String, Any>(
        "id" to senseId,
        "index" to indexInLex,
        "synset" to synsetId,
    )
        .apply {
            putAll(lexId.toData())
            examples?.let { this["examples"] = it.map { example -> if (example.second == null) example.first else mapOf("text" to example.first, "source" to example.second) }.toList() }
            verbFrames?.let { this["verbFrames"] = it.joinToString(separator = ";") }
            adjPosition?.let { this["adjPosition"] = it }
            relations?.let {
                val r = it
                //.mapValues { e -> e.value.toList() }
                //.toMap()
                this["relation"] = r
            }
        }
}

fun senseFromData(map: Map<String, Any>): Sense {
    val id = map["id"] as SenseKey
    val synsetId = map["synset"] as SynsetId
    val lexId = lexIdFromData(map)
    val index = map["index"] as Int
    val examples = map["definition"]?.let { safeCast<List<Pair<String, String?>>>(it) }
    val verbFrames = map["verbFrames"]?.let { safeCast<VerbFrameId>(it).split(";") }
    val verbTemplates = map["verbTemplates"]?.let { safeCast<String>(it).split(";").map(String::toInt) }
    val adjPosition = map["adjPosition"] as String?
    val tagCount = map["tagCount"] as Int?
    val relations = map["relation"]?.let { safeCast<Map<Relation, List<SenseKey>>>(it).mapValues { e -> e.value.toSet() } }
    return Sense(id, lexId, synsetId, index, examples?.toTypedArray(), verbFrames?.toTypedArray(), verbTemplates?.toTypedArray(), adjPosition, tagCount, relations)
}

// S E Q U E N C E S   O F   O B J E C T S

/**
 * Lexes to serializable hypermap
 *
 * @preceiver sequence of lexes
 * @return lex serializable hypermap
 */
fun Sequence<Lex>.toLexesData(): Sequence<Map<String, Any>> = map { it.toData() }

/**
 * Synsets to serializable map
 *
 * @preceiver sequence of synsets
 * @return synset serializable map
 */
fun Sequence<Synset>.toSynsetsData(): Sequence<Map<String, Any>> = map { it.toData() }

/**
 * Sense to serializable map
 *
 * @preceiver sequence of senses
 * @return sense serializable map
 */
fun Sequence<Sense>.toSensesData(): Sequence<Map<String, Any>> = map { it.toData() }

// M O D E L

/**
 * Raw data producer
 *
 * @param whichLexes which entries to select, by default all
 * @param whichSynsets which synsets, by default all
 *
 * @receiver core model
 * @return lexes, synsets and senses as serializable maps
 */
fun CoreModel.toData(
    whichLexes: Sequence<Lex> = lexes.asSequence().sortedWith(compareBy(Lex::lemma).thenBy(Lex::key2)),
    whichSynsets: Sequence<Synset> = synsets.asSequence().sortedBy { it.synsetId },
    whichSenses: Sequence<Sense> = senses.asSequence().sortedBy { it.senseKey },
): Triple<List<Any>, List<Any>, List<Any>> {
    val lexesData: List<Map<String, Any>> = whichLexes.toLexesData().toList()
    val synsetsData: List<Map<String, Any>> = whichSynsets.toSynsetsData().toList()
    val sensesData: List<Map<String, Any>> = whichSenses.toSensesData().toList()
    return Triple(lexesData, synsetsData, sensesData)
}
