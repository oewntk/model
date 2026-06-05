package org.oewntk.model

import org.oewntk.model.Lex.Groups.lexByLemmaThenByKey2
import org.oewntk.model.MapFactory.sensesById
import org.oewntk.model.MapFactory.synsetsById

// S E Q U E N C E S   O F   O B J E C T S

/**
 * Lexes to serializable hypermap
 *
 * @preceiver sequence of lexes
 * @return lex serializable hypermap
 */
fun Sequence<Lex>.toLexesMappedData(): Map<String, Any> {
    return lexByLemmaThenByKey2()
        .mapValues { (_: Lemma, key2Map: Map<Key2, Lex>) ->
            key2Map.mapValues { it.value.toData() }
        }
}

/**
 * Synsets to serializable map
 *
 * @preceiver sequence of synsets
 * @return synset serializable map
 */
fun Sequence<Synset>.toSynsetsMappedData(): Map<String, Any> {
    return synsetsById().mapValues { it.value.toData() }
}

/**
 * Sense to serializable map
 *
 * @preceiver sequence of senses
 * @return sense serializable map
 */
fun Sequence<Sense>.toSensesMappedData(): Map<String, Any> {
    return sensesById().mapValues { it.value.toData() }
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
fun CoreModel.toMappedData(
    whichLexes: Sequence<Lex> = lexes.asSequence().sortedWith(compareBy(Lex::lemma).thenBy(Lex::key2)),
    whichSynsets: Sequence<Synset> = synsets.asSequence().sortedBy { it.synsetId },
    whichSenses: Sequence<Sense> = senses.asSequence().sortedBy { it.senseKey },
): Triple<Map<String, Any>, Map<String, Any>, Map<String, Any>> {
    val lexesData: Map<Lemma, Any> = whichLexes.toLexesMappedData()
    val synsetsData: Map<SynsetId, Any> = whichSynsets.toSynsetsMappedData()
    val sensesData: Map<SenseKey, Any> = whichSenses.toSensesMappedData()
    return Triple(lexesData, synsetsData, sensesData)
}
