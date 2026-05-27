package org.oewntk.model

import java.util.Locale

object SenseKeys {

    val LEXFILE_TO_NUM = mapOf(
        "adj.all" to 0,
        "adj.pert" to 1,
        "adv.all" to 2,
        "noun.Tops" to 3,
        "noun.act" to 4,
        "noun.animal" to 5,
        "noun.artifact" to 6,
        "noun.attribute" to 7,
        "noun.body" to 8,
        "noun.cognition" to 9,
        "noun.communication" to 10,
        "noun.event" to 11,
        "noun.feeling" to 12,
        "noun.food" to 13,
        "noun.group" to 14,
        "noun.location" to 15,
        "noun.motive" to 16,
        "noun.object" to 17,
        "noun.person" to 18,
        "noun.phenomenon" to 19,
        "noun.plant" to 20,
        "noun.possession" to 21,
        "noun.process" to 22,
        "noun.quantity" to 23,
        "noun.relation" to 24,
        "noun.shape" to 25,
        "noun.state" to 26,
        "noun.substance" to 27,
        "noun.time" to 28,
        "verb.body" to 29,
        "verb.change" to 30,
        "verb.cognition" to 31,
        "verb.communication" to 32,
        "verb.competition" to 33,
        "verb.consumption" to 34,
        "verb.contact" to 35,
        "verb.creation" to 36,
        "verb.emotion" to 37,
        "verb.motion" to 38,
        "verb.perception" to 39,
        "verb.possession" to 40,
        "verb.social" to 41,
        "verb.stative" to 42,
        "verb.weather" to 43,
        "adj.ppl" to 44,
        "generated" to 99,
    )

    fun Lemma.escapeForSenseKey(): String {
        return replace(' ', '_')
    }

    fun Domain.toLexFileNum(): Int = LEXFILE_TO_NUM[this] ?: throw IllegalArgumentException(this)

    fun SynsetType.toPosNum(): Int = ordinal + 1

    fun generateSenseKey(lemma: Lemma, synset: Synset, idx: Int): SenseKey {
        lemma.lowercase(Locale.ENGLISH).escapeForSenseKey()
        val escapedLemma = lemma.lowercase(Locale.ENGLISH).escapeForSenseKey()
        val ssType = synset.type.toPosNum()
        val lexfileNum = "%02d".format(synset.lexfile.toLexFileNum())
        val lexfileIdx = "%02d".format(idx + 1)
        return "$escapedLemma%$ssType:$lexfileNum:$lexfileIdx::"
    }
}