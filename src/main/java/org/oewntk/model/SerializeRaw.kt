package org.oewntk.model

import org.oewntk.model.Lex.Groups.lexByLemmaThenByKey2
import org.oewntk.model.MapFactory.synsetsById

// L E X

/**
 * Lexes to serializable hypermap
 *
 * @preceiver sequence of lexes
 * @return lex hypermap
 */
fun Sequence<Lex>.lexAsDataSerialize(): SData {
    return lexByLemmaThenByKey2()
}

/**
 * Synsets to serializable map
 *
 * @preceiver sequence of synsets
 * @return synset map
 */
fun Sequence<Synset>.dataSerialize(): SData {
    return synsetsById()
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
): Pair<SData, SData> {
    val yLexes: Map<Lemma, Any> = whichLexes.lexAsDataSerialize()
    val ySynsets: Map<SynsetId, Any> = whichSynsets.dataSerialize()
    return yLexes to ySynsets
}
