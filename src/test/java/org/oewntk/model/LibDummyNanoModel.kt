/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

object LibDummyNanoModel {

    const val DOMAIN1 = "communication"
    const val DOMAIN2 = "body"
    const val DOMAIN3 = "person"

    const val LEMMA1 = "jest"
    const val LEMMA2 = "joke"
    const val LEMMA3 = "jester"

    const val SENSEKEY11 = "jest%2:32:00::"
    const val SENSEKEY12 = "jest%2:29:00::"
    const val SENSEKEY21 = "joke%2:32:00::"
    const val SENSEKEY22 = "joke%2:29:00::"
    const val SENSEKEY_D11 = "jester%1:18:00::"

    const val SYNSETID1 = "00855315-v"
    const val SYNSETID2 = "00105308-v"
    const val SYNSETID_H1 = "00742582-v"
    const val SYNSETID_D1 = "10240982-n"

    const val IPA1 = "dʒɛst"
    const val IPA21 = "dʒəʊk"
    const val IPA22 = "dʒoʊk"
    const val IPA3 = "dʒʌst"

    val DEFINITION1 = listOf("tell a joke", "speak humorously")
    val DEFINITION2 = listOf("act in a funny teasing way")

    val pronunciation1 = Pronunciation(IPA1, null)
    val pronunciation21 = Pronunciation(IPA21, "GB")
    val pronunciation22 = Pronunciation(IPA22, "US")
    val pronunciation3 = Pronunciation(IPA3, null)

    val synsetRelations1 = mapOf(
        "hypernym" to setOf(SYNSETID_H1),
        "also" to setOf(SYNSETID2, SYNSETID_D1),
    )
    val sense11Relations = mapOf(
        "derivation" to setOf(SENSEKEY_D11),
        "also" to setOf(SENSEKEY12),
    )

    val lex1 = Lex(LEMMA1, "v", listOf(SENSEKEY11, SENSEKEY12)).apply { pronunciations = setOf(pronunciation1) }
    val lex2 = Lex(LEMMA2, "v", listOf(SENSEKEY21, SENSEKEY22)).apply { pronunciations = setOf(pronunciation21, pronunciation22) }
    val lexD1 = Lex(LEMMA3, "n", listOf(SENSEKEY_D11))

    val synset1 = Synset(SYNSETID1, SynsetType.V, DOMAIN1, setOf(LEMMA1, LEMMA2), DEFINITION1, null, null, relations = synsetRelations1)
    val synset2 = Synset(SYNSETID2, SynsetType.V, DOMAIN2, setOf(LEMMA1, LEMMA2), DEFINITION2)
    val synsetH1 = Synset(SYNSETID_H1, SynsetType.V, DOMAIN1, setOf("communicate", "intercommunicate"), listOf("transmit thoughts or feelings"))
    val synsetD1 = Synset(SYNSETID_D1, SynsetType.N, DOMAIN3, setOf("jester", "fool", "motley fool"), listOf("a professional clown employed to entertain a king or nobleman in the Middle Ages"))

    val sense11 = Sense(SENSEKEY11, lex1.key, SYNSETID1, indexInLex = 0, relations = sense11Relations)
    val sense12 = Sense(SENSEKEY12, lex1.key, SYNSETID2, indexInLex = 1)
    val sense21 = Sense(SENSEKEY21, lex2.key, SYNSETID1, indexInLex = 0)
    val sense22 = Sense(SENSEKEY22, lex2.key, SYNSETID2, indexInLex = 1)
    val senseD11 = Sense(SENSEKEY_D11, lexD1.key, SYNSETID_D1, indexInLex = 0)

    val lexes = listOf(lex1, lex2, lexD1)
    val senses = listOf(sense11, sense12, sense21, sense22, senseD11)
    val synsets = listOf(synset1, synset2, synsetH1, synsetD1)

    val model = CoreModel(lexes, senses, synsets)
}
