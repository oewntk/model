package org.oewntk.model

// O B J E C T

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
        "lemma" to first,
        "type" to second.value,
    )
        .apply {
            third?.let { this["discriminant"] = it }
        }
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
        relations?.let { relation ->
            relation.forEach { (rel, targets) ->
                this[rel] = targets.toList()
            }
        }
        ili?.let { this["ili"] = it }
        wikidata?.let { this["wikidata"] = it.joinToString(separator = ";") }
    }
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
        "synset" to synsetId,
        "lex" to lexId.toData(),
        "type" to type.value,
        "index" to indexInLex,
    ).apply {
        examples?.let { this["examples"] = it.map { example -> if (example.second == null) example.first else mapOf("text" to example.first, "source" to example.second) }.toList() }
        verbFrames?.let { this["verbFrames"] = it.joinToString(separator = ";") }
        adjPosition?.let { this["adjPosition"] = it }
        relations?.let {
            it.forEach { (rel, targets) ->
                this[rel] = targets.toList()
            }
        }
    }
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
