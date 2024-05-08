/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

/**
 * Lex finder
 */
object Finder {

    /**
     * Find lex matching word
     *
     * @param model model
     * @param lemma lemma (CS)
     * @return collection of lexes
     */
    fun getLexes(model: CoreModel, lemma: Lemma): Collection<Lex> {
        return model.lexesByLemma!![lemma]!!
    }

    /**
     * Find lex matching word and type filter
     *
     * @param model             model
     * @param word              word
     * @param targetCategory    target category
     * @param wordExtractor     word extractor
     * @param categoryExtractor category extractor
     * @return sequence of lexes
     */
    fun getLexesHaving(
        model: CoreModel,
        word: Lemma,
        targetCategory: Category,
        wordExtractor: (Lex) -> Lemma,
        categoryExtractor: (Lex) -> Category,
    ): Sequence<Lex> {
        return model.lexes
            .asSequence()
            .filter { word == wordExtractor.invoke(it) }
            .filter { targetCategory == categoryExtractor.invoke(it) }
    }

    /**
     * Find lex matching word and type filter
     *
     * @param model      model
     * @param lemma      lemma (CS)
     * @param typeFilter type filter
     * @return sequence of lexes
     */
    fun getLexesHavingType(model: CoreModel, lemma: Lemma, typeFilter: Char): Sequence<Lex>? {
        return model.lexesByLemma!![lemma]
            ?.asSequence()
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
    fun getLexesHavingPos(model: CoreModel, lemma: Lemma, posFilter: Char): Sequence<Lex>? {
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
    fun getLcLexesHavingType(model: CoreModel, lcLemma: Lemma, typeFilter: Char): Sequence<Lex>? {
        return model.lexesByLCLemma!![lcLemma.lowercase()]
            ?.asSequence()
            ?.filter { it.type == typeFilter }
    }

    /**
     * Find lexes matching lemma ignoring case and having the desired category
     *
     * @param model          model
     * @param lcLemma        lower-cased lemma
     * @param targetCategory target category
     * @return sequence of lexes
     */
    fun getLcLexesHavingPos(model: CoreModel, lcLemma: Lemma, targetCategory: Category): Sequence<Lex>? {
        return model.lexesByLCLemma!![lcLemma.lowercase()]
            ?.asSequence()
            ?.filter { it.partOfSpeech == targetCategory }
    }

    /**
     * Find lexes matching lemma ignoring case
     *
     * @param model   model
     * @param lcLemma lower-cased lemma
     * @return sequence of lexes
     */
    fun getLcLexes(model: CoreModel, lcLemma: Lemma): Sequence<Lex>? {
        return model.lexesByLCLemma!![lcLemma.lowercase()]
            ?.asSequence()
    }

    /**
     * Find lexes matching pronunciations
     *
     * @param lexes          lexes
     * @param pronunciations pronunciations
     * @return sequence of lexes
     * @throws IllegalArgumentException if not found
     */
    fun getLexesHavingPronunciations(lexes: Sequence<Lex>, pronunciations: Set<Pronunciation>?): Sequence<Lex> {
        val p = pronunciations ?: emptySet()
        return lexes
            .filter { p == it.pronunciations }
    }

    /**
     * Find lex matching discriminant
     *
     * @param lexes        lexes
     * @param discriminant discriminant
     * @return sequence of lexes
     * @throws IllegalArgumentException if not found
     */
    fun getLexesHavingDiscriminant(lexes: Sequence<Lex>, discriminant: Discriminant?): Sequence<Lex> {
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
        val set1 = array1?.toSet() ?: emptySet()
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
        val set2 = array2?.toSet() ?: emptySet()
        return set1 == set2
    }
}
