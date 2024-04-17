/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.Key.*
import org.oewntk.model.Lex
import org.oewntk.model.MapFactory.sensesById
import org.oewntk.model.MapFactory.synsetsById
import java.io.File
import java.io.Serializable
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * Base language model
 *
 * @property lexes   lexical items
 * @property senses  senses
 * @property synsets synsets
 * @property source  source
 */
open class CoreModel( //
	lexes: Collection<Lex>?,  //
	senses: Collection<Sense>?,  //
	synsets: Collection<Synset>?
) : Serializable {

	/**
	 * Lexical items
	 */
	@JvmField
	val lexes: Collection<Lex> = Collections.unmodifiableCollection(lexes)

	/**
	 * Senses
	 */
	@JvmField
	val senses: Collection<Sense> = Collections.unmodifiableCollection(senses)

	/**
	 * Synsets
	 */
	@JvmField
	val synsets: Collection<Synset> = Collections.unmodifiableCollection(synsets)

	/**
	 * Source
	 * Input directory
	 */
	var source: File? = null
		protected set

	/**
	 * Record input directories
	 *
	 * @param source source
	 * @return this
	 */
	fun setSource(source: File?): CoreModel {
		this.source = source
		return this
	}

	/**
	 * Generate inverse relations
	 *
	 * @return this model
	 */
	fun generateInverseRelations(): CoreModel {
		InverseRelationFactory.makeSynsetRelations(synsetsById)
		InverseRelationFactory.makeSenseRelations(sensesById)
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
	 */
	@Transient
	var synsetsById: Map<String, Synset>? = null
		/**
		 * Synsets mapped by id (synset id)
		 *
		 * @return synsets mapped by id (synset id)
		 */
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
		val csWordCount = lexes.stream()
			.map(Lex::lemma)
			.distinct()
			.count()
		val lcWordCount = lexes.stream()
			.map(Lex::lCLemma)
			.distinct()
			.count()
		val casedCount = lexes.stream()
			.map(Lex::lemma)
			.filter { it != it.lowercase() }
			.distinct()
			.count()

		val distinctByKeyOEWNLexCount = lexes.stream()
			.map { W_P_A.of_t(it) }
			.distinct()
			.count()
		val distinctByKeyShallowLexCount = lexes.stream()
			.map { W_P_D.of_t(it) }
			.distinct()
			.count()
		val distinctByKeyPOSLexCount = lexes.stream()
			.map { W_P_A.of_p(it) }
			.distinct()
			.count()
		val distinctByKeyICLexCount = lexes.stream()
			.map { W_P_A.of_lc_t(it) }
			.distinct()
			.count()
		val distinctByKeyPWNLexCount = lexes.stream()
			.map { W_P.of_lc_p(it) }
			.distinct()
			.count()
		val distinctSenseGroupsCount = lexes.stream()
			.map(Lex::sensesAsSet)
			.distinct()
			.count()
		val sensesInSenseGroupsSum = lexes.stream()
			.map(Lex::sensesAsSet)
			.distinct()
			.mapToLong { obj: Set<Sense> -> obj.size.toLong() }
			.sum()

		val withMultiSenseLexCount = lexes.stream()
			.filter { lex: Lex -> lex.senses.size > 1 }
			.count()
		val discriminantCount = lexes.stream()
			.map(Lex::discriminant)
			.filter { Objects.nonNull(it) }
			.distinct()
			.count()
		val withDiscriminantLexCount = lexes.stream()
			.filter { it.discriminant != null }
			.count()
		val withPronunciationLexCount = lexes.stream()
			.filter { it.pronunciations != null }
			.count()

		val withRelationSenseCount = senses.stream()
			.filter { it.relations != null }
			.count()
		val senseRelationSum = senses.stream()
			.map(Sense::relations)
			.filter { Objects.nonNull(it) }
			.flatMap { it!!.values.stream() }
			.mapToLong { it.size.toLong() }
			.sum()

		val withRelationSynsetCount = synsets.stream()
			.filter { it.relations != null }
			.count()
		val synsetRelationSum = synsets.stream()
			.map(Synset::relations)
			.filter { Objects.nonNull(it) }
			.flatMap { it!!.values.stream() }
			.mapToLong { it.size.toLong() }
			.sum()

		return String.format(countFormat, "lexes", lexes.size) + String.format(
			countFormat,
			"lemmas (distinct CS)",
			csWordCount
		) + String.format(
			countFormat, "lemmas (distinct LC)", lcWordCount
		) + String.format(countFormat, "lemmas (cased)", casedCount) + String.format(
			countFormat, "discriminant types", discriminantCount
		) + String.format(countFormat, "lexes with discriminant", withDiscriminantLexCount) + String.format(
			countFormat, "lexes with pronunciation", withPronunciationLexCount
		) + String.format(countFormat, "lexes with multi senses", withMultiSenseLexCount) + String.format(
			countFormat, "distinct lexes by key W_P_A_type (deep)", distinctByKeyOEWNLexCount
		) + String.format(
			countFormat, "distinct lexes by key W_P_D_type (shallow)", distinctByKeyShallowLexCount
		) + String.format(
			countFormat, "distinct lexes by key W_P_A_pos (pos)", distinctByKeyPOSLexCount
		) + String.format(
			countFormat,
			"distinct lexes by key W_P_A_lc_type (ic)",
			distinctByKeyICLexCount
		) + String.format(
			countFormat, "distinct lexes by key W_P_lc_pos (pwn)", distinctByKeyPWNLexCount
		) + String.format(countFormat, "senses", senses.size) + String.format(
			countFormat, "distinct sense sets in lexes", distinctSenseGroupsCount
		) + String.format(countFormat, "senses in sense sets", sensesInSenseGroupsSum) + String.format(
			countFormat, "senses with relations", withRelationSenseCount
		) + String.format(countFormat, "sense relations", senseRelationSum) + String.format(
			countFormat, "synsets", synsets.size
		) + String.format(countFormat, "synsets with relations", withRelationSynsetCount) + String.format(
			countFormat, "synset relations", synsetRelationSum
		)
	}

	/**
	 * Computed count about this model
	 *
	 * @return info
	 */
	fun xCounts(): String {
		counts()

		val pronunciationRefSum = lexes.stream()
			.filter { it.pronunciations != null }
			.mapToLong { it.pronunciations!!.size.toLong() }
			.sum()
		val pronunciationCount = lexes.stream()
			.filter { it.pronunciations != null }
			.flatMap { Arrays.stream(it.pronunciations) }
			.distinct()
			.count()
		val withMorphLexCount = lexes.stream()
			.filter { it.forms != null }
			.count()
		val morphRefSum = lexes.stream()
			.filter { it.forms != null }
			.mapToLong { it.forms!!.size.toLong() }
			.sum()
		val morphCount = lexes.stream()
			.filter { it.forms != null }
			.flatMap { lex: Lex -> Arrays.stream(lex.forms) }
			.distinct()
			.count()

		val withExamplesSenseCount = senses.stream()
			.filter { !it.examples.isNullOrEmpty() }
			.count()
		val withVerbFramesSenseCount = senses.stream()
			.filter { !it.verbFrames.isNullOrEmpty() }
			.count()
		val withVerbTemplatesSenseCount = senses.stream()
			.filter { it.verbTemplates != null }
			.count()
		val withTagCountSenseCount = senses.stream()
			.filter { it.tagCount != null }
			.count()

		val withSamplesSynsetCount = synsets.stream()
			.filter { !it.examples.isNullOrEmpty() }
			.count()
		val sampleSum = synsets.stream()
			.filter { !it.examples.isNullOrEmpty() }
			.mapToLong { it.examples!!.size.toLong() }
			.sum()

		return String.format(countFormat, "lexes with morphs", withMorphLexCount) +
				String.format(countFormat, "senses with verb frames", withVerbFramesSenseCount) +
				String.format(countFormat, "senses with verb templates", withVerbTemplatesSenseCount) +
				String.format(countFormat, "senses with tag count", withTagCountSenseCount) +
				String.format(countFormat, "senses with examples", withExamplesSenseCount) +
				String.format(countFormat, "synsets with examples", withSamplesSynsetCount) +
				String.format(countFormat, "synset examples", sampleSum) +
				String.format(countFormat, "pronunciations", pronunciationCount) +
				String.format(countFormat, "pronunciation references", pronunciationRefSum) +
				String.format(countFormat, "morphs", morphCount) +
				String.format(countFormat, "morph references", morphRefSum)
	}

	private fun reportRelations(): String {
		val acc = longArrayOf(0, 0)
		synsets.forEach(Consumer { synset: Synset ->
			val rr: Map<String, Set<String>>? = synset.relations
			if (rr != null && !rr.isEmpty()) {
				rr.forEach(BiConsumer { r: String?, v: Set<String> -> acc[0] += v.size.toLong() })
			}
		})
		senses.forEach(Consumer { sense: Sense ->
			val rr: Map<String, Set<String>>? = sense.relations
			if (rr != null && !rr.isEmpty()) {
				rr.forEach(BiConsumer { r: String?, v: Set<String> -> acc[1] += v.size.toLong() })
			}
		})
		val synsetRelationSum = synsets.stream()
			.map(Synset::relations)
			.filter { Objects.nonNull(it) }
			.flatMap { it!!.values.stream() }
			.mapToLong { it.size.toLong() }
			.sum()
		val senseRelationSum = senses.stream()
			.map(Sense::relations)
			.filter { Objects.nonNull(it) } //
			.flatMap { it!!.values.stream() }
			.mapToLong { it.size.toLong() }.sum()
		assert(synsetRelationSum == acc[0]) {
			String.format(
				"synset relations %d %d discrepancy",
				synsetRelationSum,
				acc[0]
			)
		}
		assert(senseRelationSum == acc[1]) {
			String.format(
				"sense relations %d %d discrepancy",
				senseRelationSum,
				acc[1]
			)
		}
		return String.format(countFormat, "synset relations", synsetRelationSum) + String.format(
			countFormat,
			"sense relations",
			senseRelationSum
		)
	}

	companion object {
		/**
		 * Format for count output
		 */
		private const val countFormat = "%-50s: %6d%n"
	}
}
