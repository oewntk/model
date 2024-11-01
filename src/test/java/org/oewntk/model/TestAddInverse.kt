/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.model

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.InverseRelationFactory.INVERSE_SENSE_RELATIONS
import org.oewntk.model.LibNanoModel.model
import org.oewntk.model.LibNanoModel.sense11
import org.oewntk.model.LibNanoModel.senseD11
import org.oewntk.model.LibNanoModel.synset1
import org.oewntk.model.LibNanoModel.synsetH1
import java.io.PrintStream

class TestAddInverse {

    @Suppress("SameParameterValue")
    private fun dump(model: CoreModel, ps: PrintStream) {
        dumpSynsets(model, ps)
        dumpSenses(model, ps)
    }

    private fun dumpSenses(model: CoreModel, ps: PrintStream) {
        model.senses
            .forEach { sense ->
                ps.println("\t${sense.toLongString()}")
            }
    }

    private fun dumpSynsets(model: CoreModel, ps: PrintStream) {
        model.synsets
            .forEach { synset ->
                ps.println("\t${synset.toLongString()}")
            }
    }

    @Test
    fun testSynsetRelations() {
        ps.println(synset1.toLongString())
        ps.println(synsetH1.toLongString())
    }

    @Test
    fun testSenseRelations() {
        ps.println(sense11.toLongString())
        ps.println(senseD11.toLongString())
    }

    @Test
    fun testModel() {
        ps.println("model before generating inverses")
        dump(model, ps)
        ps.println("generate inverses")
        ps.println("model after generating inverses")
        model.generateInverseRelations(
            INVERSE_SENSE_RELATIONS + mapOf("also" to "also"),
            INVERSE_SENSE_RELATIONS + mapOf("also" to "also"),
        )
        dump(model, ps)
    }

    companion object {

        private val ps = if (!System.getProperties().containsKey("SILENT")) Tracing.psInfo else Tracing.psNull

        @Suppress("EmptyMethod")
        @JvmStatic
        @BeforeClass
        fun init() {
        }
    }
}
