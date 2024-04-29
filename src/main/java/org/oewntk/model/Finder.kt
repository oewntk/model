/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.Utils.toSet

/**
 * Lex finder
 */
object Finder {

    /**
     * Find lex matching word
     *
     * @param model model
     * @param lemma lemma (CS)
     * @return sequence of lexes
     */
    fun getLexes(model: CoreModel, lemma: String?): Collection<Lex> {
        return model.lexesByLemma!![lemma!!]!!
    }

    /**
     * Find lex matching word and type filter
     *
     * @param model            model
     * @param word             word
     * @param posTypeFilter    posType filter
     * @param wordExtractor    word extractor
     * @param posTypeExtractor posType extractor
     * @return sequence of lexes
     */
    fun getLexesHaving(
        model: CoreModel,
        word: String,
        posTypeFilter: Char,
        wordExtractor: (Lex) -> String,
        posTypeExtractor: (Lex) -> Char,
    ): Sequence<Lex> {
        return model.lexes.asSequence()
            .filter { word == wordExtractor.invoke(it) }
            .filter { posTypeFilter == posTypeExtractor.invoke(it) }
    }

    /**
     * Find lex matching word and type filter
     *
     * @param model      model
     * @param lemma      lemma (CS)
     * @param typeFilter type filter
     * @return sequence of lexes
     */
    fun getLexesHavingType(model: CoreModel, lemma: String, typeFilter: Char): Sequence<Lex>? {
        return model.lexesByLemma!![lemma]?.asSequence()
            ?.filter { it.type == typeFilter }
    }

    /**
     * Find lex matching word and part-of-speech filter
     *
     * @param model     model
     * @param lemma     lemma (cased)
     * @param posFilter part-of-speech filter
     * @return sequence of lexes
     */
    fun getLexesHavingPos(model: CoreModel, lemma: String, posFilter: Char): Sequence<Lex>? {
        return model.lexesByLemma!![lemma]
            ?.asSequence()
            ?.filter { it.partOfSpeech == posFilter }
    }

    /**
     * Find lexes matching lemma ignoring case and having the desired type
     *
     * @param model      model
     * @param lcLemma    lower-cased lemma
     * @param typeFilter type filter
     * @return sequence of lexes
     */
    fun getLcLexesHavingType(model: CoreModel, lcLemma: String, typeFilter: Char): Sequence<Lex> {
        return model.lexesByLCLemma!![lcLemma.lowercase()]!!
            .asSequence()
            .filter { it.type == typeFilter }
    }

    /**
     * Find lexes matching lemma ignoring case and having the desired pos
     *
     * @param model     model
     * @param lcLemma   lower-cased lemma
     * @param posFilter pos filter
     * @return sequence of lexes
     */
    fun getLcLexesHavingPos(model: CoreModel, lcLemma: String, posFilter: Char): Sequence<Lex> {
        return model.lexesByLCLemma!![lcLemma.lowercase()]!!
            .asSequence()
            .filter { it.partOfSpeech == posFilter }
    }

    /**
     * Find lexes matching lemma ignoring case
     *
     * @param model   model
     * @param lcLemma lower-cased lemma
     * @return sequence of lexes
     */
    fun getLcLexes(model: CoreModel, lcLemma: String): Sequence<Lex> {
        return model.lexesByLCLemma!![lcLemma.lowercase()]!!
            .asSequence()
    }

    /**
     * Find lexes matching pronunciations
     *
     * @param lexes          lexes
     * @param pronunciations pronunciations
     * @return sequence of lexes
     * @throws IllegalArgumentException if not found
     */
    fun getLexesHavingPronunciations(lexes: Sequence<Lex>, pronunciations: Array<Pronunciation>?): Sequence<Lex> {
        val set = toSet(pronunciations)
        return lexes
            .filter { compareAsSets(set, it.pronunciations) }
    }

    /**
     * Find lex matching discriminant
     *
     * @param lexes        lexes
     * @param discriminant discriminant
     * @return sequence of lexes
     * @throws IllegalArgumentException if not found
     */
    fun getLexesHavingDiscriminant(lexes: Sequence<Lex>, discriminant: String?): Sequence<Lex> {
        return lexes
            .filter { it.discriminant == discriminant }
    }

    // Set comparison

    /**
     * Compare two arrays as sets
     *
     * @param array1 array 1
     * @param array2 array 2
     * @param T type of element
     * @return true if equals
     */
    fun <T> compareAsSets(array1: Array<T>?, array2: Array<T>?): Boolean {
        val set1 = toSet(array1)
        return compareAsSets(set1, array2)
    }

    /**
     * Compare set to array as set
     *
     * @param set1   set
     * @param array2 array
     * @param T type of element
     * @return true if equals
     */
    private fun <T> compareAsSets(set1: Set<T>, array2: Array<T>?): Boolean {
        val set2 = toSet(array2)
        return set1 == set2
    }
}
