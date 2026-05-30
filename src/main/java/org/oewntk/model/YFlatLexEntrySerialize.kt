package org.oewntk.model

// L E X  E N T R I E S

/**
 * Entries to serializable map

 * @receiver sequence of entries
 * @param resolver senseKey to sense resolver
 * @return map of entries by lemma
 */
fun Sequence<LexEntry>.toSerializable(resolver: (SenseKey) -> Sense?): Map<Key2, Any> {
    return mutableMapOf<Key2, Any>().apply {
        this@toSerializable.forEach { (lemma, lexes) ->
            this[lemma] = lexes.associate { it.key2 to it.toSerializable(resolver) }
        }
    }
}

// M O D E L


/**
 * Flat data producer
 *
 * @param whichEntries which entries to select, by default all
 * @param whichSynsets which synsets, by default all
 *
 * @receiver core model
 * @return lex entries and synsets
 * @return lexes and synsets
 */
fun CoreModel.toFlatSerializableOfLexEntries(
    whichEntries: Sequence<LexEntry> = lexEntries.sortedBy { it.key },
    whichSynsets: Sequence<Synset> = synsets.asSequence().sortedBy { it.synsetId },
): Pair<SData, SData> {
    val yEntries: Map<Key2, Any> = whichEntries.toSerializable(senseResolver)
    val ySynsets: Map<SynsetId, Any> = whichSynsets.toSerializable()
    return yEntries to ySynsets
}
