package org.oewntk.model

/**
 * Check model
 */
fun <M : CoreModel> M.check(verbose: Boolean = true): M {
    if (verbose) Tracing.psErr.println("[I] Check members")
    checkMembers(verbose = verbose)
    if (verbose) Tracing.psErr.println("[I] Check synset relation targets")
    checkSynsetRelationTargets(verbose = verbose)
    if (verbose) Tracing.psErr.println("[I] Check sense relation targets")
    checkSenseRelationTargets(verbose = verbose)
    return this
}

/**
 * Check synset relation targets
 */
fun <M : CoreModel> M.checkSynsetRelationTargets(verbose: Boolean = true): M {
    var count = 0
    if (synsetsById != null) {
        for ((sourceSynsetId, sourceSynset) in synsetsById) {
            if (!sourceSynset.relations.isNullOrEmpty()) {
                sourceSynset.relations!!.forEach { (rel, targetSynsetIds) ->
                    if (targetSynsetIds.isNotEmpty()) {
                        for (targetSynsetId in targetSynsetIds) {
                            if (targetSynsetId[0] != 'Q' && synsetFinder(targetSynsetId) == null) {
                                count++
                                if (verbose) Tracing.psErr.println("[E] non-existing target $targetSynsetId of synset relation $rel($sourceSynsetId)")
                            }
                        }
                    } else {
                        count++
                        if (verbose) Tracing.psErr.println("[E] no target of synset relation $rel($sourceSynsetId)")
                    }
                }
            }
        }
    }
    Tracing.psErr.println("[${if (count == 0) "I" else "E"}] $count synset relations have no or non-existing target")
    return this
}

/**
 * Check sense relation targets
 */
fun <M : CoreModel> M.checkSenseRelationTargets(verbose: Boolean = true): M {
    var count = 0
    if (sensesById != null) {
        for ((sourceSenseId, sourceSense) in sensesById) {
            if (!sourceSense.relations.isNullOrEmpty()) {
                sourceSense.relations!!.forEach { (rel, targetSynsetIds) ->
                    if (targetSynsetIds.isNotEmpty()) {
                        for (targetSynsetId in targetSynsetIds) {
                            if (senseFinder(targetSynsetId) == null) {
                                count++
                                if (verbose) Tracing.psErr.println("[E] non-existing target $targetSynsetId of sense relation $rel($sourceSenseId)")
                            }
                        }
                    } else {
                        count++
                        if (verbose) Tracing.psErr.println("[E] no target of sense relation $rel($sourceSenseId)")
                    }
                }
            }
        }
    }
    Tracing.psErr.println("[${if (count == 0) "I" else "E"}] $count sense relations have no or non-existing target")
    return this
}

/**
 * Check synset members
 */
fun <M : CoreModel> M.checkMembers(verbose: Boolean = true): M {
    checkMembersDuplicates(verbose)
    checkMembersReference(verbose)
    checkMembersSenses(verbose)
    return this
}

/**
 * Check synset members
 */
fun <M : CoreModel> M.checkMembersDuplicates(verbose: Boolean = true): M {
    var count = 0
    for (synset in synsets) {
        val duplicates = synset.members.groupBy { it }
            .filter { it.value.size > 1 }
            .keys
        if (duplicates.isNotEmpty()) {
            count++
            if (verbose) Tracing.psErr.println("[E] duplicate synset ${synset.synsetId} members: $duplicates {${synset.members.joinToString()}}")
        }
    }
    Tracing.psErr.println("[${if (count == 0) "I" else "E"}] $count synsets have member duplicate(s)")
    return this
}

/**
 * Check synset members
 */
fun <M : CoreModel> M.checkMembersReference(verbose: Boolean = true): M {
    var count = 0
    for (synset in synsets) {
        val orphans = synset.members.filter { lexFinder(it) == null }.toList()
        if (orphans.isNotEmpty()) {
            count++
            if (verbose) Tracing.psErr.println("[E] members of synset ${synset.synsetId} with members {${synset.members.joinToString()}} have no entry: $orphans")
        }
    }
    Tracing.psErr.println("[${if (count == 0) "I" else "E"}] $count synsets have member(s) without entries")
    return this
}

/**
 * Check synset members
 */
fun <M : CoreModel> M.checkMembersSenses(verbose: Boolean = true): M {
    var count = 0
    for (synset in synsets) {
        if ("00821893-n" == synset.synsetId)
            Tracing.psInfo.println()
        for (member in synset.members) {
            try {
                synset.findSenseOf(member, lexResolver, senseResolver)
            } catch (_: IllegalStateException) {
                count++
                if (verbose) Tracing.psErr.println("[E] members of synset ${synset.synsetId} with members {${synset.members.joinToString()}} have no found sense for '$member'")
            }
        }
    }
    Tracing.psErr.println("[${if (count == 0) "I" else "E"}] $count synsets have member(s) with non found sense")
    return this
}
