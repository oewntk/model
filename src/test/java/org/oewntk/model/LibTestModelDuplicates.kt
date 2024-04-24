/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.model

import org.junit.Assert
import org.oewntk.model.KeyF.MonoValued
import org.oewntk.model.KeyF.MultiValued
import java.io.PrintStream

object LibTestModelDuplicates {

	private fun testDuplicatesForKeyMono(model: CoreModel, keyGetter: (Lex) -> MonoValued, ps: PrintStream) {
		val dups = model.lexes
			.map(keyGetter) // sequence of keys
			.groupBy { it }
			.mapValues { it.value.size } // map(key, count)
			.entries // sequence of (key,count) entries
			.filter { it.value > 1 } // if map value > 1, duplicate element
			.sortedBy { it.key.toString() }
			.toSet()
		ps.println(dups.size)
		dups.forEach { ps.println(it) }
		Assert.assertEquals(0, dups.size.toLong())
	}

	private fun testDuplicatesForKeyMulti(model: CoreModel, keyGetter: (Lex) -> MultiValued, ps: PrintStream) {
		val dups = model.lexes //
			.map(keyGetter) // sequence of keys
			.groupBy { it }
			.mapValues { it.value.size } // map(key, count))
			.entries // sequence  of (key,count) entries
			.filter { it.value > 1 } // if map value > 1, duplicate element
			.sortedBy { it.key.toString() } //
			.toSet()
		ps.println(dups.size)
		dups.forEach { ps.println(it) }
	}

	@JvmStatic
	fun testDuplicatesForKeyOEWN(model: CoreModel, ps: PrintStream) {
		testDuplicatesForKeyMono(model, { lex: Lex? -> KeyF.F_W_P_A.Mono.of(Lex::lemma, Lex::type, lex!!) }, ps)
	}

	@JvmStatic
	fun testDuplicatesForKeyPos(model: CoreModel, ps: PrintStream) {
		testDuplicatesForKeyMono(
			model,
			{ KeyF.F_W_P_A.Mono.of(Lex::lemma, Lex::partOfSpeech, it) },
			ps
		)
	}

	@JvmStatic
	fun testDuplicatesForKeyIC(model: CoreModel, ps: PrintStream) {
		testDuplicatesForKeyMulti(
			model,
			{ KeyF.F_W_P_A.Multi.of(Lex::lCLemma, Lex::type, it) },
			ps
		)
	}

	@JvmStatic
	fun testDuplicatesForKeyPWN(model: CoreModel, ps: PrintStream) {
		testDuplicatesForKeyMulti(
			model,
			{ KeyF.F_W_P.Multi.of(Lex::lCLemma, Lex::type, it) },
			ps
		)
	}
}
