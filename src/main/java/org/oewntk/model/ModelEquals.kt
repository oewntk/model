package org.oewntk.model

object ModelEquals {

    const val FAIL = true

    fun checkZipLexesEq(lexes1: Set<Lex>, lexes2: Set<Lex>) {
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
        if (lexes1 != lexes2) {
            val report = "different lexes"
            if (FAIL) throw IllegalStateException(report) else Tracing.psErr.println(report)
        }
    }

    fun checkZipSensesEq(senses1: Set<Sense>, senses2: Set<Sense>) {
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
        if (senses1 != senses2) {
            val report = "different senses"
            if (FAIL) throw IllegalStateException(report) else Tracing.psErr.println(report)
        }
    }

    fun checkZipSynsetsEq(synsets1: Set<Synset>, synsets2: Set<Synset>) {
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
        if (synsets1 != synsets2) {
            val report = "different synsets"
            if (FAIL) throw IllegalStateException(report) else Tracing.psErr.println(report)
        }
    }

    fun checkDataEq(
        data1: Triple<Set<Lex>, Set<Synset>, Set<Sense>>,
        data2: Triple<Set<Lex>, Set<Synset>, Set<Sense>>
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