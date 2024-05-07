/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

object LibNanoModel {

    val domain1 = "communication"
    val domain2 = "body"
    val lexid1 = 0
    val lexid2 = 0
    val lemma1 = "jest"
    val lemma2 = "joke"
    val pos = 'v'
    val ipa1 = "dʒɛst"
    val pronunciation1 = Pronunciation(ipa1, null)
    val ipa21 = "dʒəʊk"
    val ipa22 = "dʒoʊk"
    val pronunciation21 = Pronunciation(ipa21, "GB")
    val pronunciation22 = Pronunciation(ipa22, "US")

    val senseKey11 = "jest%2:32:00::"
    val senseKey12 = "jest%2:29:00::"
    val senseKey21 = "joke%2:32:00::"
    val senseKey22 = "joke%2:29:00::"

    val lex1 = Lex(lemma1, pos.toString(), mutableListOf(senseKey11, senseKey12)).apply { pronunciations = setOf(pronunciation1) }
    val lex2 = Lex(lemma2, pos.toString(), mutableListOf(senseKey21, senseKey22)).apply { pronunciations = setOf(pronunciation21, pronunciation22) }

    val synsetId1 = "00855315-v"
    val synsetId2 = "00105308-v"

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
    val sense11 = Sense(senseKey11, lex1, pos, lexid1, synsetId1)
    val sense12 = Sense(senseKey12, lex1, pos, lexid2, synsetId2)
    val sense21 = Sense(senseKey21, lex2, pos, lexid1, synsetId1)
    val sense22 = Sense(senseKey22, lex2, pos, lexid2, synsetId2)

    val lexes = listOf(lex1, lex2)
    val senses = listOf(sense11, sense12, sense21, sense22)
    val synsets = listOf(synset1, synset2)

    val model = CoreModel(lexes, senses, synsets)
}
