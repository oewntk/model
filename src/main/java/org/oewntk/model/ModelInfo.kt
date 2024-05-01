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

        return format("lexes", model.lexes.size) +
                format("lemmas (distinct CS)", csWordCount) +
                format("lemmas (distinct LC)", lcWordCount) +
                format("lemmas (cased)", casedCount) +
                format("discriminant types", discriminantCount) +
                format("lexes with discriminant", withDiscriminantLexCount) +
                format("lexes with pronunciation", withPronunciationLexCount) +
                format("lexes with multi senses", withMultiSenseLexCount) +
                format("distinct lexes by key W_P_A_type (deep)", distinctByKeyOEWNLexCount) +
                format("distinct lexes by key W_P_D_type (shallow)", distinctByKeyShallowLexCount) +
                format("distinct lexes by key W_P_A_pos (pos)", distinctByKeyPOSLexCount) +
                format("distinct lexes by key W_P_A_lc_type (ic)", distinctByKeyICLexCount) +
                format("distinct lexes by key W_P_lc_pos (pwn)", distinctByKeyPWNLexCount) +
                format("senses", model.senses.size) +
                format("distinct sense sets in lexes", distinctSenseGroupsCount) +
                format("senses in sense sets", sensesInSenseGroupsSum) +
                format("senses with relations", withRelationSenseCount) +
                format("sense relations", senseRelationSum) +
                format("synsets", model.synsets.size) +
                format("synsets with relations", withRelationSynsetCount) +
                format("synset relations", synsetRelationSum)
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

        return format("lexes with morphs", withMorphLexCount) +
                format("senses with verb frames", withVerbFramesSenseCount) +
                format("senses with verb templates", withVerbTemplatesSenseCount) +
                format("senses with tag count", withTagCountSenseCount) +
                format("senses with examples", withExamplesSenseCount) +
                format("synsets with examples", withSamplesSynsetCount) +
                format("synset examples", sampleSum) +
                format("pronunciations", pronunciationCount) +
                format("pronunciation references", pronunciationRefSum) +
                format("morphs", morphCount) +
                format("morph references", morphRefSum)
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
        return format("synset relations", synsetRelationSum) +
                format("sense relations", senseRelationSum)
    }

    /**
     * Format for count output
     */
    private const val COUNT_TEMPLATE = "%-50s: %6d%n"

    private fun format(label: String, value: Any): String {
        return String.format(COUNT_TEMPLATE, label, value)
    }
}
