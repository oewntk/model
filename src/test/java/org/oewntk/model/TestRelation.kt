/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class TestRelation {

    private val list = listOf(
        "agent",
        "also",
        "antonym",
        "attribute",
        "body_part",
        "by_means_of",
        "causes",
        "collocation",
        "derivation",
        "destination",
        "domain_region",
        "domain_topic",
        "entails",
        "event",
        "exemplifies",
        "has_domain_region",
        "has_domain_topic",
        "holo_member",
        "holo_part",
        "holo_substance",
        "hypernym",
        "hyponym",
        "instance_hypernym",
        "instance_hyponym",
        "instrument",
        "is_caused_by",
        "is_entailed_by",
        "is_exemplified_by",
        "location",
        "material",
        "mero_member",
        "mero_part",
        "mero_substance",
        "other",
        "participle",
        "pertainym",
        "property",
        "result",
        "similar",
        "state",
        "undergoer",
        "uses",
        "vehicle",
    )

    //init {
    //    println(RELATION_RE)
    //}

    @Test
    fun testRelationMatch() {
        list.forEach {
            assert(it.isRelation()) { "$it not a relation" }
        }
    }

    @Test
    fun testRelationNonMatch() {
        assertFalse("it".isRelation())
    }
}
