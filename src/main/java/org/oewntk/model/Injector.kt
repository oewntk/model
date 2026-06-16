package org.oewntk.model

class Injector(
    val senseToVerbTemplates: Collection<Pair<SenseKey, List<VerbTemplateId>>>?,
    val senseToTagCounts: Collection<Pair<SenseKey, TagCount>>?,
    val warnIfSensenumLessThanIndex: Boolean = false,
    val warnIfSensenumNotEqualToIndex: Boolean = false,
    val warnIfSenseNotResolvable: Boolean = false,
) {

    fun inject(model: CoreModel) {

        // inject sense's verb templates
        if (senseToVerbTemplates != null)
            for ((sensekey, templatesIds) in senseToVerbTemplates) {
                val sense = model.senseFinder(sensekey)
                if (sense != null) {
                    sense.verbTemplates = templatesIds.toSet()
                } else if (warnIfSenseNotResolvable) {
                    Tracing.psErr.println("[W] Unresolvable $sensekey with templates ${templatesIds.joinToString()}")
                }
            }

        // inject sense's tag counts
        if (senseToTagCounts != null)
            for ((sensekey, tagCount) in senseToTagCounts) {
                val sense = model.senseFinder(sensekey)
                if (sense != null) {
                    sense.tagCount = tagCount.count
                    if (sense.indexInLex + 1 != tagCount.senseNum) {
                        if (warnIfSensenumNotEqualToIndex) Tracing.psErr.println("[W] Unequal sense index ${sense.indexInLex + 1} in ${sense.senseId} with tag count sense num ${tagCount.senseNum}")
                        if (warnIfSensenumLessThanIndex && sense.indexInLex + 1 > tagCount.senseNum) Tracing.psErr.println("[W] Sense index ${sense.indexInLex + 1} in ${sense.senseId} more than tag count sense num ${tagCount.senseNum}")
                    }
                } else if (warnIfSenseNotResolvable)
                    Tracing.psErr.println("[W] Unresolvable $sensekey with tagcount $tagCount")
            }
    }
}