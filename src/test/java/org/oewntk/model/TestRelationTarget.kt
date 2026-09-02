package org.oewntk.model

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.oewntk.model.SenseKey.Companion.isSenseKey
import org.oewntk.model.SynsetId.Companion.isSynsetId
import kotlin.test.fail

class TestRelationTarget {

    private val synsetIdList = listOf(
        "00001740-n",
    )

    private val senseKeyList = listOf(
        "jest%1:00:00::"
    )

    @Test
    fun testRelationSynsetTargets() {
        synsetIdList.forEach {
            assert(it.isSynsetId()) { "$it not a synset id" }

            val target = RelationTarget(it)
            assertTrue(target.id.isSynsetId())
            assertTrue(target.isSynsetId)
            assertFalse(target.id.isSenseKey())
            assertFalse(target.isSenseKey)

            val synsetId: SynsetId = target.synsetId
            assertTrue(synsetId.id.isSynsetId())

            try {
                target.senseKey
                fail()
            } catch (_: IllegalArgumentException) {
            }
        }
    }


    @Test
    fun testRelationSenseTargets() {
        senseKeyList.forEach {
            assert(it.isSenseKey()) { "$it not a sensekey id" }

            val target = RelationTarget(it)
            assertTrue(target.id.isSenseKey())
            assertTrue(target.isSenseKey)
            assertFalse(target.id.isSynsetId())
            assertFalse(target.isSynsetId)

            val senseKey: SenseKey = target.senseKey
            assertTrue(senseKey.id.isSenseKey())

            try {
                target.synsetId
                fail()
            } catch (_: IllegalArgumentException) {
            }
        }
    }

}