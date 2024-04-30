/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.Key.*
import org.oewntk.model.MapFactory.sensesById
import org.oewntk.model.MapFactory.synsetsById
import java.io.File
import java.io.Serializable
import java.util.*

/**
 * Base language model
 *
 * @property lexes   lexical items
 * @property senses  senses
 * @property synsets synsets
 * @property source  source
 */
open class CoreModel(
    lexes: Collection<Lex>,
    senses: Collection<Sense>,
    synsets: Collection<Synset>,
) : Serializable {

    /**
     * Lexical items
     */
    val lexes: Collection<Lex> = Collections.unmodifiableCollection(lexes)

    /**
     * Senses
     */
    val senses: Collection<Sense> = Collections.unmodifiableCollection(senses)

    /**
     * Synsets
     */
    val synsets: Collection<Synset> = Collections.unmodifiableCollection(synsets)

    /**
     * Source
     * Input directory
     */
    var source: File? = null

    /**
     * Generate inverse relations
     *
     * @return this model
     */
    fun generateInverseRelations(): CoreModel {
        InverseRelationFactory.makeSynsetRelations(synsetsById!!)
        InverseRelationFactory.makeSenseRelations(sensesById!!)
        return this
    }

    /**
     * Cached
     * Lexical units mapped by lemma written form.
     * A multimap: each value is an array of lexes for the lemma.
     */
    @Transient
    var lexesByLemma: Map<String, Collection<Lex>>? = null
        get() {
            if (field == null) {
                field = LexGroupings.lexesByLemma(lexes)
            }
            return field
        }
        private set

    /**
     * Cached
     * Lexical units mapped by lemma lower-cased written form.
     * A multimap: each value is an array of lexes for the lemma.
     */
    @Transient
    var lexesByLCLemma: Map<String, Collection<Lex>>? = null
        get() {
            if (field == null) {
                field = LexGroupings.lexesByLCLemma(lexes)
            }
            return field
        }
        private set

    /**
     * Cached
     * Senses mapped by id (sensekey)
     */
    @Transient
    var sensesById: Map<String, Sense>? = null
        get() {
            if (field == null) {
                field = sensesById(senses)
            }
            return field
        }
        private set

    /**
     * Cached
     * Synsets mapped by id (synset id)
     */
    @Transient
    var synsetsById: Map<String, Synset>? = null
        get() {
            if (field == null) {
                field = synsetsById(synsets)
            }
            return field
        }
        private set

    /**
     * Info about this model
     *
     * @return info
     */
    open fun info(): String? {
        return String.format("lexes: %d, senses: %d, synsets: %s", lexes.size, senses.size, synsets.size)
    }

    /**
     * Computed count about this model
     *
     * @return info
     */
    fun counts(): String {
        val csWordCount = lexes
            .map { it.lemma }
            .distinct()
            .count()
        val lcWordCount = lexes
            .map { it.lCLemma }
            .distinct()
            .count()
        val casedCount = lexes
            .map { it.lemma }
            .filter { it != it.lowercase() }
            .distinct()
            .count()

        val distinctByKeyOEWNLexCount = lexes
            .map { W_P_A.of_t(it) }
            .distinct()
            .count()
        val distinctByKeyShallowLexCount = lexes
            .map { W_P_D.of_t(it) }
            .distinct()
            .count()
        val distinctByKeyPOSLexCount = lexes
            .map { W_P_A.of_p(it) }
            .distinct()
            .count()
        val distinctByKeyICLexCount = lexes
            .map { W_P_A.of_lc_t(it) }
            .distinct()
            .count()
        val distinctByKeyPWNLexCount = lexes
            .map { W_P.of_lc_p(it) }
            .distinct()
            .count()
        val distinctSenseGroupsCount = lexes
            .map { it.senses.toSet() }
            .distinct()
            .count()
        val sensesInSenseGroupsSum = lexes
            .map { it.senses.toSet() }
            .distinct()
            .sumOf { it.size.toLong() }

        val withMultiSenseLexCount = lexes
            .count { it.senses.size > 1 }
        val discriminantCount = lexes
            .mapNotNull { it.discriminant }
            .distinct()
            .count()
        val withDiscriminantLexCount = lexes
            .count { it.discriminant != null }
        val withPronunciationLexCount = lexes
            .count { it.pronunciations != null }

        val withRelationSenseCount = senses
            .count { it.relations != null }
        val senseRelationSum = senses
            .mapNotNull { it.relations }
            .flatMap { it.values }
            .sumOf { it.size.toLong() }

        val withRelationSynsetCount = synsets
            .count { it.relations != null }
        val synsetRelationSum = synsets
            .mapNotNull { it.relations }
            .flatMap { it.values }
            .sumOf { it.size.toLong() }

        return String.format(COUNT_TEMPLATE, "lexes", lexes.size) +
                String.format(COUNT_TEMPLATE, "lemmas (distinct CS)", csWordCount) +
                String.format(COUNT_TEMPLATE, "lemmas (distinct LC)", lcWordCount) +
                String.format(COUNT_TEMPLATE, "lemmas (cased)", casedCount) +
                String.format(COUNT_TEMPLATE, "discriminant types", discriminantCount) +
                String.format(COUNT_TEMPLATE, "lexes with discriminant", withDiscriminantLexCount) +
                String.format(COUNT_TEMPLATE, "lexes with pronunciation", withPronunciationLexCount) +
                String.format(COUNT_TEMPLATE, "lexes with multi senses", withMultiSenseLexCount) +
                String.format(COUNT_TEMPLATE, "distinct lexes by key W_P_A_type (deep)", distinctByKeyOEWNLexCount) +
                String.format(COUNT_TEMPLATE, "distinct lexes by key W_P_D_type (shallow)", distinctByKeyShallowLexCount) +
                String.format(COUNT_TEMPLATE, "distinct lexes by key W_P_A_pos (pos)", distinctByKeyPOSLexCount) +
                String.format(COUNT_TEMPLATE, "distinct lexes by key W_P_A_lc_type (ic)", distinctByKeyICLexCount) +
                String.format(COUNT_TEMPLATE, "distinct lexes by key W_P_lc_pos (pwn)", distinctByKeyPWNLexCount) +
                String.format(COUNT_TEMPLATE, "senses", senses.size) +
                String.format(COUNT_TEMPLATE, "distinct sense sets in lexes", distinctSenseGroupsCount) +
                String.format(COUNT_TEMPLATE, "senses in sense sets", sensesInSenseGroupsSum) +
                String.format(COUNT_TEMPLATE, "senses with relations", withRelationSenseCount) +
                String.format(COUNT_TEMPLATE, "sense relations", senseRelationSum) +
                String.format(COUNT_TEMPLATE, "synsets", synsets.size) +
                String.format(COUNT_TEMPLATE, "synsets with relations", withRelationSynsetCount) +
                String.format(COUNT_TEMPLATE, "synset relations", synsetRelationSum)
    }

    /**
     * Computed count about this model
     *
     * @return info
     */
    fun xCounts(): String {
        counts()

        val pronunciationRefSum = lexes
            .filter { it.pronunciations != null }
            .sumOf { it.pronunciations!!.size.toLong() }
        val pronunciationCount = lexes
            .filter { it.pronunciations != null }
            .flatMap { it.pronunciations!!.asSequence() }
            .distinct()
            .count()
        val withMorphLexCount = lexes
            .count { it.forms != null }
        val morphRefSum = lexes
            .filter { it.forms != null }
            .sumOf { it.forms!!.size.toLong() }
        val morphCount = lexes
            .filter { it.forms != null }
            .flatMap { it.forms!!.asSequence() }
            .distinct()
            .count()

        val withExamplesSenseCount = senses
            .count { !it.examples.isNullOrEmpty() }
        val withVerbFramesSenseCount = senses
            .count { !it.verbFrames.isNullOrEmpty() }
        val withVerbTemplatesSenseCount = senses
            .count { it.verbTemplates != null }
        val withTagCountSenseCount = senses
            .count { it.tagCount != null }

        val withSamplesSynsetCount = synsets
            .count { !it.examples.isNullOrEmpty() }
        val sampleSum = synsets
            .filter { !it.examples.isNullOrEmpty() }
            .sumOf { it.examples!!.size.toLong() }

        return String.format(COUNT_TEMPLATE, "lexes with morphs", withMorphLexCount) +
                String.format(COUNT_TEMPLATE, "senses with verb frames", withVerbFramesSenseCount) +
                String.format(COUNT_TEMPLATE, "senses with verb templates", withVerbTemplatesSenseCount) +
                String.format(COUNT_TEMPLATE, "senses with tag count", withTagCountSenseCount) +
                String.format(COUNT_TEMPLATE, "senses with examples", withExamplesSenseCount) +
                String.format(COUNT_TEMPLATE, "synsets with examples", withSamplesSynsetCount) +
                String.format(COUNT_TEMPLATE, "synset examples", sampleSum) +
                String.format(COUNT_TEMPLATE, "pronunciations", pronunciationCount) +
                String.format(COUNT_TEMPLATE, "pronunciation references", pronunciationRefSum) +
                String.format(COUNT_TEMPLATE, "morphs", morphCount) +
                String.format(COUNT_TEMPLATE, "morph references", morphRefSum)
    }

    private fun reportRelations(): String {
        val synsetRelationSum = synsets
            .mapNotNull { it.relations }
            .flatMap { it.values }
            .sumOf { it.size.toLong() }
        val senseRelationSum = senses
            .mapNotNull { it.relations }
            .flatMap { it.values }
            .sumOf { it.size.toLong() }
        return String.format(COUNT_TEMPLATE, "synset relations", synsetRelationSum) +
                String.format(COUNT_TEMPLATE, "sense relations", senseRelationSum)
    }

    companion object {

        /**
         * Format for count output
         */
        private const val COUNT_TEMPLATE = "%-50s: %6d%n"
    }
}
