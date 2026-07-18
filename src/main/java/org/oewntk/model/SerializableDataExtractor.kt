package org.oewntk.model

// K E Y S

const private val KEY_ADJPOSITION = "adjPosition"
const private val KEY_DEFINITION = "definition"
const private val KEY_DISCRIMINANT = "discriminant"
const private val KEY_DOMAIN = "domain"
const private val KEY_EXAMPLE = "example"
const private val KEY_FORM = "form"
const private val KEY_ID = "id"
const private val KEY_ILI = "ili"
const private val KEY_INDEX = "index"
const private val KEY_LEMMA = "lemma"
const private val KEY_LEXFILE = "lexfile"
const private val KEY_MEMBERS = "member"
const private val KEY_PRONUNCIATION = "pronunciation"
const private val KEY_RELATION = "relation"
const private val KEY_SENSE = "sense"
const private val KEY_SENSE_EXAMPLE = "example"
const private val KEY_SOURCE = "source"
const private val KEY_SYNSET = "synset"
const private val KEY_TAGCOUNT = "tagcount"
const private val KEY_TEXT = "text"
const private val KEY_TYPE = "type"
const private val KEY_USAGE = "usage"
const private val KEY_VALUE = "value"
const private val KEY_VARIETY = "variety"
const private val KEY_VERBFRAME = "verbFrame"
const private val KEY_VERBTEMPLATE = "verbTemplate"
const private val KEY_WIKIDATA = "wikidata"

// O B J E C T S

/**
 * SynsetType from dict
 *
 * @return type
 */
fun typeFromData(dict: Map<String, Any>): SynsetType {
    return dict[KEY_TYPE].let {
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
fun examplesFromData(list: List<Any>): List<Example> {
    val data = safeCast<List<Any>>(list)
    return data.map { example ->
        when (example) {
            is String -> Example(example, null)
            is Map<*, *> -> Example(example[KEY_TEXT] as String, example[KEY_SOURCE] as String?)
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
fun pronunciationFromData(dict: Map<String, Any>): Pronunciation {
    val value = dict[KEY_VALUE] as PronunciationValue
    val variety = dict[KEY_VARIETY] as PronunciationVariety?
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
        KEY_LEMMA to lemma,
        KEY_TYPE to type.value,
    )
        .apply {
            discriminant?.let { this[KEY_DISCRIMINANT] = it }
        }
}

/**
 * LexId from dict
 *
 * @param dict dictionary
 * @return lex id
 */
fun lexIdFromData(dict: Map<String, Any>): LexId {
    val lemma = dict[KEY_LEMMA] as Lemma
    val type: SynsetType = typeFromData(dict)
    val discriminant = dict[KEY_DISCRIMINANT] as Discriminant?
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
        KEY_LEMMA to lemma,
        KEY_TYPE to type.value,
        KEY_SENSE to senseKeys,
    ).apply {
        discriminant?.let { this[KEY_DISCRIMINANT] = it }
        forms?.let { this[KEY_FORM] = it.toList() }
        pronunciations?.let { this[KEY_PRONUNCIATION] = it.map { pronunciation -> pronunciation.toData() } }
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
    val senseKeys = safeCast<List<SenseKey>>(dict[KEY_SENSE]!!)
    return Lex(lexId.lemma, lexId.type, lexId.discriminant, senseKeys).apply {
        dict[KEY_FORM]?.let { forms = safeCast<List<String>>(it).toSet() }
        dict[KEY_PRONUNCIATION]?.let { pronunciations = safeCast<List<Map<String, Any>>>(it).map { p -> pronunciationFromData(p) }.toSet() }
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
        KEY_ID to synsetId,
        KEY_TYPE to type.value,
        KEY_DOMAIN to domain,
        KEY_MEMBERS to members.toList(),
        KEY_DEFINITION to (definitions.toList()),
    ).apply {
        examples?.let { this[KEY_EXAMPLE] = it.map { example -> if (example.source == null) example.text else mapOf(KEY_TEXT to example.text, KEY_SOURCE to example.source) }.toList() }
        usages?.let { this[KEY_USAGE] = it }
        relations?.let { this[KEY_RELATION] = it }
        ili?.let { this[KEY_ILI] = it }
        wikidata?.let { this[KEY_WIKIDATA] = it.joinToString(separator = ";") }
        source?.let { this[KEY_SOURCE] = it }
        if (includeLexFile) this[KEY_LEXFILE] = lexfile
    }
}

/**
 * Synset from dict
 *
 * @param dict dictionary
 * @return synset
 */
fun synsetFromData(dict: Map<String, Any>, includeLexFile: Boolean = false): Synset {
    val synsetId = dict[KEY_ID] as SynsetId
    val type = typeFromData(dict)
    val domain = dict[KEY_DOMAIN] as Domain
    val members = safeCast<List<Lemma>>(dict[KEY_MEMBERS]!!)
    val definitions = safeCast<List<String>>(dict[KEY_DEFINITION]!!)
    val examples = dict[KEY_EXAMPLE]?.let { examplesFromData(safeCast(it)) }
    val usages = dict[KEY_USAGE]?.let { safeCast<List<String>>(it) }
    val relations = dict[KEY_RELATION]?.let { safeCast<Map<Relation, List<SenseKey>>>(it).mapValues { e -> e.value.toSet() } }
    val ili = dict[KEY_ILI] as String?
    val wikidata = dict[KEY_WIKIDATA]?.let { safeCast<String>(it).split(";") }
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
 * Sense to serializable dict
 *
 * @preceiver sense
 * @return serializable dict
 */
fun Sense.toData(): Map<String, Any> {
    return mutableMapOf<String, Any>(
        KEY_ID to senseId,
        KEY_INDEX to indexInLex,
        KEY_SYNSET to synsetId,
    )
        .apply {
            putAll(lexId.toData())
            examples?.let { this[KEY_SENSE_EXAMPLE] = it.map { example -> if (example.source == null) example.text else mapOf(KEY_TEXT to example.text, KEY_SOURCE to example.source) }.toList() }
            verbFrames?.let { this[KEY_VERBFRAME] = it.joinToString(separator = ";") }
            verbTemplates?.let { this[KEY_VERBTEMPLATE] = it.joinToString(separator = ";") }
            adjPosition?.let { this[KEY_ADJPOSITION] = it }
            tagCount?.let { this[KEY_TAGCOUNT] = it }
            relations?.let { this[KEY_RELATION] = it }
        }
}

/**
 * Sense from dict
 *
 * @param dict dictionary
 * @return sense
 */
fun senseFromData(dict: Map<String, Any>): Sense {
    val id = dict[KEY_ID] as SenseKey
    val synsetId = dict[KEY_SYNSET] as SynsetId
    val lexId = lexIdFromData(dict)
    val index = dict[KEY_INDEX] as Int
    val examples = dict[KEY_SENSE_EXAMPLE]?.let { examplesFromData(safeCast(it)) }
    val verbFrames = dict[KEY_VERBFRAME]?.let { safeCast<VerbFrameId>(it).split(";") }?.toSet()
    val verbTemplates = dict[KEY_VERBTEMPLATE]?.let { safeCast<String>(it).split(";").map(String::toInt) }?.toSet()
    val adjPosition = dict[KEY_ADJPOSITION] as String?
    val tagCount = dict[KEY_TAGCOUNT] as Int?
    val relations = dict[KEY_RELATION]?.let { safeCast<Map<Relation, List<SenseKey>>>(it).mapValues { e -> e.value.toSet() } }
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
