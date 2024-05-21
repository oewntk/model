/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

object LibNanoModel {

    const val domain1 = "communication"
    const val domain2 = "body"
    const val lemma1 = "jest"
    const val lemma2 = "joke"
    const val pos = 'v'
    const val senseKey11 = "jest%2:32:00::"
    const val senseKey12 = "jest%2:29:00::"
    const val senseKey21 = "joke%2:32:00::"
    const val senseKey22 = "joke%2:29:00::"
    const val synsetId1 = "00855315-v"
    const val synsetId2 = "00105308-v"
    const val ipa1 = "dʒɛst"
    const val ipa21 = "dʒəʊk"
    const val ipa22 = "dʒoʊk"

    val pronunciation1 = Pronunciation(ipa1, null)
    val pronunciation21 = Pronunciation(ipa21, "GB")
    val pronunciation22 = Pronunciation(ipa22, "US")

    val lex1 = Lex(lemma1, pos.toString(), listOf(senseKey11, senseKey12)).apply { pronunciations = setOf(pronunciation1) }
    val lex2 = Lex(lemma2, pos.toString(), listOf(senseKey21, senseKey22)).apply { pronunciations = setOf(pronunciation21, pronunciation22) }

    val synset1 = Synset(
        synsetId1,
        pos,
        domain1,
        arrayOf(lemma1, lemma2),
        arrayOf("tell a joke", "speak humorously")
    )
    val synset2 = Synset(
        synsetId2,
        pos,
        domain2,
        arrayOf(lemma1, lemma2),
        arrayOf("act in a funny teasing way")
    )
    val sense11 = Sense(senseKey11, lex1, pos, 0, synsetId1)
    val sense12 = Sense(senseKey12, lex1, pos, 1, synsetId2)
    val sense21 = Sense(senseKey21, lex2, pos, 0, synsetId1)
    val sense22 = Sense(senseKey22, lex2, pos, 1, synsetId2)

    // relations

    const val synsetIdH1 = "00742582-v"
    val synsetH1 = Synset(
        synsetIdH1,
        pos,
        domain1,
        arrayOf("communicate", "intercommunicate"),
        arrayOf("transmit thoughts or feelings")
    )

    const val synsetIdD1 = "10240982-n"
    val synsetD1 = Synset(
        synsetIdD1,
        'n',
        "person",
        arrayOf("jester", "fool", "motley fool"),
        arrayOf("a professional clown employed to entertain a king or nobleman in the Middle Ages")
    )

    val senseKeyD11 = "jester%1:18:00::"
    val lexD1 = Lex("jester", "n", listOf(senseKeyD11))
    val senseD11 = Sense(
        senseKeyD11, lexD1, 'n', 0, "10240982-n"
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
