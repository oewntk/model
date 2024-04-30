/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import java.io.PrintStream
import java.util.*
import java.util.function.Consumer

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
        classifier2: (Lex) -> K,
        classifier3: (Lex) -> K,
        model: CoreModel,
        ps: PrintStream,
    ) {
        val map2: Map<out K, List<Lex>> = lexes
            .groupBy(classifier2)
        for (k2 in map2.keys) {
            ps.printf("\t%s:%n", k2)
            val map3: Map<out K, List<Lex>> = map2[k2]!!
                .groupBy(classifier3)
            for (k3 in map3.keys) {
                ps.printf("\t\t%s:%n", keyToString(k3))
                map3[k3]!!.forEach {
                    dump(it.senses, model, "\t\t\t", ps)
                }
            }
        }
    }

    private fun dump(senses: Collection<Sense>, model: CoreModel, indent: String, ps: PrintStream) {
        senses.forEach { sense: Sense ->
            ps.printf("%s%s%n", indent, sense)
            dump(sense, model, indent + "\t", ps)
        }
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
                relations[type]!!.joinToString(",")
            )
        })

        // verbframes
        val verbFrames = sense.verbFrames
        if (verbFrames != null) {
            ps.printf("%sframes: [%s]%n", indent, verbFrames.joinToString(","))
        }

        // verbtemplates
        val verbTemplates = sense.verbTemplates
        if (verbTemplates != null) {
            ps.printf("%stemplates: [%s]%n", indent, verbTemplates.joinToString(","))
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
        ps.printf("%s{%s}%n", indent + "\t", synset.members.joinToString(","))
        ps.printf("%s%s%n", indent + "\t", synset.definitions.joinToString(","))
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
