package org.oewntk.model

import java.io.File

/**
 * Check model
 */
fun <M : CoreModel> M.check(throws: Boolean = true, verbose: Boolean = false): M {
    if (verbose) Tracing.psErr.println("[I] Lex key duplicates")
    checkLexKeyDuplicates(throws = throws, verbose = verbose)
    if (verbose) Tracing.psErr.println("[I] Lex value duplicates")
    checkLexValueDuplicates(throws = throws, verbose = verbose)

    if (verbose) Tracing.psErr.println("[I] Sense reference")
    checkSenseReference(throws = throws, verbose = verbose)

    if (verbose) Tracing.psErr.println("[I] Synset members")
    checkMembers(throws = throws, verbose = verbose)

    if (verbose) Tracing.psErr.println("[I] Synset relation targets")
    checkSynsetRelationTargets(throws = throws, verbose = verbose)
    if (verbose) Tracing.psErr.println("[I] Sense relation targets")
    checkSenseRelationTargets(throws = throws, verbose = verbose)

    Tracing.psErr.println("[I] CoreModel checked")
    return this
}

// L E X

/**
 * Check lex value duplicates
 */
fun <M : CoreModel> M.checkLexValueDuplicates(throws: Boolean = false, verbose: Boolean = true): M {
    val duplicates = lexes.groupBy { it.value }
        .filter { it.value.size > 1 }
        .keys
    if (duplicates.isNotEmpty()) {
        val duplicatesCsv = duplicates.sortedBy { it.joinToString() }.joinToString("\n")
        val state = "duplicate lex values:\n${duplicatesCsv}"
        if (throws) throw IllegalStateException(state)
        if (verbose) {
            val csvFile = File("lex-value-duplicates.log")
            csvFile.writeText(duplicatesCsv)
            Tracing.psErr.println("[E] $state")
        }
    }
    val count = duplicates.size
    if (verbose) Tracing.psErr.println("[${if (count == 0) "I" else "E"}] $count lexes have value duplicate(s)")
    return this
}

/**
 * Check lex key duplicates
 */
private val keySorter = compareBy<Triple<Lemma, Char, Discriminant?>> { it.first }.thenBy { it.second }.thenBy { it.third }

fun <M : CoreModel> M.checkLexKeyDuplicates(throws: Boolean = false, verbose: Boolean = true): M {
    val duplicates = lexes.groupBy { it.key }
        .filter { it.value.size > 1 }
        .keys
    if (duplicates.isNotEmpty()) {
        val duplicatesCsv = duplicates.sortedWith(keySorter).joinToString("\n")
        val state = "duplicate lex keys:\n${duplicatesCsv}"
        if (throws) throw IllegalStateException(state)
        if (verbose) {
            val csvFile = File("lex-key-duplicates.log")
            csvFile.writeText(duplicatesCsv)
            Tracing.psErr.println("[E] $state")
        }
    }
    val count = duplicates.size
    if (verbose) Tracing.psErr.println("[${if (count == 0) "I" else "E"}] $count lexes have key duplicate(s)")
    return this
}

// S E N S E

fun <M : CoreModel> M.checkSenseReference(throws: Boolean = false, verbose: Boolean = true): M {
    var count = 0
    senses.forEach { sense ->
        if (synsetFinder(sense.synsetId) == null) {
            count++
            val state = "${sense.senseKey} has non-existing target synset ${sense.synsetId}"
            if (throws) throw IllegalStateException(state)
            if (verbose) Tracing.psErr.println("[E] $state")
        }
    }
    if (verbose) Tracing.psErr.println("[${if (count == 0) "I" else "E"}] $count senses have no or non-existing synset target")
    return this
}

// R E L A T I O N S

/**
 * Check synset relation targets
 */
fun <M : CoreModel> M.checkSynsetRelationTargets(throws: Boolean = false, verbose: Boolean = true): M {
    var count = 0
    synsets
        .filter { !it.relations.isNullOrEmpty() }
        .forEach {
            it.relations?.forEach { (rel, targetSynsetIds) ->
                if (targetSynsetIds.isNotEmpty()) {
                    for (targetSynsetId in targetSynsetIds) {
                        if (targetSynsetId[0] != 'Q' && synsetFinder(targetSynsetId) == null) {
                            count++
                            val state = "non-existing target $targetSynsetId of synset relation $rel(${it.synsetId})"
                            if (throws) throw IllegalStateException(state)
                            if (verbose) Tracing.psErr.println("[E] $state")
                        }
                    }
                } else {
                    count++
                    val state = "no target of synset relation $rel(${it.synsetId})"
                    if (throws) throw IllegalStateException(state)
                    if (verbose) Tracing.psErr.println("[E] $state")
                }
            }
        }
    if (verbose) Tracing.psErr.println("[${if (count == 0) "I" else "E"}] $count synset relations have no or non-existing target")
    return this
}

/**
 * Check sense relation targets
 */
fun <M : CoreModel> M.checkSenseRelationTargets(throws: Boolean = false, verbose: Boolean = true): M {
    var count = 0
    senses
        .filter { !it.relations.isNullOrEmpty() }
        .forEach {
            it.relations?.forEach { (rel, targetSenseKeys) ->
                if (targetSenseKeys.isNotEmpty()) {
                    for (targetSenseKey in targetSenseKeys) {
                        if (senseFinder(targetSenseKey) == null) {
                            count++
                            val state = "non-existing target $targetSenseKey of sense relation $rel(${it.senseKey})"
                            if (throws) throw IllegalStateException(state)
                            if (verbose) Tracing.psErr.println("[E] $state")
                        }
                    }
                } else {
                    count++
                    val state = "no target of sense relation $rel(${it.senseKey})"
                    if (throws) throw IllegalStateException(state)
                    if (verbose) Tracing.psErr.println("[E] $state")
                }
            }
        }
    if (verbose) Tracing.psErr.println("[${if (count == 0) "I" else "E"}] $count sense relations have no or non-existing target")
    return this
}

// S Y N S E T  M E M B E R S

/**
 * Check synset members
 */
fun <M : CoreModel> M.checkMembers(throws: Boolean = false, verbose: Boolean = true): M {
    checkMembersDuplicates(throws = throws, verbose = verbose)
    checkMembersReference(throws = throws, verbose = verbose)
    checkMembersSenses(throws = throws, verbose = verbose)
    return this
}

/**
 * Check synset members
 */
fun <M : CoreModel> M.checkMembersDuplicates(throws: Boolean = false, verbose: Boolean = true): M {
    var count = 0
    synsets.forEach { synset ->
        val duplicates = synset.members.groupBy { it }
            .filter { it.value.size > 1 }
            .keys
        if (duplicates.isNotEmpty()) {
            count++
            val state = "duplicate synset ${synset.synsetId} members: $duplicates {${synset.members.joinToString()}}"
            if (throws) throw IllegalStateException(state)
            if (verbose) Tracing.psErr.println("[E] $state")
        }
    }
    if (verbose) Tracing.psErr.println("[${if (count == 0) "I" else "E"}]\t$count synsets have member duplicate(s)")
    return this
}

/**
 * Check synset members
 */
fun <M : CoreModel> M.checkMembersReference(throws: Boolean = false, verbose: Boolean = true): M {
    val membersWithoutLex = synsets
        .map { synset -> synset.members.filter { member -> lexFinder(member) == null }.toList() to synset }
        .filter { it.first.isNotEmpty() }
        .map { (orphans, synset) -> "$orphans;${synset.synsetId};{${synset.members.joinToString()}" }
    if (membersWithoutLex.isNotEmpty()) {
        val csv = membersWithoutLex.joinToString(separator = "\n")
        if (throws) throw IllegalStateException(csv)
        if (verbose) Tracing.psErr.println("[E] $csv")
    }
    val count = membersWithoutLex.size
    if (verbose) Tracing.psErr.println("[${if (count == 0) "I" else "E"}]\t$count synsets have member(s) without entries")

    return this
}

/**
 * Check synset members
 */
fun <M : CoreModel> M.checkMembersSenses(throws: Boolean = false, verbose: Boolean = true): M {
     val membersWithoutSense = synsets
        .map { synset -> synset.members.filter { member -> synset.findSenseOf(member, lexResolver, senseResolver) == null }.toList() to synset }
        .filter { it.first.isNotEmpty() }
        .map { (orphans, synset) -> "$orphans;${synset.synsetId};{${synset.members.joinToString()}" }
    if (membersWithoutSense.isNotEmpty()) {
        val csv = membersWithoutSense.joinToString(separator = "\n")
        if (throws) throw IllegalStateException(csv)
        if (verbose) Tracing.psErr.println("[E] $csv")
    }
    val count = membersWithoutSense.size
    if (verbose) Tracing.psErr.println("[${if (count == 0) "I" else "E"}]\t$count synsets have member(s) without resolvable sense")
    return this
}
