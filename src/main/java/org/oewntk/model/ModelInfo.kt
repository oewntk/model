/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.Key.*

/**
 * Model info
 */
object ModelInfo {

    /**
     * Computed count about this model
     *
     * @return info
     */
    fun counts(model: CoreModel): String {
        val csWordCount = model.lexes
            .map { it.lemma }
            .distinct()
            .count()
        val lcWordCount = model.lexes
            .map { it.lCLemma }
            .distinct()
            .count()
        val casedCount = model.lexes
            .map { it.lemma }
            .filter { it != it.lowercase() }
            .distinct()
            .count()

        val distinctByKeyOEWNLexCount = model.lexes
            .map { W_P_A.of_t(it) }
            .distinct()
            .count()
        val distinctByKeyShallowLexCount = model.lexes
            .map { W_P_D.of_t(it) }
            .distinct()
            .count()
        val distinctByKeyPOSLexCount = model.lexes
            .map { W_P_A.of_p(it) }
            .distinct()
            .count()
        val distinctByKeyICLexCount = model.lexes
            .map { W_P_A.of_lc_t(it) }
            .distinct()
            .count()
        val distinctByKeyPWNLexCount = model.lexes
            .map { W_P.of_lc_p(it) }
            .distinct()
            .count()
        val distinctSenseGroupsCount = model.lexes
            .map { it.senses.toSet() }
            .distinct()
            .count()
        val sensesInSenseGroupsSum = model.lexes
            .map { it.senses.toSet() }
            .distinct()
            .sumOf { it.size.toLong() }

        val withMultiSenseLexCount = model.lexes
            .count { it.senses.size > 1 }
        val discriminantCount = model.lexes
            .mapNotNull { it.discriminant }
            .distinct()
            .count()
        val withDiscriminantLexCount = model.lexes
            .count { it.discriminant != null }
        val withPronunciationLexCount = model.lexes
            .count { it.pronunciations != null }

        val withRelationSenseCount = model.senses
            .count { it.relations != null }
        val senseRelationSum = model.senses
            .mapNotNull { it.relations }
            .flatMap { it.values }
            .sumOf { it.size.toLong() }

        val withRelationSynsetCount = model.synsets
            .count { it.relations != null }
        val synsetRelationSum = model.synsets
            .mapNotNull { it.relations }
            .flatMap { it.values }
            .sumOf { it.size.toLong() }

        return String.format(COUNT_TEMPLATE, "lexes", model.lexes.size) +
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
                String.format(COUNT_TEMPLATE, "senses", model.senses.size) +
                String.format(COUNT_TEMPLATE, "distinct sense sets in lexes", distinctSenseGroupsCount) +
                String.format(COUNT_TEMPLATE, "senses in sense sets", sensesInSenseGroupsSum) +
                String.format(COUNT_TEMPLATE, "senses with relations", withRelationSenseCount) +
                String.format(COUNT_TEMPLATE, "sense relations", senseRelationSum) +
                String.format(COUNT_TEMPLATE, "synsets", model.synsets.size) +
                String.format(COUNT_TEMPLATE, "synsets with relations", withRelationSynsetCount) +
                String.format(COUNT_TEMPLATE, "synset relations", synsetRelationSum)
    }

    /**
     * Computed count about this model
     *
     * @return info
     */
    fun xCounts(model: CoreModel): String {
        counts(model)

        val pronunciationRefSum = model.lexes
            .filter { it.pronunciations != null }
            .sumOf { it.pronunciations!!.size.toLong() }
        val pronunciationCount = model.lexes
            .filter { it.pronunciations != null }
            .flatMap { it.pronunciations!!.asSequence() }
            .distinct()
            .count()
        val withMorphLexCount = model.lexes
            .count { it.forms != null }
        val morphRefSum = model.lexes
            .filter { it.forms != null }
            .sumOf { it.forms!!.size.toLong() }
        val morphCount = model.lexes
            .filter { it.forms != null }
            .flatMap { it.forms!!.asSequence() }
            .distinct()
            .count()

        val withExamplesSenseCount = model.senses
            .count { !it.examples.isNullOrEmpty() }
        val withVerbFramesSenseCount = model.senses
            .count { !it.verbFrames.isNullOrEmpty() }
        val withVerbTemplatesSenseCount = model.senses
            .count { it.verbTemplates != null }
        val withTagCountSenseCount = model.senses
            .count { it.tagCount != null }

        val withSamplesSynsetCount = model.synsets
            .count { !it.examples.isNullOrEmpty() }
        val sampleSum = model.synsets
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

    private fun reportRelations(model: CoreModel): String {
        val synsetRelationSum = model.synsets
            .mapNotNull { it.relations }
            .flatMap { it.values }
            .sumOf { it.size.toLong() }
        val senseRelationSum = model.senses
            .mapNotNull { it.relations }
            .flatMap { it.values }
            .sumOf { it.size.toLong() }
        return String.format(COUNT_TEMPLATE, "synset relations", synsetRelationSum) +
                String.format(COUNT_TEMPLATE, "sense relations", senseRelationSum)
    }

    /**
     * Format for count output
     */
    private const val COUNT_TEMPLATE = "%-50s: %6d%n"
}
