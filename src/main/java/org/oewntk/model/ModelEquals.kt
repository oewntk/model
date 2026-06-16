package org.oewntk.model

import java.lang.System.identityHashCode

object ModelEquals {

    const val FAIL = true

    fun <T> Collection<T>.zipEquals(other: Collection<T>): Boolean = zip(other).none { (o1, o2) -> (o1 != o2) }

    @OptIn(ExperimentalStdlibApi::class)
    fun CoreModel.dataEquals(other: Any?): Boolean {
        if (this === other) return true
        return if (other is CoreModel) {
            val eq = lexes == other.lexes && senses == other.senses && synsets == other.synsets
            if (!eq) {
                val eqLexes = lexes == other.lexes
                val eqSenses = senses == other.senses
                val eqSynsets = synsets == other.synsets

                val zipEqLexes = lexes.zipEquals(other.lexes)
                val zipEqSynsets = synsets.zipEquals(other.synsets)
                val zipEqSenses = senses.zipEquals(other.senses)

                val lexesID = identityHashCode(lexes)
                val otherLexesID = identityHashCode(other.lexes)
                val synsetsID = identityHashCode(synsets)
                val otherSynsetsID = identityHashCode(other.synsets)
                val sensesID = identityHashCode(senses)
                val otherSensesID = identityHashCode(other.senses)

                println("-----ID(lexes) ID(synsets) ID(senses)")
                println("  A= $lexesID $synsetsID $sensesID")
                println("  B= $otherLexesID $otherSynsetsID $otherSensesID")
                println("-----class(lexes) class(synsets) class(senses)")
                println("  A= ${lexes.javaClass.name} ${synsets.javaClass.name} ${senses.javaClass.name}")
                println("  B= ${other.lexes.javaClass.name} ${other.synsets.javaClass.name} ${other.senses.javaClass.name}")
                println("-----hash(lexes) hash(synsets) hash(senses)")
                println("  A= ${lexes.hashCode()} ${synsets.hashCode()} ${senses.hashCode()}")
                println("  B= ${other.lexes.hashCode()} ${other.synsets.hashCode()} ${other.senses.hashCode()}")
                println("-----equals(lexes) equals(synsets) equals(senses)")
                println("     $eqLexes $eqSynsets $eqSenses")
                println("-----zipEquals(lexes) zipEquals(synsets) zipEquals(senses)")
                println("     $zipEqLexes $zipEqSynsets $zipEqSenses")
            }
            eq
        } else false
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun CoreModel.dataSetsEquals(other: Any?): Boolean {
        if (this === other) return true
        return if (other is CoreModel) {
            val lexesSet = lexes.toSet()
            val sensesSet = senses.toSet()
            val synsetsSet = synsets.toSet()
            val otherLexesSet = other.lexes.toSet()
            val otherSensesSet = other.senses.toSet()
            val otherSynsetsSet = other.synsets.toSet()

            val eq = lexesSet == otherLexesSet && sensesSet == otherSensesSet && synsetsSet == otherSynsetsSet
            if (!eq) {

                val eqLexesSet = lexesSet == otherLexesSet
                val eqSensesSet = sensesSet == otherSensesSet
                val eqSynsetsSet = synsetsSet == otherSynsetsSet

                val zipEqLexesSet = lexesSet.zipEquals(otherLexesSet)
                val zipEqSynsetsSet = synsetsSet.zipEquals(otherSynsetsSet)
                val zipEqSensesSet = sensesSet.zipEquals(otherSensesSet)

                val lexesSetID = identityHashCode(lexesSet).toHexString()
                val otherLexesSetID = identityHashCode(otherLexesSet).toHexString()
                val synsetsSetID = identityHashCode(synsetsSet).toHexString()
                val otherSynsetsSetID = identityHashCode(otherSynsetsSet).toHexString()
                val sensesSetID = identityHashCode(sensesSet).toHexString()
                val otherSensesSetID = identityHashCode(otherSensesSet).toHexString()

                println("-----ID(set(lexes)) ID(set(synsets)) ID(set(senses))")
                println("  A'= $lexesSetID $synsetsSetID $sensesSetID")
                println("  B'= $otherLexesSetID $otherSynsetsSetID $otherSensesSetID")
                println("-----class(set(lexes)) class(set(synsets)) class(set(senses))")
                println("  A'= ${lexesSet.javaClass.name} ${synsetsSet.javaClass.name} ${sensesSet.javaClass.name}")
                println("  B'= ${otherLexesSet.javaClass.name} ${otherSynsetsSet.javaClass.name} ${otherSensesSet.javaClass.name}")
                println("-----hash(lexes) hash(synsets) hash(senses)")
                println("  A'= ${lexesSet.hashCode()} ${synsetsSet.hashCode()} ${sensesSet.hashCode()}")
                println("  B'= ${otherLexesSet.hashCode()} ${otherSynsetsSet.hashCode()} ${otherSensesSet.hashCode()}")
                println("-----equals(set(lexes)) equals(set(synsets)) equals(set(senses))")
                println("     $eqLexesSet $eqSynsetsSet $eqSensesSet")
                println("-----zipEquals(set(lexes)) zipEquals(set(synsets)) zipEquals(set(senses))")
                println("     $zipEqLexesSet $zipEqSynsetsSet $zipEqSensesSet")
            }
            eq
        } else false
    }

    fun checkLexesEq(lexes1: List<Lex>, lexes2: List<Lex>) {
        if (lexes1 != lexes2) {
            val report = "different lexes"
            if (FAIL) throw IllegalStateException(report) else Tracing.psErr.println(report)
        }
    }

    fun checkSynsetsEq(synsets1: List<Synset>, synsets2: List<Synset>) {
        if (synsets1 != synsets2) {
            val report = "different synsets"
            if (FAIL) throw IllegalStateException(report) else Tracing.psErr.println(report)
        }
    }

    fun checkSensesEq(senses1: List<Sense>, senses2: List<Sense>) {
        if (senses1 != senses2) {
            val report = "different senses"
            if (FAIL) throw IllegalStateException(report) else Tracing.psErr.println(report)
        }
    }

    fun checkZipLexesEq(lexes1: List<Lex>, lexes2: List<Lex>) {
        lexes1.zip(lexes2).forEach { (lex1, lex2) ->
            if (lex1 != lex2) {
                val keyEq = lex1.key == lex2.key
                val valueEq = lex1.value == lex2.value
                val typeEq = lex1.type == lex2.type
                val formsEq = lex1.forms == lex2.forms
                val pronunciationsEq = lex1.pronunciations == lex2.pronunciations
                val report = "$lex1 != $lex2 : k=$keyEq v=$valueEq t=$typeEq f=$formsEq p=$pronunciationsEq"
                if (FAIL) throw IllegalStateException(report) else Tracing.psErr.println(report)
            }
        }
    }

    fun checkZipSynsetsEq(synsets1: List<Synset>, synsets2: List<Synset>) {
        synsets1.zip(synsets2).forEach { (synset1, synset2) ->
            if (synset1 != synset2) {
                val keyEq = synset1.key == synset2.key
                val valueEq = synset1.value.contentEquals(synset2.value)
                val typeEq = synset1.type == synset2.type
                val domainEq = synset1.domain == synset2.domain
                val membersEq = synset1.members == synset2.members
                val definitionsEq = synset1.definitions == synset2.definitions
                val relationsEq = synset1.relations == synset2.relations
                val examplesEq = synset1.examples == synset2.examples
                val usagesEq = synset1.usages == synset2.usages
                val iliEq = synset1.ili == synset2.ili
                val wikidataEq = synset1.wikidata == synset2.wikidata
                val sourceEq = synset1.source == synset2.source
                val report = "$synset1 != $synset2 : k=$keyEq v=$valueEq t=$typeEq d=$domainEq m=$membersEq def=$definitionsEq e=$examplesEq u=$usagesEq r=$relationsEq i=$iliEq w=$wikidataEq s=$sourceEq"
                if (FAIL) throw IllegalStateException(report) else Tracing.psErr.println(report)
            }
        }
    }

    fun checkZipSensesEq(senses1: List<Sense>, senses2: List<Sense>) {
        senses1.zip(senses2).forEach { (sense1, sense2) ->
            if (sense1 != sense2) {
                Tracing.psErr.println("$sense1 != $sense2")
                val keyEq = sense1.key == sense2.key
                val valueEq = sense1.value == sense2.value
                val typeEq = sense1.type == sense2.type
                val indexInLexEq = sense1.indexInLex == sense2.indexInLex
                val examplesEq = sense1.examples == sense2.examples
                val adjPositionEq = sense1.adjPosition == sense2.adjPosition
                val relationsEq = sense1.relations == sense2.relations
                val verbFramesEq = sense1.verbFrames == sense2.verbFrames
                val verbTemplatesEq = sense1.verbTemplates == sense2.verbTemplates
                val tagCountEq = sense1.tagCount == sense2.tagCount
                val report = "$sense1 != $sense2 : k=$keyEq v=$valueEq t=$typeEq i=$indexInLexEq e=$examplesEq ap=$adjPositionEq r=$relationsEq vf=$verbFramesEq vt=$verbTemplatesEq c=$tagCountEq"
                if (FAIL) throw IllegalStateException(report) else Tracing.psErr.println(report)
            }
        }
    }

    fun checkDataEq(
        data1: Triple<List<Lex>, List<Synset>, List<Sense>>,
        data2: Triple<List<Lex>, List<Synset>, List<Sense>>
    ) {
        val (lexes1, synsets1, senses1) = data1
        val (lexes2, synsets2, senses2) = data2
        val eqLexes = lexes1 == lexes2
        val eqSynsets = synsets1 == synsets2
        val eqSenses = senses1 == senses2
        val eq = eqLexes && eqSynsets && eqSenses
        if (!eq) {
            val report = "lexes=$eqLexes synsets=$eqSynsets senses=$eqSenses"
            if (FAIL) throw IllegalStateException(report) else Tracing.psErr.println(report)
        }
    }
}