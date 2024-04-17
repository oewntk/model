/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.model

import org.oewntk.model.Lex
import java.io.PrintStream
import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import java.util.stream.Collectors

object LibTestModelQueries {

	private val nullableDiscriminant = { lex: Lex -> Optional.ofNullable(lex.discriminant) }

	private val nullablePronunciations = { lex: Lex -> Optional.ofNullable(lex.pronunciations) }

	private fun <K> keyToString(k: K): String {
		if (k is Optional<*>) {
			val o = k as Optional<*>
			if (o.isPresent) {
				val v = o.get()
				if (v is Array<*> && v.isArrayOf<Any>()) {
					return v.contentToString()
				}
				return v.toString()
			}
			return "-"
		}
		return k.toString()
	}

	@JvmStatic
	fun testWordByType(model: CoreModel, lemma: String?, ps: PrintStream) {
		ps.println(lemma)
		val lexes = model.lexesByLemma!![lemma!!]!!
		dump(lexes, Lex::type, nullableDiscriminant, model, ps)
	}

	fun testWordByPos(model: CoreModel, lemma: String?, ps: PrintStream) {
		ps.println(lemma)
		val lexes = model.lexesByLemma!![lemma!!]!!
		dump(lexes, Lex::partOfSpeech, nullableDiscriminant, model, ps)
	}

	@JvmStatic
	fun testWordByTypeAndPronunciation(model: CoreModel, lemma: String?, ps: PrintStream) {
		ps.println(lemma)
		val lexes = model.lexesByLemma!![lemma!!]!!
		dump(lexes, Lex::type, nullablePronunciations, model, ps)
	}

	fun testWordByPosAndPronunciation(model: CoreModel, lemma: String?, ps: PrintStream) {
		ps.println(lemma)
		val lexes = model.lexesByLemma!![lemma!!]!!
		dump(lexes, Lex::partOfSpeech, nullablePronunciations, model, ps)
	}

	private fun <K> dump(
		lexes: Collection<Lex>,
		classifier2: Function<Lex, out K>,
		classifier3: Function<Lex, out K>,
		model: CoreModel,
		ps: PrintStream
	) {
		val map2: Map<out K, List<Lex>> =
			lexes.stream().collect((Collectors.groupingBy(classifier2, Collectors.toList())))
		for (k2 in map2.keys) {
			ps.printf("\t%s:%n", k2)
			val map3: Map<out K, List<Lex>> = map2[k2]!!
				.stream().collect((Collectors.groupingBy(classifier3, Collectors.toList())))
			for (k3 in map3.keys) {
				ps.printf("\t\t%s:%n", keyToString(k3))
				map3[k3]!!.forEach(Consumer { lex: Lex -> dump(lex.senses, model, "\t\t\t", ps) })
			}
		}
	}

	private fun dump(senses: List<Sense>, model: CoreModel, indent: String, ps: PrintStream) {
		senses.forEach(Consumer { sense: Sense ->
			ps.printf("%s%s%n", indent, sense)
			dump(sense, model, indent + "\t", ps)
		})
	}

	private fun dump(sense: Sense, model: CoreModel, indent: String, ps: PrintStream) {
		ps.printf(
			"%ssk=%s type=%c pos=%c lemma='%s' index=%d adj=%s synset=%s%n",
			indent,
			sense.senseKey,
			sense.type,
			sense.partOfSpeech,
			sense.lemma,
			sense.lexIndex + 1,
			sense.adjPosition,
			toShortSynset(sense.synsetId, model)
		)
		dumpSynset(sense.synsetId, model, indent + "\t", ps)

		// relations
		val relations: Map<String, Set<String>>? = sense.relations
		relations?.keys?.forEach(Consumer { type: String ->
			ps.printf(
				"%s%-28s: [%s]%n",
				indent,
				type,
				java.lang.String.join(",", relations[type])
			)
		})

		// verbframes
		val verbFrames = sense.verbFrames
		if (verbFrames != null) {
			ps.printf("%sframes: [%s]%n", indent, java.lang.String.join(",", *verbFrames))
		}

		// verbtemplates
		val verbTemplates = sense.verbTemplates
		if (verbTemplates != null) {
			ps.printf("%stemplates: [%s]%n", indent, Arrays.stream(verbTemplates).mapToObj { i: Int -> i.toString() }
				.collect(Collectors.joining(",")))
		}
	}

	private fun dumpSynset(synsetId: String, model: CoreModel, indent: String, ps: PrintStream) {
		val synset = model.synsetsById!![synsetId]
		dump(synset, indent, ps)
	}

	private fun toShortSynset(synsetId: String, model: CoreModel): String {
		val synset = model.synsetsById!![synsetId]
		return toShort(synset)
	}

	private fun toShort(synset: Synset?): String {
		return String.format(
			"%s %s '%s'",
			synset!!.synsetId,
			synset.members.contentToString(),
			toShortDefinition(synset)
		)
	}

	private fun toShortDefinition(synset: Synset?): String {
		val definition = synset!!.definition
		if (definition!!.length > 32) {
			return definition.substring(0, 32) + "..."
		}
		return definition
	}

	private fun dump(synset: Synset?, indent: String, ps: PrintStream) {
		ps.printf("%s%s%n", indent, synset!!.synsetId)
		ps.printf("%s{%s}%n", indent + "\t", java.lang.String.join(",", *synset.members))
		ps.printf("%s%s%n", indent + "\t", java.lang.String.join(",", *synset.definitions))
		val relations = synset.relations
		relations?.keys?.forEach(Consumer { type: String ->
			ps.printf(
				"%s%-20s: %s%n",
				indent + "\t",
				type,
				relations[type]
			)
		})
	}
}
