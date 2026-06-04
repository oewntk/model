package org.oewntk.model

import org.oewntk.model.Lex.Groups.lexByLemmaThenByKey2
import org.oewntk.model.MapFactory.sensesById
import org.oewntk.model.MapFactory.synsetsById

// O B J E C T

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
        "type" to type.value,
        "index" to lexIndex,
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
fun Sequence<Lex>.toLexesData(): Map<String, Any> {
    return lexByLemmaThenByKey2()
}

/**
 * Synsets to serializable map
 *
 * @preceiver sequence of synsets
 * @return synset serializable map
 */
fun Sequence<Synset>.toSynsetsData(): Map<String, Any> {
    return synsetsById()
}

/**
 * Sense to serializable map
 *
 * @preceiver sequence of senses
 * @return sense serializable map
 */
fun Sequence<Sense>.toSensesData(): Map<String, Any> {
    return sensesById()
}

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
): Triple<Map<String, Any>, Map<String, Any>, Map<String, Any>> {
    val lexesData: Map<Lemma, Any> = whichLexes.toLexesData()
    val synsetsData: Map<SynsetId, Any> = whichSynsets.toSynsetsData()
    val sensesData: Map<SenseKey, Any> = whichSenses.toSensesData()
    return Triple(lexesData, synsetsData, sensesData)
}
