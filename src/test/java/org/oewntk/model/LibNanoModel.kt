/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

object LibNanoModel {

    const val DOMAIN1 = "communication"
    const val DOMAIN2 = "body"
    const val LEMMA1 = "jest"
    const val LEMMA2 = "joke"
    const val POS = 'v'
    const val SENSEKEY11 = "jest%2:32:00::"
    const val SENSEKEY12 = "jest%2:29:00::"
    const val SENSEKEY21 = "joke%2:32:00::"
    const val SENSEKEY22 = "joke%2:29:00::"
    const val SYNSETID1 = "00855315-v"
    const val SYNSETID2 = "00105308-v"
    const val IPA1 = "dʒɛst"
    const val IPA21 = "dʒəʊk"
    const val IPA22 = "dʒoʊk"

    val pronunciation1 = Pronunciation(IPA1, null)
    val pronunciation21 = Pronunciation(IPA21, "GB")
    val pronunciation22 = Pronunciation(IPA22, "US")

    val lex1 = Lex(LEMMA1, POS.toString(), listOf(SENSEKEY11, SENSEKEY12)).apply { pronunciations = setOf(pronunciation1) }
    val lex2 = Lex(LEMMA2, POS.toString(), listOf(SENSEKEY21, SENSEKEY22)).apply { pronunciations = setOf(pronunciation21, pronunciation22) }

    val synset1 = Synset(
        SYNSETID1,
        POS,
        DOMAIN1,
        arrayOf(LEMMA1, LEMMA2),
        arrayOf("tell a joke", "speak humorously"),
        null,
        null,
    )
    val synset2 = Synset(
        SYNSETID2,
        POS,
        DOMAIN2,
        arrayOf(LEMMA1, LEMMA2),
        arrayOf("act in a funny teasing way")
    )
    val sense11 = Sense(SENSEKEY11, lex1, POS, 0, SYNSETID1)
    val sense12 = Sense(SENSEKEY12, lex1, POS, 1, SYNSETID2)
    val sense21 = Sense(SENSEKEY21, lex2, POS, 0, SYNSETID1)
    val sense22 = Sense(SENSEKEY22, lex2, POS, 1, SYNSETID2)

    // relations

    const val SYNSETID_H1 = "00742582-v"
    val synsetH1 = Synset(
        SYNSETID_H1,
        POS,
        DOMAIN1,
        arrayOf("communicate", "intercommunicate"),
        arrayOf("transmit thoughts or feelings")
    )

    const val SYNSETID_D1 = "10240982-n"
    val synsetD1 = Synset(
        SYNSETID_D1,
        'n',
        "person",
        arrayOf("jester", "fool", "motley fool"),
        arrayOf("a professional clown employed to entertain a king or nobleman in the Middle Ages")
    )

    const val SENSEKEY_D11 = "jester%1:18:00::"
    val lexD1 = Lex("jester", "n", listOf(SENSEKEY_D11))
    val senseD11 = Sense(
        SENSEKEY_D11, lexD1, 'n', 0, "10240982-n"
    )

    init {
        synset1.relations = mapOf(
            "hypernym" to setOf(synsetH1.synsetId),
            "also" to setOf(synset2.synsetId, synsetD1.synsetId),
        )
        sense11.relations = mapOf(
            "derivation" to setOf(senseD11.senseKey),
            "also" to setOf(sense12.senseKey),
        )
    }

    val lexes = listOf(lex1, lex2, lexD1)
    val senses = listOf(sense11, sense12, sense21, sense22, senseD11)
    val synsets = listOf(synset1, synset2, synsetH1, synsetD1)

    val model = CoreModel(lexes, senses, synsets)
}
