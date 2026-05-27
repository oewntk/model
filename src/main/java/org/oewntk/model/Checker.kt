package org.oewntk.model

import java.io.File

/**
 * Check model
 */
fun <M : CoreModel> M.check(throws: Boolean = true, verbose: Boolean = false): M {
    if (verbose) Tracing.psInfo.println("[I] Lex key duplicates")
    checkLexKeyDuplicates(throws = throws, verbose = verbose)
    if (verbose) Tracing.psInfo.println("[I] Lex value duplicates")
    checkLexValueDuplicates(throws = throws, verbose = verbose)

    if (verbose) Tracing.psInfo.println("[I] Sense reference")
    checkSenseReference(throws = throws, verbose = verbose)

    if (verbose) Tracing.psInfo.println("[I] Synset members")
    checkMembers(throws = throws, verbose = verbose)

    if (verbose) Tracing.psInfo.println("[I] Synset relation targets")
    checkSynsetRelationTargets(throws = throws, verbose = verbose)
    if (verbose) Tracing.psInfo.println("[I] Sense relation targets")
    checkSenseRelationTargets(throws = throws, verbose = verbose)

    Tracing.psInfo.println("[I] CoreModel checked")
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
            Tracing.psErr.println("[E] ${duplicates.size} lexes have value duplicate(s) logged in $csvFile")
            //Tracing.psErr.println("[E] ${duplicates.size} lexes have value duplicate(s)\n$state")
        }
    } else {
        if (verbose) Tracing.psInfo.println("[I] 0 lexes have value duplicate(s)")
    }
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
            Tracing.psErr.println("[E] ${duplicates.size} lexes have key duplicate(s) logged in $csvFile")
            // Tracing.psErr.println("[E] ${duplicates.size} lexes have key duplicate(s)\n$state")
        }
    } else {
        if (verbose) Tracing.psInfo.println("[I] 0 lexes have key duplicate(s)")
    }
    return this
}

// S E N S E

fun <M : CoreModel> M.checkSenseReference(throws: Boolean = false, verbose: Boolean = true): M {
    val orphans = senses
        .map { sense -> sense to synsetFinder(sense.synsetId) }
        .filter { it.second == null }
        .map { (sense, _) ->
            "${sense.senseKey};${sense.synsetId}"
        }
        .toList()
    if (orphans.isNotEmpty()) {
        val state = orphans.joinToString(separator = "\n")
        if (throws) throw IllegalStateException(state)
        if (verbose) {
            val csvFile = File("sense-orphan-synset.log")
            csvFile.writeText(state)
            Tracing.psErr.println("[E] ${orphans.size} senses have no or non-existing synset target logged in $csvFile")
            // Tracing.psErr.println("[E] ${orphans.size} senses have no or non-existing synset target\n$state")
        }
    } else {
        if (verbose) Tracing.psInfo.println("[I] 0 senses have no or non-existing synset target")
    }
    return this
}

// R E L A T I O N S

/**
 * Check synset relation targets
 */
fun <M : CoreModel> M.checkSynsetRelationTargets(throws: Boolean = false, verbose: Boolean = true): M {
    val instances = synsets
        .asSequence()
        .filter { !it.relations.isNullOrEmpty() }
        .flatMap { synset ->
            synset.relations!!.flatMap { (rel, targetSynsetIds) ->
                targetSynsetIds.map { targetSynsetId -> Triple(synset, rel, targetSynsetId) }
            }
        }
        .filter { (_, _, targetSynsetId) -> targetSynsetId[0] != 'Q' && synsetFinder(targetSynsetId) == null }
        .toList()

    if (instances.isNotEmpty()) {
        val state = instances.joinToString(separator = "\n") { (synset, rel, targetSynsetId) -> "${synset.synsetId};$rel;$targetSynsetId" }
        if (throws) throw IllegalStateException(state)
        if (verbose) {
            val csvFile = File("relations-synset.log")
            csvFile.writeText(state)
            Tracing.psErr.println("[E] ${instances.size} synsets have failing sense relations logged in $csvFile")
            //Tracing.psErr.println("[E] ${instances.size} synsets have failing sense relations\n$state")
        }
    } else {
        if (verbose) Tracing.psInfo.println("[I] No synsets have failing sense relations")
    }
    return this
}

/**
 * Check sense relation targets
 */
fun <M : CoreModel> M.checkSenseRelationTargets(throws: Boolean = false, verbose: Boolean = true): M {
    val instances = senses
        .asSequence()
        .filter { !it.relations.isNullOrEmpty() }
        .flatMap { sense ->
            sense.relations!!.flatMap { (rel, targetSenseKeys) ->
                targetSenseKeys.map { targetSenseKey -> Triple(sense, rel, targetSenseKey) }
            }
        }
        .filter { (_, _, targetSenseKey) -> senseFinder(targetSenseKey) == null }
        .toList()

    if (instances.isNotEmpty()) {
        val state = instances.joinToString(separator = "\n") { (sense, rel, targetSenseKey) -> "${sense.senseKey};$rel;$targetSenseKey" }
        if (throws) throw IllegalStateException(state)
        if (verbose) {
            val csvFile = File("relations-sense.log")
            csvFile.writeText(state)
            Tracing.psErr.println("[E] ${instances.size} senses have failing sense relations logged in $csvFile")
            //Tracing.psErr.println("[E] ${instances.size} senses have failing sense relations\n$state")
        }
    } else {
        if (verbose) Tracing.psInfo.println("[I] No senses have failing sense relations")
    }
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
    val instances = synsets
        .map { synset ->
            synset to synset.members.groupBy { it }
                .filter { it.value.size > 1 }
                .keys
        }
        .filter { it.second.isNotEmpty() }
        .sortedBy { it.first }

    if (instances.isNotEmpty()) {
        val state = instances.joinToString(separator = "\n") { (synset, duplicates) -> "${duplicates.joinToString(separator = ",")};${synset.synsetId};${synset.members.joinToString(separator = ",")}}" }
        if (throws) throw IllegalStateException(state)
        if (verbose) {
            val csvFile = File("member-duplicates.log")
            csvFile.writeText(state)
            Tracing.psErr.println("[E] ${instances.size} synsets have member duplicate(s) logged in $csvFile")
            //Tracing.psErr.println("[E] ${instances.size} synsets have member duplicate(s)\n$state")
        }
    } else {
        if (verbose) Tracing.psInfo.println("[I]\t0 synsets have member duplicate(s)")
    }
    return this
}

/**
 * Check synset members
 */
fun <M : CoreModel> M.checkMembersReference(throws: Boolean = false, verbose: Boolean = true): M {
    val instances: List<Pair<Synset, List<Lemma>>> = synsets
        .map { synset -> synset to synset.members.filter { member -> lexFinder(member) == null }.toList() }
        .filter { it.second.isNotEmpty() }
        .sortedBy { it.first }

    if (instances.isNotEmpty()) {
        val state = instances.joinToString(separator = "\n") { (synset, members) -> "${members.joinToString(separator = ",")};${synset.synsetId};{${synset.members.joinToString(separator = ",")}" }
        if (throws) throw IllegalStateException(state)
        if (verbose) {
            val csvFile = File("member-orphan-entry.log")
            csvFile.writeText(state)
            Tracing.psErr.println("[E] ${instances.size} synsets have member(s) without entries in $csvFile")
            //Tracing.psErr.println("[E] ${instances.size} synsets have member(s) without entries\n$state")
        }
    } else {
        if (verbose) Tracing.psInfo.println("[I]\t0 synsets have member(s) without entries")
    }
    return this
}

/**
 * Check synset members
 */
fun <M : CoreModel> M.checkMembersSenses(throws: Boolean = false, verbose: Boolean = true): M {
    val instances = synsets
        .map { synset -> synset to synset.members.filter { member -> synset.findSenseOf(member, lexResolver, senseResolver) == null }.toList() }
        .filter { it.second.isNotEmpty() }
        .sortedBy { it.first }

    if (instances.isNotEmpty()) {
        val state = instances.joinToString(separator = "\n") { (synset, members) -> "${members.joinToString(separator = ",")};${synset.synsetId};${synset.members.joinToString(separator = ",")}" }
        if (throws) throw IllegalStateException(state)
        if (verbose) {
            val csvFile = File("member-orphan-sense.log")
            csvFile.writeText(state)
            Tracing.psErr.println("[E] ${instances.size} synsets have member(s) without resolvable sense logged in $csvFile")
            // Tracing.psErr.println("[E] ${instances.size} synsets have member(s) without resolvable sense\n$state")
        }
    } else {
        if (verbose) Tracing.psInfo.println("[I]\t0 synsets have member(s) without resolvable sense")
    }
    return this
}
