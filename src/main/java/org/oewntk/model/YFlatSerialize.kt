package org.oewntk.model

import org.oewntk.model.Lex.Groups.groupByLemmaThenByKey2

// L E X

/**
 * Lexes to serialized hypermap
 *
 * @preceiver sequence of lexes
 * @return lex hypermap
 */
fun Sequence<Lex>.asEntriesToSerializable(): Map<Lemma, Map<Key2, Collection<Lex>>> {
    return this.groupByLemmaThenByKey2()
}

/**
 * Lexes to serializable list of lex values

 * @receiver sequence of lexes
 * @param resolver senseKey to sense resolver
 * @return list of lex values
 */
fun Sequence<Lex>.asValuesToSerializable(resolver: (SenseKey) -> Sense?): List<Any> {
    fun Lex.toSerializable(): Map<String, Any> = this@toSerializable.toSerializable { resolver.invoke(it)!! }
    return this@asValuesToSerializable
        .map(Lex::toSerializable)
        .toList()
}

// M O D E L

/**
 * Flat data producer
 *
 * @param whichLexes which entries to select, by default all
 * @param whichSynsets which synsets, by default all
 *
 * @receiver core model
 * @return lexes and synsets
 */
fun CoreModel.toFlatSerializableOfLexes(
    whichLexes: Sequence<Lex> = lexes.asSequence().sortedWith(compareBy(Lex::lemma).thenBy(Lex::key2)),
    whichSynsets: Sequence<Synset> = synsets.asSequence().sortedBy { it.synsetId },
): Pair<SData, SData> {
    val yLexes: Map<Lemma, Any> = whichLexes.asEntriesToSerializable()
    val ySynsets: Map<SynsetId, Any> = whichSynsets.toSerializable()
    return yLexes to ySynsets
}
