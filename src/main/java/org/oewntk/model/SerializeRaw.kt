package org.oewntk.model

import org.oewntk.model.Lex.Groups.lexByLemmaThenByKey2
import org.oewntk.model.MapFactory.sensesById
import org.oewntk.model.MapFactory.synsetsById

// L E X

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
    val ySenses: Map<SenseKey, Any> = whichSynsets.synsetsAsDataSerialize()
    return Triple(yLexes, ySynsets, ySenses)
}
