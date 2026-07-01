/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import java.util.*

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
        val icWordCount = model.lexes
            .map { it.lCLemma }
            .distinct()
            .count()
        val casedCount = model.lexes
            .map { it.lemma }
            .filter { it != it.lowercase(Locale.ENGLISH) }
            .distinct()
            .count()

        val distinctByKeyOEWNShallowLexCount = model.lexes
            .map { Key.UsingDiscriminant.of(it) }
            .distinct()
            .count()
        val distinctByKeyOEWNDeepLexCount = model.lexes
            .map { Key.UsingPronunciation.of(it) }
            .distinct()
            .count()

        val distinctByKeyICLexCount = model.lexes
            .map { Key.UsingDiscriminant.ofIgnoringCase(it) }
            .distinct()
            .count()
        val distinctByKeyPOSLexCount = model.lexes
            .map { Key.UsingDiscriminant.ofUsingPartOfSpeech(it) }
            .distinct()
            .count()
        val distinctByKeyPWNLexCount = model.lexes
            .map { Key.Base.ofIgnoringCaseUsingPartOfSpeech(it) }
            .distinct()
            .count()

        val distinctSenseGroupsCount = model.lexes
            .map { it.senseKeys.toSet() }
            .distinct()
            .count()
        val sensesInSenseGroupsSum = model.lexes
            .map { it.senseKeys.toSet() }
            .distinct()
            .sumOf { it.size.toLong() }

        val withMultiSenseLexCount = model.lexes
            .count { it.senseKeys.size > 1 }
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
            .sumOf { it.size}

        val withRelationSynsetCount = model.synsets
            .count { it.relations != null }
        val synsetRelationSum = model.synsets
            .mapNotNull { it.relations }
            .flatMap { it.values }
            .sumOf { it.size }

        return formatCounts("lexes", model.lexes.size) +
                formatCounts("lemmas (distinct case sensitive)", csWordCount) +
                formatCounts("lemmas (distinct ignore case)", icWordCount) +
                formatCounts("lemmas (cased)", casedCount) +
                formatCounts("discriminant types", discriminantCount) +
                formatCounts("lexes with discriminant", withDiscriminantLexCount) +
                formatCounts("lexes with pronunciation", withPronunciationLexCount) +
                formatCounts("lexes with multi senses", withMultiSenseLexCount) +
                formatCounts("distinct lexes by (case-sensitive lemma   | type | discr.) (shallow)", distinctByKeyOEWNShallowLexCount) +
                formatCounts("distinct lexes by (case-sensitive lemma   | type | pronun.) (deep)", distinctByKeyOEWNDeepLexCount) +
                formatCounts("distinct lexes by (case-sensitive lemma   | pos  | discr.) (pos)", distinctByKeyPOSLexCount) +
                formatCounts("distinct lexes by (case-insensitive lemma | type | discr.) (ci)", distinctByKeyICLexCount) +
                formatCounts("distinct lexes by (case-insensitive lemma | pos) (pwn)", distinctByKeyPWNLexCount) +
                formatCounts("senses", model.senses.size) +
                formatCounts("distinct sense sets in lexes", distinctSenseGroupsCount) +
                formatCounts("senses in sense sets", sensesInSenseGroupsSum) +
                formatCounts("senses with relations", withRelationSenseCount) +
                formatCounts("sense relations", senseRelationSum) +
                formatCounts("synsets", model.synsets.size) +
                formatCounts("synsets with relations", withRelationSynsetCount) +
                formatCounts("synset relations", synsetRelationSum, last = true)
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

        return formatCounts("lexes with morphs", withMorphLexCount) +
                formatCounts("senses with verb frames", withVerbFramesSenseCount) +
                formatCounts("senses with verb templates", withVerbTemplatesSenseCount) +
                formatCounts("senses with tag count", withTagCountSenseCount) +
                formatCounts("senses with examples", withExamplesSenseCount) +
                formatCounts("synsets with examples", withSamplesSynsetCount) +
                formatCounts("synset examples", sampleSum) +
                formatCounts("pronunciations", pronunciationCount) +
                formatCounts("pronunciation references", pronunciationRefSum) +
                formatCounts("morphs", morphCount) +
                formatCounts("morph references", morphRefSum, last = true)
    }

    private fun reportRelationCounts(model: CoreModel): String {
        val synsetRelationSum = model.synsets
            .mapNotNull { it.relations }
            .flatMap { it.values }
            .sumOf { it.size.toLong() }
        val senseRelationSum = model.senses
            .mapNotNull { it.relations }
            .flatMap { it.values }
            .sumOf { it.size.toLong() }
        return formatCounts("synset relations", synsetRelationSum) +
                formatCounts("sense relations", senseRelationSum, last = true)
    }

    /**
     * Relation types
     */
    fun relations(model: CoreModel): String {
        val synsetRelations = model.synsets
            .mapNotNull { it.relations }
            .flatMap { it.keys }
            .sorted()
            .distinct()

        val senseRelations = model.senses
            .mapNotNull { it.relations }
            .flatMap { it.keys }
            .sorted()
            .distinct()
        return format("inverses generated", model.generatedInverses) +
                format("synset relations types", synsetRelations) +
                format("sense relations types", senseRelations, last = true)
    }

    /**
     * Format for count output
     */
    private const val COUNT_TEMPLATE = "%-70s: %6d%s"

    private fun formatCounts(label: String, value: Any, last: Boolean = false): String {
        return COUNT_TEMPLATE.format(label, value, if (last) "" else "\n")
    }

    /**
     * Format for info output
     */
    private const val INFO_TEMPLATE = "%-70s: %s%s"

    private fun format(label: String, value: Any, last: Boolean = false): String {
        return INFO_TEMPLATE.format(label, value, if (last) "" else "\n")
    }
}
