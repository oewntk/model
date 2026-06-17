package org.oewntk.model

// O B J E C T S

/**
 * SynsetType from dict
 *
 * @return type
 */
fun typeFromData(dict: Map<String, Any>): SynsetType {
    return dict["type"].let {
        when (it) {
            is String -> SynsetType.fromKey2(it)
            is Char -> SynsetType.fromChar(it)
            else -> throw IllegalArgumentException(it.toString())
        }
    }
}

/**
 * Example from dict
 *
 * @return list of examples
 */
fun examplesFromData(list: List<Any>): List<Pair<String, String?>> {
    val data = safeCast<List<Any>>(list)
    return data.map { example ->
        when (example) {
            is String -> example to null
            is Map<*, *> -> example["text"] as String to example["source"] as String?
            else -> throw IllegalArgumentException(example.toString())
        }
    }.toList()
}

/**
 * Pronunciation to serializable dict
 *
 * @receiver pronunciation
 * @return dict
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

/**
 * Pronunciation from dict
 *
 * @param dict dictionary
 * @return pronunciation
 */
fun pronunciationFromData(dict: Map<String, Any>): Pronunciation {
    val value = dict["value"] as PronunciationValue
    val variety = dict["variety"] as PronunciationVariety?
    return Pronunciation(value, variety)
}


/**
 * Lex id to serializable dictionary
 *
 * @receiver lex id
 * @return dictionary
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

/**
 * LexId from dict
 *
 * @param dict dictionary
 * @return lex id
 */
fun lexIdFromData(dict: Map<String, Any>): LexId {
    val lemma = dict["lemma"] as Lemma
    val type: SynsetType = typeFromData(dict)
    val discriminant = dict["discriminant"] as Discriminant?
    return LexId(lemma, type, discriminant)
}

/**
 * Lex to serializable dict
 *
 * @preceiver lex
 * @return serializable dict
 */
fun Lex.toData(): Map<String, Any> {
    return mutableMapOf(
        "lemma" to lemma,
        "type" to type.value,
        "sense" to senseKeys,
    ).apply {
        discriminant?.let { this["discriminant"] = it }
        forms?.let { this["form"] = it.toList() }
        pronunciations?.let { this["pronunciation"] = it.map { pronunciation -> pronunciation.toData() } }
    }
}

/**
 * Lex from dict
 *
 * @param dict dictionary
 * @return lex
 */
fun lexFromData(dict: Map<String, Any>): Lex {
    val lexId = lexIdFromData(dict)
    val senseKeys = safeCast<List<SenseKey>>(dict["sense"]!!)
    return Lex(lexId.lemma, lexId.type, lexId.discriminant, senseKeys).apply {
        dict["form"]?.let { forms = safeCast<List<String>>(it).toSet() }
        dict["pronunciation"]?.let { pronunciations = safeCast<List<Map<String, Any>>>(it).map { p -> pronunciationFromData(p) }.toSet() }
    }
}

/**
 * Synset to serializable dict
 *
 * @preceiver synset
 * @return serializable dict
 */
fun Synset.toData(includeLexFile: Boolean = false): Map<String, Any> {
    return mutableMapOf(
        "id" to synsetId,
        "type" to type.value,
        "domain" to domain,
        "member" to members.toList(),
        "definition" to (definitions.toList()),
    ).apply {
        examples?.let { this["example"] = it.map { example -> if (example.second == null) example.first else mapOf("text" to example.first, "source" to example.second) }.toList() }
        usages?.let { this["usages"] = it }
        relations?.let { this["relation"] = it }
        ili?.let { this["ili"] = it }
        wikidata?.let { this["wikidata"] = it.joinToString(separator = ";") }
        source?.let { this["source"] = it }
        if (includeLexFile) this["lexfile"] = lexfile
    }
}

/**
 * Synset from dict
 *
 * @param dict dictionary
 * @return synset
 */
fun synsetFromData(dict: Map<String, Any>, includeLexFile: Boolean = false): Synset {
    val synsetId = dict["id"] as SynsetId
    val type = typeFromData(dict)
    val domain = dict["domain"] as Domain
    val members = safeCast<List<Lemma>>(dict["member"]!!)
    val definitions = safeCast<List<String>>(dict["definition"]!!)
    val examples = dict["example"]?.let { examplesFromData(safeCast(it)) }
    val usages = dict["usage"]?.let { safeCast<List<String>>(it) }
    val relations = dict["relation"]?.let { safeCast<Map<Relation, List<SenseKey>>>(it).mapValues { e -> e.value.toSet() } }
    val ili = dict["ili"] as String?
    val wikidata = dict["wikidata"]?.let { safeCast<String>(it).split(";") }
    val source = dict["source"] as String?
    val lexfile = dict["lexfile"] as String?
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
 * Sense to serializable dict
 *
 * @preceiver sense
 * @return serializable dict
 */
fun Sense.toData(): Map<String, Any> {
    return mutableMapOf<String, Any>(
        "id" to senseId,
        "index" to indexInLex,
        "synset" to synsetId,
    )
        .apply {
            putAll(lexId.toData())
            examples?.let { this["example"] = it.map { example -> if (example.second == null) example.first else mapOf("text" to example.first, "source" to example.second) }.toList() }
            verbFrames?.let { this["verbFrame"] = it.joinToString(separator = ";") }
            verbTemplates?.let { this["verbTemplate"] = it.joinToString(separator = ";") }
            adjPosition?.let { this["adjPosition"] = it }
            tagCount?.let { this["tagcount"] = it }
            relations?.let { this["relation"] = it }
        }
}

/**
 * Sense from dict
 *
 * @param dict dictionary
 * @return sense
 */
fun senseFromData(dict: Map<String, Any>): Sense {
    val id = dict["id"] as SenseKey
    val synsetId = dict["synset"] as SynsetId
    val lexId = lexIdFromData(dict)
    val index = dict["index"] as Int
    val examples = dict["example"]?.let { examplesFromData(safeCast(it)) }
    val verbFrames = dict["verbFrame"]?.let { safeCast<VerbFrameId>(it).split(";") }?.toSet()
    val verbTemplates = dict["verbTemplate"]?.let { safeCast<String>(it).split(";").map(String::toInt) }?.toSet()
    val adjPosition = dict["adjPosition"] as String?
    val tagCount = dict["tagcount"] as Int?
    val relations = dict["relation"]?.let { safeCast<Map<Relation, List<SenseKey>>>(it).mapValues { e -> e.value.toSet() } }
    return Sense(id, lexId, synsetId, index, examples, verbFrames, verbTemplates, adjPosition, tagCount, relations)
}

// S E Q U E N C E S   O F   O B J E C T S

/**
 * Lexes to serializable hyperdict
 *
 * @preceiver sequence of lexes
 * @return lex serializable hyperdict
 */
fun Sequence<Lex>.toLexesData(): Sequence<Map<String, Any>> = map { it.toData() }

/**
 * Synsets to serializable dict
 *
 * @preceiver sequence of synsets
 * @return synset serializable dict
 */
fun Sequence<Synset>.toSynsetsData(): Sequence<Map<String, Any>> = map { it.toData() }

/**
 * Sense to serializable dict
 *
 * @preceiver sequence of senses
 * @return sense serializable dict
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
 * @return lexes, synsets and senses as serializable dicts
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
