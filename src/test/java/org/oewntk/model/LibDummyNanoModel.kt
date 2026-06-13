/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

object LibDummyNanoModel {

    const val DOMAIN1 = "communication"
    const val DOMAIN2 = "body"
    const val LEMMA1 = "jest"
    const val LEMMA2 = "joke"
    val STYPE = SynsetType.V
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

    val lex1 = Lex(LEMMA1, STYPE.value.toString(), listOf(SENSEKEY11, SENSEKEY12)).apply { pronunciations = setOf(pronunciation1) }
    val lex2 = Lex(LEMMA2, STYPE.value.toString(), listOf(SENSEKEY21, SENSEKEY22)).apply { pronunciations = setOf(pronunciation21, pronunciation22) }

    val synset1 = Synset(
        SYNSETID1,
        STYPE,
        DOMAIN1,
        setOf(LEMMA1, LEMMA2),
        listOf("tell a joke", "speak humorously"),
        null,
        null,
    )
    val synset2 = Synset(
        SYNSETID2,
        STYPE,
        DOMAIN2,
        setOf(LEMMA1, LEMMA2),
        listOf("act in a funny teasing way")
    )
    val sense11 = Sense(SENSEKEY11, lex1.key, SYNSETID1, indexInLex = 0)
    val sense12 = Sense(SENSEKEY12, lex1.key, SYNSETID2, indexInLex = 1)
    val sense21 = Sense(SENSEKEY21, lex2.key, SYNSETID1, indexInLex = 0)
    val sense22 = Sense(SENSEKEY22, lex2.key, SYNSETID2, indexInLex = 1)

    // relations

    const val SYNSETID_H1 = "00742582-v"
    val synsetH1 = Synset(
        SYNSETID_H1,
        STYPE,
        DOMAIN1,
        setOf("communicate", "intercommunicate"),
        listOf("transmit thoughts or feelings")
    )

    const val SYNSETID_D1 = "10240982-n"
    val synsetD1 = Synset(
        SYNSETID_D1,
        SynsetType.N,
        "person",
        setOf("jester", "fool", "motley fool"),
        listOf("a professional clown employed to entertain a king or nobleman in the Middle Ages")
    )

    const val SENSEKEY_D11 = "jester%1:18:00::"
    val lexD1 = Lex("jester", "n", listOf(SENSEKEY_D11))
    val senseD11 = Sense(SENSEKEY_D11, lexD1.key, "10240982-n", indexInLex = 0)

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
