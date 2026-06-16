/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.LibDummyNanoModel.DEFINITION1
import org.oewntk.model.LibDummyNanoModel.DEFINITION2
import org.oewntk.model.LibDummyNanoModel.DOMAIN1
import org.oewntk.model.LibDummyNanoModel.DOMAIN2
import org.oewntk.model.LibDummyNanoModel.LEMMA1
import org.oewntk.model.LibDummyNanoModel.LEMMA2
import org.oewntk.model.LibDummyNanoModel.LEMMA3
import org.oewntk.model.LibDummyNanoModel.SENSEKEY11
import org.oewntk.model.LibDummyNanoModel.SENSEKEY12
import org.oewntk.model.LibDummyNanoModel.SYNSETID1
import org.oewntk.model.LibDummyNanoModel.SYNSETID2
import org.oewntk.model.LibDummyNanoModel.lex1
import org.oewntk.model.LibDummyNanoModel.lex2
import org.oewntk.model.LibDummyNanoModel.pronunciation1
import org.oewntk.model.LibDummyNanoModel.pronunciation21

object LibDummyNanoModelGenerator {

    // @formatter:off
    fun genSynsetEqual(): Pair<Synset, Synset> =
        Synset(SYNSETID1, SynsetType.V, DOMAIN1, setOf(LEMMA1, LEMMA2), DEFINITION1, null, null) to
        Synset(SYNSETID1, SynsetType.V, DOMAIN1, setOf(LEMMA1, LEMMA2), DEFINITION1, null, null)

    fun genSynsetDiffWithId(): Pair<Synset, Synset> =
        Synset(SYNSETID1, SynsetType.V, DOMAIN1, setOf(LEMMA1, LEMMA2), DEFINITION1, null, null) to
        Synset(SYNSETID2, SynsetType.V, DOMAIN1, setOf(LEMMA1, LEMMA2), DEFINITION1, null, null)

    fun genSynsetDiffWithType(): Pair<Synset, Synset> =
        Synset(SYNSETID1, SynsetType.V, DOMAIN1, setOf(LEMMA1, LEMMA2), DEFINITION1, null, null) to
        Synset(SYNSETID1, SynsetType.N, DOMAIN1, setOf(LEMMA1, LEMMA2), DEFINITION1, null, null)

    fun genSynsetDiffWithDomain(): Pair<Synset, Synset> =
        Synset(SYNSETID1, SynsetType.V, DOMAIN1, setOf(LEMMA1, LEMMA2), DEFINITION1, null, null) to
        Synset(SYNSETID1, SynsetType.V, DOMAIN2, setOf(LEMMA1, LEMMA2), DEFINITION1, null, null)

    fun genSynsetDiffWithMembers(): Pair<Synset, Synset> =
        Synset(SYNSETID1, SynsetType.V, DOMAIN1, setOf(LEMMA1, LEMMA2), DEFINITION1, null, null) to
        Synset(SYNSETID1, SynsetType.V, DOMAIN1, setOf(LEMMA1, LEMMA3), DEFINITION1, null, null)

    fun genSynsetDiffWithDefinition(): Pair<Synset, Synset> =
        Synset(SYNSETID1, SynsetType.V, DOMAIN1, setOf(LEMMA1, LEMMA2), DEFINITION1, null, null) to
        Synset(SYNSETID1, SynsetType.V, DOMAIN1, setOf(LEMMA1, LEMMA2), DEFINITION2, null, null)

    fun genSynsetDiffWithExamples(): Pair<Synset, Synset> =
        Synset(SYNSETID1, SynsetType.V, DOMAIN1, setOf(LEMMA1, LEMMA2), DEFINITION1, null, null) to
        Synset(SYNSETID1, SynsetType.V, DOMAIN1, setOf(LEMMA1, LEMMA2), DEFINITION1, listOf("He often jokes when ..." to null), null)

    // @formatter:on

    // @formatter:off

    fun genSenseEqual(): Pair<Sense, Sense> =
        Sense(SENSEKEY11, lex1.key, SYNSETID1, indexInLex = 0) to
        Sense(SENSEKEY11, lex1.key, SYNSETID1, indexInLex = 0)

    fun genSenseDiffWithId(): Pair<Sense, Sense> =
        Sense(SENSEKEY11, lex1.key, SYNSETID1, indexInLex = 0) to
        Sense(SENSEKEY12, lex1.key, SYNSETID1, indexInLex = 0)

    fun genSenseDiffWithLexId(): Pair<Sense, Sense> =
        Sense(SENSEKEY11, lex1.key, SYNSETID1, indexInLex = 0) to
        Sense(SENSEKEY11, lex2.key, SYNSETID1, indexInLex = 0)

    fun genSenseDiffWithSynsetId(): Pair<Sense, Sense> =
        Sense(SENSEKEY11, lex1.key, SYNSETID1, indexInLex = 0) to
        Sense(SENSEKEY11, lex1.key, SYNSETID2, indexInLex = 0)

    fun genSenseDiffWithIndexInLex(): Pair<Sense, Sense> =
        Sense(SENSEKEY11, lex1.key, SYNSETID1, indexInLex = 0) to
        Sense(SENSEKEY11, lex1.key, SYNSETID1, indexInLex = 1)

    fun genSenseDiffWithAdjPosition(): Pair<Sense, Sense> =
        Sense(SENSEKEY11, lex1.key, SYNSETID1, indexInLex = 0, adjPosition = "a") to
        Sense(SENSEKEY11, lex1.key, SYNSETID1, indexInLex = 0, adjPosition = "ip")

    fun genSenseDiffWithVerbFrames(): Pair<Sense, Sense> =
        Sense(SENSEKEY11, lex1.key, SYNSETID1, indexInLex = 0, verbFrames = setOf("vt","vta")) to
        Sense(SENSEKEY11, lex1.key, SYNSETID1, indexInLex = 0, verbFrames = setOf("vt","vti"))

    fun genSenseDiffWithVerbTemplates(): Pair<Sense, Sense> =
        Sense(SENSEKEY11, lex1.key, SYNSETID1, indexInLex = 0, verbTemplates = setOf(1,2,3)) to
        Sense(SENSEKEY11, lex1.key, SYNSETID1, indexInLex = 0, verbTemplates = setOf(4,5,6))

    // @formatter:off

    fun genLexEqual(): Pair<Lex, Lex> =
        Lex(LEMMA1, "v", listOf(SENSEKEY11, SENSEKEY12)).apply { pronunciations = setOf(pronunciation1) } to
        Lex(LEMMA1, "v", listOf(SENSEKEY11, SENSEKEY12)).apply { pronunciations = setOf(pronunciation1) }

    fun genLeDiffWithLemma(): Pair<Lex, Lex> =
        Lex(LEMMA1, "v", listOf(SENSEKEY11, SENSEKEY12)).apply { pronunciations = setOf(pronunciation1) } to
        Lex(LEMMA2, "v", listOf(SENSEKEY11, SENSEKEY12)).apply { pronunciations = setOf(pronunciation1) }

    fun genLeDiffWithType(): Pair<Lex, Lex> =
        Lex(LEMMA1, "v", listOf(SENSEKEY11, SENSEKEY12)).apply { pronunciations = setOf(pronunciation1) } to
        Lex(LEMMA1, "a", listOf(SENSEKEY11, SENSEKEY12)).apply { pronunciations = setOf(pronunciation1) }

    fun genLeDiffWithDiscriminant(): Pair<Lex, Lex> =
        Lex(LEMMA1, SynsetType.V, discriminant= null, listOf(SENSEKEY11, SENSEKEY12)).apply { pronunciations = setOf(pronunciation1) } to
        Lex(LEMMA1, SynsetType.V, discriminant = "-1", listOf(SENSEKEY11, SENSEKEY12)).apply { pronunciations = setOf(pronunciation1) }

    fun genLeDiffWithKey2(): Pair<Lex, Lex> =
        Lex(LEMMA1, "v", listOf(SENSEKEY11, SENSEKEY12)).apply { pronunciations = setOf(pronunciation1) } to
        Lex(LEMMA1, "v-1", listOf(SENSEKEY11, SENSEKEY12)).apply { pronunciations = setOf(pronunciation1) }

    fun genLeDiffWithSenseKeys(): Pair<Lex, Lex> =
        Lex(LEMMA1, "v", listOf(SENSEKEY11, SENSEKEY12)).apply { pronunciations = setOf(pronunciation1) } to
        Lex(LEMMA1, "v", listOf(SENSEKEY11)).apply { pronunciations = setOf(pronunciation1) }

   fun genLeDiffWithPronunciations(): Pair<Lex, Lex> =
        Lex(LEMMA1, "v", listOf(SENSEKEY11, SENSEKEY12)).apply { pronunciations = setOf(pronunciation1) } to
        Lex(LEMMA1, "v", listOf(SENSEKEY11, SENSEKEY12)).apply { pronunciations = setOf(pronunciation21) }

    // @formatter:on
}
