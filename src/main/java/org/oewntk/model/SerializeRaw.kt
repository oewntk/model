package org.oewntk.model

import org.oewntk.model.Lex.Groups.lexByLemmaThenByKey2
import org.oewntk.model.MapFactory.synsetsById

// L E X

/**
 * Lexes to hypermap
 *
 * @preceiver sequence of lexes
 * @return lex hypermap
 */
fun Sequence<Lex>.asLexData(): SData {
    return lexByLemmaThenByKey2()
}

/**
 * Synsets to serialized hypermap
 *
 * @preceiver sequence of lexes
 * @return lex hypermap
 */
fun Sequence<Synset>.asSynsetData(): SData {
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
fun CoreModel.asData(
    whichLexes: Sequence<Lex> = lexes.asSequence().sortedWith(compareBy(Lex::lemma).thenBy(Lex::key2)),
    whichSynsets: Sequence<Synset> = synsets.asSequence().sortedBy { it.synsetId },
): Pair<SData, SData> {
    val yLexes: Map<Lemma, Any> = whichLexes.asLexData()
    val ySynsets: Map<SynsetId, Any> = whichSynsets.asSynsetData()
    return yLexes to ySynsets
}
