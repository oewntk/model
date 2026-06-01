package org.oewntk.model

import org.oewntk.model.Lex.Groups.lexByLemmaThenByKey2
import org.oewntk.model.MapFactory.sensesById
import org.oewntk.model.MapFactory.synsetsById

// O B J E C T

fun Lex.lexAsDataSerialize(): Any {
    return mutableMapOf(
        "lemma" to lemma,
        "type" to type.value,
        "discriminant" to discriminant,
        "sense" to senseKeys,
    ).apply {
        if (pronunciations != null)
            this["pronunciation"] = pronunciations
    }
}

fun Synset.synsetAsDataSerialize(): Any {
    return mutableMapOf<String, Any>(
        "id" to synsetId,
        "type" to type.value,
        "domain" to domain,
        "member" to members.toList(),
        "definition" to (definitions.toList()),
    ).apply {
        if (examples != null) this["examples"] = examples.map { if (it.second == null) it.first else mapOf("text" to it.first, "source" to it.second) }.toList()
        if (usages != null) this["usages"] = usages
        if (relations != null) {
            relations!!.forEach { (rel, targets) ->
                this[rel] = targets.toList()
            }
        }
        if (ili != null) this["ili"] = ili
        if (wikidata != null) this["wikidata"] = wikidata.joinToString(separator = ";")
    }
}


fun Sense.senseAsDataSerialize(): Any {
    return mutableMapOf<String, Any>(
        "id" to senseId,
        "synset" to synsetId,
        "type" to type.value,
        "index" to lexIndex,
    ).apply {
        if (examples != null) this["examples"] = examples.map { if (it.second == null) it.first else mapOf("text" to it.first, "source" to it.second) }.toList()
        if (verbFrames != null) this["verbFrames"] = verbFrames.joinToString(separator = ";")
        if (adjPosition != null) this["adjPosition"] = adjPosition
        if (relations != null) {
            relations!!.forEach { (rel, targets) ->
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
 * @return lex hypermap
 */
fun Sequence<Lex>.lexesAsDataSerialize(): SData {
    return lexByLemmaThenByKey2()
}

/**
 * Synsets to serializable map
 *
 * @preceiver sequence of synsets
 * @return synset map
 */
fun Sequence<Synset>.synsetsAsDataSerialize(): SData {
    return synsetsById()
}

/**
 * Sense to serializable map
 *
 * @preceiver sequence of synsets
 * @return synset map
 */
fun Sequence<Sense>.sensesAsDataSerialize(): SData {
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
 * @return lexes and synsets
 */
fun CoreModel.dataSerialize(
    whichLexes: Sequence<Lex> = lexes.asSequence().sortedWith(compareBy(Lex::lemma).thenBy(Lex::key2)),
    whichSynsets: Sequence<Synset> = synsets.asSequence().sortedBy { it.synsetId },
    whichSenses: Sequence<Sense> = senses.asSequence().sortedBy { it.senseKey },
): Triple<SData, SData, SData> {
    val yLexes: Map<Lemma, Any> = whichLexes.lexesAsDataSerialize()
    val ySynsets: Map<SynsetId, Any> = whichSynsets.synsetsAsDataSerialize()
    val ySenses: Map<SenseKey, Any> = whichSenses.sensesAsDataSerialize()
    return Triple(yLexes, ySynsets, ySenses)
}
